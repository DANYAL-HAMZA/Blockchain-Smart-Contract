
// Layout of Contract:
// version
// imports
// errors
// interfaces, libraries, contracts
// Type declarations
// State variables
// Events
// Modifiers
// Functions

// Layout of Functions:
// constructor
// receive function (if exists)
// fallback function (if exists)
// external
// public
// internal
// private
// internal & private view & pure functions
// external & public view & pure functions

// SPDX-License-Identifier: MIT

        pragma solidity ^0.8.0;

import {AggregatorV3Interface} from "./AggregatorV3Interface.sol";
    import { IERC20 } from "./IERC20.sol";
    import { DecentralizedCoin } from "./DecentralizedCoin.sol";
import {ReentrancyGuard} from "./ReentrancyGuard.sol";
import {Ownable} from "./Ownable.sol";
import {Pausable} from "./Pausable.sol";
import {OracleLib} from "./OracleLib.sol";

/*
 * @title DSCEngine
 * @author Danyal Hamza
 *
 * The system is designed to be as minimal as possible, and have the tokens maintain a 1 token == $1 peg at all times.
 * This is a stablecoin with the properties:
 * - Exogenously Collateralized
 * - Dollar Pegged
 * - Algorithmically Stable
 *
 * It is similar to DAI if DAI had no governance, no fees, and was backed by only WETH and WBTC.
 *
 * Our DSC system should always be "overcollateralized". At no point, should the value of
 * all collateral < the $ backed value of all the DSC.
 *
 * @notice This contract is the core of the Decentralized Stablecoin system. It handles all the logic
 * for minting and redeeming DSC, as well as depositing and withdrawing collateral.
 * @notice This contract is based on the MakerDAO DSS system
 */
    contract DSCEngine is  ReentrancyGuard, Ownable,
    Pausable {
    ///////////////////
    // Errors
    ///////////////////
    error DSCEngine__TokenAddressesAndPriceFeedAddressesAmountsDontMatch();
    error DSCEngine__NeedsMoreThanZero();
    error DSCEngine__TokenNotAllowed(address token);
    error DSCEngine__TransferFailed();
    error DSCEngine__BreaksHealthFactor(uint256 healthFactorValue);
    error DSCEngine__MintFailed();
        error DSCEngine__BurnFailed();
    error DSCEngine__HealthFactorOk();
    error DSCEngine__HealthFactorNotImproved();

    ///////////////////
    // Types
    ///////////////////

    using OracleLib for AggregatorV3Interface;

    ///////////////////
    // State Variables
    /////////////////

        DecentralizedCoin private immutable i_dsc;
        address private immutable ownerAddress = 0x78723527393356e3BB25D030720E692eF3Cbb242;


        uint256 private constant LIQUIDATION_THRESHOLD = 50; // This means you need to be 200% over-collateralized
    uint256 private constant LIQUIDATION_BONUS = 10; // This means you get assets at a 10% discount when liquidating
    uint256 private constant LIQUIDATION_PRECISION = 100;
    uint256 private constant MIN_HEALTH_FACTOR = 1e18;
    uint256 private constant PRECISION = 1e18;
    uint256 private constant ADDITIONAL_FEED_PRECISION = 1e10;
    uint256 private constant FEED_PRECISION = 1e8;

    ///  Mapping of token address to price feed address
    mapping(address collateralToken => address priceFeed) private s_priceFeeds;
    ///  Amount of collateral deposited by user
    mapping(address user => mapping(address collateralToken => uint256 amount)) private s_collateralDeposited;

        ///  Amount of DSC minted by user
    mapping(address user => uint256 amount) private s_DSCMinted;
        mapping(address user => uint256 amount) private s_DSCMintedThroughBank;
        mapping(address user => uint256 amount) private s_DSC_BURNT;
        mapping(address user => uint256 amount) private s_DSC_TRANSFERRED;

        struct underCollateraliZedUser {
            address userAddress;
            uint256 debtToCoverByUser;
        }


        ///  If we know exactly how many tokens we have, we could make this immutable!
    address[] private s_collateralTokens;
        address[] private s_tokenHolderThroughCollateralization;
        address[] private s_tokenHoldersThroughBank;

    ///////////////////
    // Events
    ///////////////////
    event CollateralDeposited(address  user, address  token, uint256  amount);
        event CollateralDepositedByBankEvent(address  user, address  token, uint256  amount);
        event CollateralRedeemedEvent( address redeemTo, address token, uint256 amount);
        event TokenTransferEvent(address from, address to, uint256  amount) ;
        event TokenBurnEvent(address from, uint256 amount) ;
        event TokenMintEvent(address  to, uint256 amount) ;
        event BankTokenMintEvent(address  to, uint256 amount) ;
        event LiquidationEvent(address  user, address liquidator, uint256 debtToCover) ;



        // if redeemFrom != redeemedTo, then it was liquidated

    ///////////////////
    // Modifiers
    ///////////////////
    modifier moreThanZero(uint256 amount) {
    if (amount == 0) {
    revert DSCEngine__NeedsMoreThanZero();
    }
    _;
    }

    modifier isAllowedToken(address token) {
    if (s_priceFeeds[token] == address(0)) {
    revert DSCEngine__TokenNotAllowed(token);
    }
    _;
    }

    ///////////////////
    // Functions
    ///////////////////

    constructor(address[] memory tokenAddresses, address[] memory priceFeedAddresses, address dscAddress) Ownable(ownerAddress)
     {


    if (tokenAddresses.length != priceFeedAddresses.length) {
    revert DSCEngine__TokenAddressesAndPriceFeedAddressesAmountsDontMatch();
    }
    /* These feeds will be the USD pairs
     For example ETH / USD or MKR / USD
     */
    for (uint256 i = 0; i < tokenAddresses.length; i++) {
    s_priceFeeds[tokenAddresses[i]] = priceFeedAddresses[i];
    s_collateralTokens.push(tokenAddresses[i]);
    }
i_dsc = new DecentralizedCoin();

    }

    ///////////////////
    // External Functions
    ///////////////////
    /*
     * @param tokenCollateralAddress: The ERC20 token address of the collateral you're depositing
     * @param amountCollateral: The amount of collateral you're depositing
     * @param amountDscToMint: The amount of DSC you want to mint
     * @notice This function will deposit your collateral and mint DSC in one transaction
     */

    function depositCollateralAndMintDsc(
    address tokenCollateralAddress,
    uint256 amountDscToMintInUSD,
    address user
    )
    external
    nonReentrant
    whenNotPaused
    {
        uint256 requiredCollateralAmount = calculate150PercentOfCollateralAmountToMint(amountDscToMintInUSD);
        require(requiredCollateralAmount >= amountDscToMintInUSD, "deposit 150% of the DSC amount you want to mint");

    uint256 collateralAmountFromDSCToMint = getTokenAmountFromUsd(tokenCollateralAddress, requiredCollateralAmount);

    depositCollateral(tokenCollateralAddress,user, collateralAmountFromDSCToMint);
    mintDsc(user ,amountDscToMintInUSD);
    }

    /*
     * @parameter tokenCollateralAddress: The ERC20 token address of the collateral you're depositing
     * @parameter amountCollateral: The amount of collateral you're depositing
     * @parameter amountDscToBurn: The amount of DSC you want to burn
     * @notice This function will withdraw your collateral and burn DSC in one transaction
     * @notice The reverseIfHealthFactorIsBroken modifier makes sure the user deposits more collateral than the amount DSC to be minted, maintaining
      overcollateralization
     */

    /*used if you want to just convert your DSC into colateral tokens( WETH or DAI)
         * @notice Is amountCollateral not equal to amountDSCToBurn? Why do we have to repeat it
         */
    function redeemCollateralForDsc(
    address tokenCollateralAddress,
    uint256 amountDscInUSD,
    address user
    )
    external
    moreThanZero(amountDscInUSD)
    isAllowedToken(tokenCollateralAddress)
    whenNotPaused
    {

    _burnDsc(user,amountDscInUSD);
        uint256 collateralAmountFromDSCToBurn = getTokenAmountFromUsd(tokenCollateralAddress, amountDscInUSD);
    _redeemCollateral(tokenCollateralAddress, collateralAmountFromDSCToBurn, user);
    revertIfHealthFactorIsBroken(user);
    }

    /*
     * @param tokenCollateralAddress: The ERC20 token address of the collateral you're redeeming
     * @param amountCollateral: The amount of collateral you're redeeming
     * @notice This function will redeem your collateral.
     * @notice If you have DSC minted, you will not be able to redeem until you burn your DSC
     * @notice This function redeems your collateral only if you have already burnt an amount of your tokens using the burnDSC function.
     */
    function redeemCollateral(address tokenCollateralAddress, uint256 amountCollateralInUSD, address user)
    external
    moreThanZero(amountCollateralInUSD)
    nonReentrant
    isAllowedToken(tokenCollateralAddress)
    whenNotPaused
    {
        uint256 collateralAmountFromDSCToBurn = getTokenAmountFromUsd(tokenCollateralAddress, amountCollateralInUSD);
    _redeemCollateral(tokenCollateralAddress, collateralAmountFromDSCToBurn, user);
    revertIfHealthFactorIsBroken(user);
    }

    /*
     * @notice careful! You'll burn your DSC here! Make sure you want to do this...
     * @dev you might want to use this if you're nervous you might get liquidated and want to just burn
     * you DSC but keep your collateral in.
     */

    function burnDsc(address user,uint256 amount) external moreThanZero(amount) whenNotPaused {
    _burnDsc(user, amount);
    revertIfHealthFactorIsBroken(user); // I don't think this would ever hit...
    }

    /*
     * @param collateral: The ERC20 token address of the collateral you're using to make the protocol solvent again.
     * This is collateral that you're going to take from the user who is insolvent.
     * In return, you have to burn your DSC to pay off their debt, but you don't pay off your own.
     * @param user: The user who is insolvent. They have to have a _healthFactor below MIN_HEALTH_FACTOR
     * @param debtToCover: The amount of DSC you want to burn to cover the user's debt.
     *
     * @notice: You can partially liquidate a user.
     * @notice: You will get a 10% LIQUIDATION_BONUS for taking the users funds.
    * @notice: This function working assumes that the protocol will be roughly 150% overcollateralized in order for this
    to work.
    * @notice: A known bug would be if the protocol was only 100% collateralized, we wouldn't be able to liquidate
    anyone.
     * For example, if the price of the collateral plummeted before anyone could be liquidated.
     */
        /* @notice How do we find and include the debtToCover, since it should be known before the function can work.
        */
    function liquidate(
    address collateral,
    address user,
    address liquidator,
    uint256 debtToCover
    )
    external
    moreThanZero(debtToCover)
    nonReentrant
    whenNotPaused
    {
    uint256 startingUserHealthFactor = _healthFactor(user);
    if (startingUserHealthFactor >= MIN_HEALTH_FACTOR) {
    revert DSCEngine__HealthFactorOk();
    }
    /*If we are covering for 100 DSC, we need  $100 of collateral
        This is because th DSC is directly pegged to the dollar
    */
    uint256 tokenAmountFromDebtCovered = getTokenAmountFromUsd(collateral, debtToCover);
    /* And give them a 10% bonus // So we are giving the liquidator $110 of WETH for 100 DSC
     We should implement a feature to liquidate in the event the protocol is insolvent
     And sweep extra amounts into a treasury
    */
    uint256 bonusCollateral = (tokenAmountFromDebtCovered * LIQUIDATION_BONUS) / LIQUIDATION_PRECISION;
    /* Burn DSC equal to debtToCover// Figure out how much collateral to recover based on how much burnt
    This function transfers collateral amount(WETH or DAI) equivalent to deptToCover plus bonus from
    the liquidated user to the liquidator.
    */
    _redeemCollateral(collateral, tokenAmountFromDebtCovered + bonusCollateral,  liquidator);
    _burnDsc(liquidator, debtToCover);
        s_DSCMinted[liquidator] -= debtToCover;
        s_collateralDeposited[user][collateral] -= (tokenAmountFromDebtCovered + bonusCollateral);

    uint256 endingUserHealthFactor = _healthFactor(user);
    // This conditional should never hit, but just in case
    if (endingUserHealthFactor <= startingUserHealthFactor) {
    revert DSCEngine__HealthFactorNotImproved();
    }
        /* @notice This function makes sure the liquidator has a safe health factor in order for them to be able to liquidate.
        */
        emit LiquidationEvent(user,liquidator,debtToCover);
    revertIfHealthFactorIsBroken(liquidator);
    }

    ///////////////////
    // Public Functions
    ///////////////////
    /*
     * @param amountDscToMint: The amount of DSC you want to mint
     * You can only mint DSC if you hav enough collateral
     */

    function mintDsc(address to,uint256 amountDscToMint) public moreThanZero(amountDscToMint)
    whenNotPaused
    nonReentrant {
    s_DSCMinted[to] += amountDscToMint;
    revertIfHealthFactorIsBroken(to);
    bool minted = i_dsc.mint(to, amountDscToMint * PRECISION);

    if (minted != true) {
    revert DSCEngine__MintFailed();
    }
        s_tokenHolderThroughCollateralization.push(to);
        emit TokenMintEvent(to, amountDscToMint);
    }
        /* @notice This function is used at the bank level when the bank account holder is wishes to convert their
        stored cash into bank DSC. No collateral will be deposited in this case.
        Hence all users converting their cash into banks DSC will not be checked for broken health factor. They will
        also not be liquidated since their DSC are backed by real cash at the bank.
        */
        function mintDscForBank(address to, uint256 amountDSC )
        public moreThanZero(amountDSC) nonReentrant onlyOwner whenNotPaused  {
            s_DSCMintedThroughBank[to] += amountDSC;
            bool minted = i_dsc.mint(to, amountDSC * PRECISION);
            if(minted != true){
                revert DSCEngine__MintFailed();
}
            s_tokenHoldersThroughBank.push(to);
            emit BankTokenMintEvent(to, amountDSC);
        }
/*Here we do not burn the tokens because since it is a transfer to another address, the tokens are still valid
as they still represent cash at the bank
*/
        function transferDscFromUser1ToUser2(uint256 amount, address from, address to)
        public moreThanZero(amount) nonReentrant whenNotPaused
        {
            s_DSC_TRANSFERRED[from] += amount;
            uint256 balance = i_dsc.balanceOf(from);
            require(balance >= amount,"insufficient balance");
            bool success = i_dsc.transferFrom(from, to, amount * PRECISION);

            if (!success) {
                revert DSCEngine__TransferFailed();
            }
            emit TokenTransferEvent(from, to, amount);

        }
//@@notice This function is useful when a user who minted DSC tokens by depositing collateral wants to convert their DSC into physical cash
        function transferCollateralToBankAddressAndBurnEquivalentDSc(address user, address bankAddress, uint256 amountToConvertToCash, address collateralAddress)
        public
        moreThanZero(amountToConvertToCash)
        isAllowedToken(collateralAddress)
        nonReentrant
        whenNotPaused
        {
            uint256 balance = i_dsc.balanceOf(user);
            require(balance >= amountToConvertToCash,"insufficient balance");
            uint256 tokenAmountFromAmountToConvert = getTokenAmountFromUsd(collateralAddress, amountToConvertToCash);
            bool success = IERC20(collateralAddress).transfer(bankAddress,tokenAmountFromAmountToConvert * PRECISION);

            if(success){
                _burnDsc(user,amountToConvertToCash);
                s_collateralDeposited[user][collateralAddress] -= tokenAmountFromAmountToConvert;
                s_DSCMinted[user] -= amountToConvertToCash;
            }
            else{
            revert DSCEngine__TransferFailed();
            }
            revertIfHealthFactorIsBroken(user);
        }

    /*

     * @param tokenCollateralAddress: The ERC20 token address of the collateral you're depositing
     * @param amountCollateral: The amount of collateral you're depositing
     */
    //@notice uses IERC20 for transfer because we are transfering the WBTC and WETH by providing their addresses.
    function depositCollateral(
    address tokenCollateralAddress,
    address user,
    uint256 amountCollateralInUSD
    )
    public
    moreThanZero(amountCollateralInUSD)
    nonReentrant
    isAllowedToken(tokenCollateralAddress)

    {

        //@@notice the sender must be approved
        uint256 collateralAmountFromDSCToBurn = getTokenAmountFromUsd(tokenCollateralAddress, amountCollateralInUSD);
    bool success = IERC20(tokenCollateralAddress).transferFrom(user, address(this) , collateralAmountFromDSCToBurn * PRECISION);
    if (!success) {
    revert DSCEngine__TransferFailed();
    }
        s_collateralDeposited[user][tokenCollateralAddress] += amountCollateralInUSD;
        emit CollateralDeposited(user, tokenCollateralAddress, amountCollateralInUSD);
    }

    ///////////////////
    // Private Functions
    ///////////////////

    //used within the liquidate function
    function _redeemCollateral(
    address tokenCollateralAddress,
    uint256 amountCollateral,
    address to
    )
    private
    nonReentrant
    moreThanZero(amountCollateral)
    {
    s_collateralDeposited[to][tokenCollateralAddress] -= amountCollateral;
    emit CollateralRedeemedEvent( to, tokenCollateralAddress, amountCollateral);
    bool success = IERC20(tokenCollateralAddress).transfer(to, amountCollateral * PRECISION);
    if (!success) {
    revert DSCEngine__TransferFailed();
    }
    }

    /* @notice This method is used in the redeemColateral function, redeemCollateralForDSC function and also
    the burnDSC function.
    */

    function _burnDsc(address from, uint256 amount) public nonReentrant moreThanZero(amount)   {
    s_DSC_BURNT[from] += amount;
        s_DSCMinted[from] -= amount;
    bool success = i_dsc.burn(from, amount * PRECISION );

    if (!success) {
    revert DSCEngine__TransferFailed();
    }
    emit TokenBurnEvent(from,amount);

    }

    //////////////////////////////
    // Private & Internal View & Pure Functions
    //////////////////////////////

    function _getAccountInformation(address user)
    private
    view
    returns (uint256 totalDscMinted, uint256 collateralValueInUsd)
    {
    totalDscMinted = s_DSCMinted[user];
    collateralValueInUsd = getAccountCollateralValue(user);
    }
        /* @notice This function returns all under collateralized users and their depth, enabling the liquidators to know
        the exact users and amount to liquidate.
        */
        function getUnderCollateralizedUsersAndTheirDebt() public view returns (address,uint256){
            uint256 debtToCoverByUser;
            address underCollateralizedUser;
            for(uint256 index = 1; index <= s_tokenHolderThroughCollateralization.length; index++ ){
                address userAddress = s_tokenHolderThroughCollateralization[index];
            (uint256 totalDscMintedByUser,uint256 totalCollateralValueOfUserInUsd) = _getAccountInformation(userAddress);
                if(totalCollateralValueOfUserInUsd < totalDscMintedByUser){
                 uint256 debtToCover = (totalDscMintedByUser - totalCollateralValueOfUserInUsd);
                    debtToCoverByUser = debtToCover;
                    underCollateralizedUser = userAddress;

                }
            }
            return (underCollateralizedUser,debtToCoverByUser);
        }

    function _healthFactor(address user) private view returns (uint256) {
    (uint256 totalDscMinted, uint256 collateralValueInUsd) = _getAccountInformation(user);
    return _calculateHealthFactor(totalDscMinted, collateralValueInUsd);
    }

    function _getUsdValue(address token, uint256 amount) private view returns (uint256) {
    AggregatorV3Interface priceFeed = AggregatorV3Interface(s_priceFeeds[token]);
    (, int256 price,,,) = priceFeed.staleCheckLatestRoundData();
    /* 1 ETH = 1000 USD
     The returned value from Chainlink will be 1000 * 1e8
     Most USD pairs have 8 decimals, so we will just pretend they all do
     We want to have everything in terms of WEI, so we add 10 zeros at the end
     We then divide by the PRECISION(1e18) to get the usd value
      */
    return ((uint256(price) * ADDITIONAL_FEED_PRECISION) * amount) / PRECISION;
    }

    function _calculateHealthFactor(
    uint256 totalDscMinted,
    uint256 collateralValueInUsd
    )
    internal
    pure
    returns (uint256)
    {
    if (totalDscMinted == 0) return type(uint256).max;
    uint256 collateralAdjustedForThreshold = (collateralValueInUsd * LIQUIDATION_THRESHOLD) / LIQUIDATION_PRECISION;
    return (collateralAdjustedForThreshold * PRECISION) / totalDscMinted;
    }

    function revertIfHealthFactorIsBroken(address user) internal view {
    uint256 userHealthFactor = _healthFactor(user);
    if (userHealthFactor < MIN_HEALTH_FACTOR) {
    revert DSCEngine__BreaksHealthFactor(userHealthFactor);
    }
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    // External & Public View & Pure Functions
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    function calculateHealthFactor(
    uint256 totalDscMinted,
    uint256 collateralValueInUsd
    )
    external
    pure
    returns (uint256)
    {
    return _calculateHealthFactor(totalDscMinted, collateralValueInUsd);
    }

    function getAccountInformation(address user)
    external
    view
    returns (uint256 totalDscMinted, uint256 collateralValueInUsd)
    {
    return _getAccountInformation(user);
    }

    function getUsdValue(
    address token,
    uint256 amount // in WEI
    )
    external
    view
    returns (uint256)
    {
    return _getUsdValue(token, amount);
    }

    function getCollateralBalanceOfUser(address user, address token) external view returns (uint256) {
    return s_collateralDeposited[user][token];
    }

        /* @notice This function calculates the collateral value in USD deposited by user
        So we can compare this function to the DSC balance of the user to know if they are under collateralized
        */
    function getAccountCollateralValue(address user) public view returns (uint256 totalCollateralValueInUsd) {
    for (uint256 index = 0; index < s_collateralTokens.length; index++) {
    address token = s_collateralTokens[index];
    uint256 amount = s_collateralDeposited[user][token];
    totalCollateralValueInUsd += _getUsdValue(token, amount);
    }
    return totalCollateralValueInUsd;
    }

        /*
        We can use this function to calculate the collateral amount in USD needed to mint an equivalent amount of DSC
        */
    function getTokenAmountFromUsd(address token, uint256 usdAmountInWei) public view returns (uint256) {
    AggregatorV3Interface priceFeed = AggregatorV3Interface(s_priceFeeds[token]);
    (, int256 price,,,) = priceFeed.staleCheckLatestRoundData();
    /* $100e18 USD Debt
    1 ETH = 2000 USD
     The returned value from Chainlink will be 2000 * 1e8
     Most USD pairs have 8 decimals, so we will just pretend they all do
     */
    return ((usdAmountInWei * PRECISION) / (uint256(price) * ADDITIONAL_FEED_PRECISION));
    }

    function getPrecision() external pure returns (uint256) {
    return PRECISION;
    }
        function calculate150PercentOfCollateralAmountToMint(uint256 amountDscToMintInUsd) public pure
        returns (uint256){
           return (amountDscToMintInUsd * LIQUIDATION_THRESHOLD) / LIQUIDATION_PRECISION;

        }

    function getAdditionalFeedPrecision() external pure returns (uint256) {
    return ADDITIONAL_FEED_PRECISION;
    }

    function getLiquidationThreshold() external pure returns (uint256) {
    return LIQUIDATION_THRESHOLD;
    }

    function getLiquidationBonus() external pure returns (uint256) {
    return LIQUIDATION_BONUS;
    }

    function getLiquidationPrecision() external pure returns (uint256) {
    return LIQUIDATION_PRECISION;
    }

    function getMinHealthFactor() external pure returns (uint256) {
    return MIN_HEALTH_FACTOR;
    }

    function getCollateralTokens() external view returns (address[] memory) {
    return s_collateralTokens;
    }

    function getDsc() external view returns (address) {
    return address(i_dsc);
    }

    function getCollateralTokenPriceFeed(address token) external view returns (address) {
    return s_priceFeeds[token];
    }

    function getHealthFactor(address user) external view returns (uint256) {
    return _healthFactor(user);
    }
    function getTotalTokensTransferredByUser(address user)public view returns(uint256)  {
    return s_DSC_TRANSFERRED[user];
    }
    function getTotalTokensMintedToUser(address user)public view returns(uint256)  {
    return s_DSCMinted[user];
    }
    function getTotalTokensBurnt(address user)public view returns(uint256)  {
    return s_DSC_BURNT[user];
    }



        function getAllUsersThroughBankApplication() public view returns (address[] memory){
            return s_tokenHoldersThroughBank;
        }

        function getAllUsersThroughCollateralization() public view returns (address[] memory){
            return s_tokenHolderThroughCollateralization;
        }

    function isValidToken(address user) public view returns (bool) {
        return i_dsc.balanceOf(user) > 0;

    }
        function pause() external onlyOwner whenNotPaused{
            _pause();
        }
        function unPause() external onlyOwner whenPaused{
            _unpause();
        }


    }




