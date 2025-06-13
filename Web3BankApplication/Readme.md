# Web3BankApplication
Web3BankApplication is a web3 powered bank application that seems to improve upon the 
traditional bank application system in order to meet the constantly evolving architecture
of finance. The application is made of five services with each performing distinct functions.

## Features
### Traditional Bank Application
> This service follows the traditional known technique of designing bank application systems.
- Register an account with the bank.
- Update user 
- debit an account
- credit an account
- fetch all registered users
- fetch a particular registered user
- transfer cash from one account to another
- make enquiry about user account balance
- make enquiry about username
- Generate bank statement for made transactions

### Blockchain Powered Bank Application
> This service contains functions that allows users to perform advanced transactions based on deployed smart contract
> transactions. The main improvement here is the ability of a non account user to possess funds via bank stable coin,
> which can be withdrawn at anytime.

- Convert an amount of your stored cash into custom bank stable coin backed by openzeppelin ERC20 contracts. 
The equivalent amount of stable coin is minted to provided user wallet address.
- Convert minted stable coin back to cash.
- Transfer tokens to both account and non account holder
- Deposit tokens to withdraw equivalent amount at your own pace, via mobile money
- Generate bank statements containing transactions over a specific period of time

### Defi Application (Decentralized finance)
> This service is a full on chain decentralized financial service powered by the same smart contract used in the
> Blockchain Powered Application

- Deposit Collateral(DAI/BTC/ETH) and mint stable coin
> The stable coin is 150% collateralized. Hence, one must deposit 150% of the stable coin amount
> they would like to mint as collateral
- Redeem Collateral for stable coin
> Users can return their stable coins to redeem their collateral
- Redeem Collateral
> Users can redeem collateral only if they have originally deposited an equivalent amount of stable coin
- Burn DSC
> Users can burn the stable coins in their possession if they are scared of being liquidated
- Liquidate User
> users are liquidated if they become under collateralized. Under col-lateralization occurs when a user's collateral 
> is less in value than  the stable coins in his/her possession. Another user called the liquidator liquidates the 
> under collateralized user. Liquidation is done by burning the debt amount of stable coins from the 
> liquidator's account. An equivalent amount plus a liquidation bonus amount is then transferred from the
> protocol to the liquidator's account
- Deposit Collateral
> Users are allowed to deposit collateral and mint equivalent amount of stable coins at a later time
- Mint Decentralized Token
> Users can mint an amount of stable coin if they have already deposited an equivalent amount in collateral
- Generate statements of transactions

### Common Service 
> The common service contain functions and classes used in multiple services
## Tech Stack
- spring boot
- maven
- solidity
- web3j
- web3j-evm
- chain link price feeds
- openzeppelin contracts