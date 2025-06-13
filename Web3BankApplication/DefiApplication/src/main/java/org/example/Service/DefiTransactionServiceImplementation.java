package org.example.Service;

import org.example.Entity.*;
import org.example.Repository.DefiTransactionRepository;
import org.example.Repository.DefiUserRepository;
import org.example.dto.DefiTransactionRequest;
import org.example.dto.EmailDetails;
import org.example.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.web3j.model.DSCEngine;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;

import java.math.BigInteger;

public class DefiTransactionServiceImplementation implements DefiTransactionService{
    @Autowired
    private final DefiUserRepository defiUserRepository;
    @Autowired

    private final BlockchainService blockchainService;
    @Autowired

    private final DefiTransactionRepository defiTransactionRepository;
    @Autowired

    private final BlockchainEventServiceImplementation blockchainEventServiceImplementation;


    public DefiTransactionServiceImplementation(DefiUserRepository defiUserRepository, BlockchainService blockchainService, DefiTransactionRepository defiTransactionRepository, EmailService emailService, BlockchainEventServiceImplementation blockchainEventServiceImplementation) {
        this.defiUserRepository = defiUserRepository;
        this.blockchainService = blockchainService;
        this.defiTransactionRepository = defiTransactionRepository;
        this.blockchainEventServiceImplementation = blockchainEventServiceImplementation;
    }


    @Override
    public Response depositCollateralAndMintDsc(DefiTransactionRequest defiTransactionRequest) throws Exception {
        boolean isUserExists = defiUserRepository.existsByWalletAddress(defiTransactionRequest.getUserAddress());
TransactionReceipt transactionReceipt;
        if (!isUserExists ) {
            return Response.builder()
                    .responseCode(ResponseUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(ResponseUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .data(null)
                    .build();
        }
        String walletAddress = defiTransactionRequest.getUserAddress();

        DSCEngine transactions = blockchainService.loadContract();
        DefiUser user = defiUserRepository.findByWalletAddress(defiTransactionRequest.getUserAddress());
        Boolean isValidToken = transactions.isValidToken(walletAddress).send();



        if(ResponseUtils.isValidAddress(walletAddress) && isValidToken) {
             transactionReceipt = transactions.depositCollateralAndMintDsc(defiTransactionRequest.getTokenCollateralAddress(),defiTransactionRequest.getAmount().toBigInteger(),defiTransactionRequest.getUserAddress()).send();
            if(!transactionReceipt.getTransactionHash().isEmpty()) {


                DefiTransaction transaction = DefiTransaction.builder()
                        .transactionHash(transactionReceipt.getTransactionHash())
                        .transactionType("Collateral Deposit And Token Mint")
                        .amount(defiTransactionRequest.getAmount())
                        .tokenCollateralAddress(defiTransactionRequest.getTokenCollateralAddress())
                        .userAddress(defiTransactionRequest.getUserAddress())

                        .build();
                defiTransactionRepository.save(transaction);
            }
        }
        else
        {
            throw new IllegalArgumentException("Invalid User or Insufficient Account Balance");
        }

blockchainEventServiceImplementation.listenTokenDepositEvent();
        blockchainEventServiceImplementation.listenTokenMintEvent();

        EmailDetails DepositAndMintAlert = EmailDetails.builder()
                .subject("DANYAL BANK BLOCKCHAIN COLLATERAL DEPOSIT AND TOKEN MINT ALERT")
                .recipient(user.getEmail())
                .messageBody("A token amount of " + defiTransactionRequest.getAmount() + "has been minted  to your wallet address, and the same collateral" +
                        " collateral amounnt of" + defiTransactionRequest.getAmount() +
                        "has been transferred from your wallet address")
                .build();
        EmailService.sendSimpleEmail(DepositAndMintAlert);

        return Response.builder()
                .responseCode(ResponseUtils.TOKEN_MINT_SUCCESSFUL_CODE)
                .responseMessage(ResponseUtils.TOKEN_MINT_SUCCESSFUL_MESSAGE)
                .tokenData(TokenData.builder()
                        .transactionHash(transactionReceipt.getTransactionHash())
                        .tokenCollateralAddress(defiTransactionRequest.getTokenCollateralAddress())
                        .amount(defiTransactionRequest.getAmount())
                        .userAddress(defiTransactionRequest.getUserAddress())
                        .build())
                .build();
    }

    @Override
    public Response redeemCollateralForDsc(DefiTransactionRequest defiTransactionRequest) throws Exception {
        boolean isUserExists = defiUserRepository.existsByWalletAddress(defiTransactionRequest.getUserAddress());
        TransactionReceipt transactionReceipt;
        if (!isUserExists ) {
            return Response.builder()
                    .responseCode(ResponseUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(ResponseUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .data(null)
                    .build();
        }
        String walletAddress = defiTransactionRequest.getUserAddress();

        DSCEngine transactions = blockchainService.loadContract();
        DefiUser user = defiUserRepository.findByWalletAddress(defiTransactionRequest.getUserAddress());
        Boolean isValidToken = transactions.isValidToken(walletAddress).send();



        if(ResponseUtils.isValidAddress(walletAddress) & isValidToken) {
            transactionReceipt = transactions.redeemCollateralForDsc(defiTransactionRequest.getTokenCollateralAddress(),defiTransactionRequest.getAmount().toBigInteger(),defiTransactionRequest.getUserAddress()).send();
            if(!transactionReceipt.getTransactionHash().isEmpty()) {


                DefiTransaction transaction = DefiTransaction.builder()
                        .transactionHash(transactionReceipt.getTransactionHash())
                        .transactionType("Redeem Collateral For Dsc")
                        .amount(defiTransactionRequest.getAmount())
                        .tokenCollateralAddress(defiTransactionRequest.getTokenCollateralAddress())
                        .userAddress(defiTransactionRequest.getUserAddress())

                        .build();
                defiTransactionRepository.save(transaction);
            }
        }
        else
        {
            throw new IllegalArgumentException("Invalid User or Insufficient Account Balance");
        }

        blockchainEventServiceImplementation.listenTokenRedeemEvent();


        EmailDetails CollateralRedeemAlert = EmailDetails.builder()
                .subject("DANYAL BANK BLOCKCHAIN COLLATERAL REDEEM ALERT")
                .recipient(user.getEmail())
                .messageBody("Token Collateral of " + defiTransactionRequest.getAmount()  +
                        "has been transferred to your wallet address")
                .build();
        EmailService.sendSimpleEmail(CollateralRedeemAlert);

        return Response.builder()
                .responseCode(ResponseUtils.COLLATERAL_REDEEMED_SUCCESSFULLY)
                .responseMessage(ResponseUtils.COLLATERAL_REDEEM_SUCCESSFUL_MESSAGE)
                .tokenData(TokenData.builder()
                        .transactionHash(transactionReceipt.getTransactionHash())
                        .tokenCollateralAddress(defiTransactionRequest.getTokenCollateralAddress())
                        .amount(defiTransactionRequest.getAmount())
                        .userAddress(defiTransactionRequest.getUserAddress())
                        .build())
                .build();

    }

    @Override
    public Response redeemCollateral(DefiTransactionRequest defiTransactionRequest) throws Exception {
        boolean isUserExists = defiUserRepository.existsByWalletAddress(defiTransactionRequest.getUserAddress());
        TransactionReceipt transactionReceipt;
        if (!isUserExists ) {
            return Response.builder()
                    .responseCode(ResponseUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(ResponseUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .data(null)
                    .build();
        }
        String walletAddress = defiTransactionRequest.getUserAddress();

        DSCEngine transactions = blockchainService.loadContract();
        DefiUser user = defiUserRepository.findByWalletAddress(defiTransactionRequest.getUserAddress());
        Boolean isValidToken = transactions.isValidToken(walletAddress).send();


        if(ResponseUtils.isValidAddress(walletAddress) && isValidToken) {
            transactionReceipt = transactions.redeemCollateral(defiTransactionRequest.getTokenCollateralAddress(),defiTransactionRequest.getAmount().toBigInteger(),defiTransactionRequest.getUserAddress()).send();
            if(!transactionReceipt.getTransactionHash().isEmpty()) {


                DefiTransaction transaction = DefiTransaction.builder()
                        .transactionHash(transactionReceipt.getTransactionHash())
                        .transactionType("Redeem Collateral")
                        .amount(defiTransactionRequest.getAmount())
                        .tokenCollateralAddress(defiTransactionRequest.getTokenCollateralAddress())
                        .userAddress(defiTransactionRequest.getUserAddress())

                        .build();
                defiTransactionRepository.save(transaction);
            }
        }
        else
        {
            throw new IllegalArgumentException("Invalid User or Insufficient Account Balance");
        }

        blockchainEventServiceImplementation.listenTokenRedeemEvent();


        EmailDetails CollateralRedeemAlert = EmailDetails.builder()
                .subject("DANYAL BANK BLOCKCHAIN COLLATERAL REDEEM ALERT")
                .recipient(user.getEmail())
                .messageBody("Token Collateral of " + defiTransactionRequest.getAmount()  +
                        "has been transferred to your wallet address")
                .build();
        EmailService.sendSimpleEmail(CollateralRedeemAlert);

        return Response.builder()
                .responseCode(ResponseUtils.COLLATERAL_REDEEMED_SUCCESSFULLY)
                .responseMessage(ResponseUtils.COLLATERAL_REDEEM_SUCCESSFUL_MESSAGE)
                .tokenData(TokenData.builder()
                        .transactionHash(transactionReceipt.getTransactionHash())
                        .tokenCollateralAddress(defiTransactionRequest.getTokenCollateralAddress())
                        .amount(defiTransactionRequest.getAmount())
                        .userAddress(defiTransactionRequest.getUserAddress())
                        .build())
                .build();


    }

    @Override
    public Response burnDsc(DefiTransactionRequest defiTransactionRequest) throws Exception {
        boolean isUserExists = defiUserRepository.existsByWalletAddress(defiTransactionRequest.getUserAddress());
        TransactionReceipt transactionReceipt;
        if (!isUserExists ) {
            return Response.builder()
                    .responseCode(ResponseUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(ResponseUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .data(null)
                    .build();
        }
        String walletAddress = defiTransactionRequest.getUserAddress();

        DSCEngine transactions = blockchainService.loadContract();
        DefiUser user = defiUserRepository.findByWalletAddress(defiTransactionRequest.getUserAddress());
        Boolean isValidToken = transactions.isValidToken(walletAddress).send();


        if(ResponseUtils.isValidAddress(walletAddress) && isValidToken) {
            transactionReceipt = transactions.burnDsc(defiTransactionRequest.getUserAddress(),defiTransactionRequest.getAmount().toBigInteger()).send();
            if(!transactionReceipt.getTransactionHash().isEmpty()) {


                DefiTransaction transaction = DefiTransaction.builder()
                        .transactionHash(transactionReceipt.getTransactionHash())
                        .transactionType("Burn Dsc")
                        .amount(defiTransactionRequest.getAmount())
                        .userAddress(defiTransactionRequest.getUserAddress())
                        .build();
                defiTransactionRepository.save(transaction);
            }
        }
        else
        {
            throw new IllegalArgumentException("Invalid User or Insufficient Account Balance");
        }

blockchainEventServiceImplementation.listenTokenBurnEvent();

        EmailDetails tokenBurnAlert = EmailDetails.builder()
                .subject("DANYAL BANK BLOCKCHAIN DSC ALERT")
                .recipient(user.getEmail())
                .messageBody("Token amount of " + defiTransactionRequest.getAmount()  +
                        "has been burnt from your wallet address")
                .build();
        EmailService.sendSimpleEmail(tokenBurnAlert);

        return Response.builder()
                .responseCode(ResponseUtils.TOKEN_BURN_SUCCESSFUL_CODE)
                .responseMessage(ResponseUtils.TOKEN_BURN_SUCCESSFUL_MESSAGE)
                .tokenData(TokenData.builder()
                        .transactionHash(transactionReceipt.getTransactionHash())
                        .amount(defiTransactionRequest.getAmount())
                        .userAddress(defiTransactionRequest.getUserAddress())
                        .build())
                .build();

    }

    @Override
    public Response liquidateUser(DefiTransactionRequest defiTransactionRequest) throws Exception {
        boolean isUserExists = defiUserRepository.existsByWalletAddress(defiTransactionRequest.getUserAddress());
        TransactionReceipt transactionReceipt;
        if (!isUserExists ) {
            return Response.builder()
                    .responseCode(ResponseUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(ResponseUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .data(null)
                    .build();
        }
        String walletAddress = defiTransactionRequest.getUserAddress();

        DSCEngine transactions = blockchainService.loadContract();
        DefiUser underCollateralizedUser = defiUserRepository.findByWalletAddress(defiTransactionRequest.getUserAddress());
        DefiUser liquidator= defiUserRepository.findByWalletAddress(defiTransactionRequest.getLiquidator());
        Boolean isValidToken = transactions.isValidToken(walletAddress).send();




        if(ResponseUtils.isValidAddress(walletAddress) && isValidToken) {
            transactionReceipt = transactions.liquidate(defiTransactionRequest.getTokenCollateralAddress(),
                    defiTransactionRequest.getUserAddress(),defiTransactionRequest.getLiquidator(),defiTransactionRequest.getDebtToCover().toBigInteger()).send();
            if(!transactionReceipt.getTransactionHash().isEmpty()) {


                DefiTransaction transaction = DefiTransaction.builder()
                        .transactionHash(transactionReceipt.getTransactionHash())
                        .transactionType("Liquidation")
                        .debtToCover(defiTransactionRequest.getDebtToCover())
                        .liquidator(defiTransactionRequest.getLiquidator())
                        .userAddress(defiTransactionRequest.getUserAddress())
                        .build();
                defiTransactionRepository.save(transaction);
            }
        }
        else
        {
            throw new IllegalArgumentException("Invalid User or Insufficient Account Balance");
        }

        blockchainEventServiceImplementation.listenToLiquidateEvent();

        EmailDetails LiquidatorLiquidationAlert = EmailDetails.builder()
                .subject(" LIQUIDATION  ALERT")
                .recipient(liquidator.getEmail())
                .messageBody("You have liquidated an amount of " + defiTransactionRequest.getDebtToCover()  +
                        "from " + defiTransactionRequest.getUserAddress())
                .build();
        EmailService.sendSimpleEmail(LiquidatorLiquidationAlert);

        EmailDetails UnderCollateralizedLiquidationAlert = EmailDetails.builder()
                .subject(" LIQUIDATION  ALERT")
                .recipient(underCollateralizedUser.getEmail())
                .messageBody("You have been liquidated by an amount of " + defiTransactionRequest.getDebtToCover()  +
                        "by " + defiTransactionRequest.getLiquidator())
                .build();
        EmailService.sendSimpleEmail(UnderCollateralizedLiquidationAlert);

        return Response.builder()
                .responseCode(ResponseUtils.LIQUIDATION_SUCCESSFULL_CODE)
                .responseMessage(ResponseUtils.LIQUIDATION_SUCCESSFULL_MESSAGE)
                .tokenData(TokenData.builder()
                        .transactionHash(transactionReceipt.getTransactionHash())
                        .liquidator(defiTransactionRequest.getLiquidator())
                        .userAddress(defiTransactionRequest.getUserAddress())
                        .debtToCover(defiTransactionRequest.getDebtToCover())
                        .build())
                .build();
    }

    @Override
    public Response depositCollateral(DefiTransactionRequest defiTransactionRequest) throws Exception {

        boolean isUserExists = defiUserRepository.existsByWalletAddress(defiTransactionRequest.getUserAddress());
        TransactionReceipt transactionReceipt;
        if (!isUserExists ) {
            return Response.builder()
                    .responseCode(ResponseUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(ResponseUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .data(null)
                    .build();
        }
        String walletAddress = defiTransactionRequest.getUserAddress();

        DSCEngine transactions = blockchainService.loadContract();
        DefiUser user = defiUserRepository.findByWalletAddress(defiTransactionRequest.getUserAddress());
        Boolean isValidToken = transactions.isValidToken(walletAddress).send();


        if(ResponseUtils.isValidAddress(walletAddress) && isValidToken) {
            transactionReceipt = transactions.depositCollateral(defiTransactionRequest.getTokenCollateralAddress(),
                    defiTransactionRequest.getUserAddress(),defiTransactionRequest.getAmount().toBigInteger()).send();
            if(!transactionReceipt.getTransactionHash().isEmpty()) {


                DefiTransaction transaction = DefiTransaction.builder()
                        .transactionHash(transactionReceipt.getTransactionHash())
                        .transactionType("Collateral Deposit")
                        .tokenCollateralAddress(defiTransactionRequest.getTokenCollateralAddress())
                        .userAddress(defiTransactionRequest.getUserAddress())
                        .amount(defiTransactionRequest.getAmount())
                        .build();
                defiTransactionRepository.save(transaction);
            }
        }
        else
        {
            throw new IllegalArgumentException("Invalid User or Insufficient Account Balance");
        }

blockchainEventServiceImplementation.listenTokenDepositEvent();

        EmailDetails tokenBurnAlert = EmailDetails.builder()
                .subject("COLLATERAL DEPOSIT ALERT")
                .recipient(user.getEmail())
                .messageBody("Collateral amount of " + defiTransactionRequest.getAmount()  +
                        "has been transferred from your wallet address")
                .build();
        EmailService.sendSimpleEmail(tokenBurnAlert);

        return Response.builder()
                .responseCode(ResponseUtils.COLLATERAL_DEPOSIT_SUCCESSFUL_CODE)
                .responseMessage(ResponseUtils.COLLATERAL_DEPOSIT_SUCCESSFUL_MESSAGE)
                .tokenData(TokenData.builder()
                        .transactionHash(transactionReceipt.getTransactionHash())
                        .amount(defiTransactionRequest.getAmount())
                        .userAddress(defiTransactionRequest.getUserAddress())
                        .tokenCollateralAddress(defiTransactionRequest.getTokenCollateralAddress())
                        .build())
                .build();

    }

    @Override
    public Response mintDsc(DefiTransactionRequest defiTransactionRequest) throws Exception {
        boolean isUserExists = defiUserRepository.existsByWalletAddress(defiTransactionRequest.getUserAddress());
        TransactionReceipt transactionReceipt;
        if (!isUserExists ) {
            return Response.builder()
                    .responseCode(ResponseUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(ResponseUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .data(null)
                    .build();
        }
        String walletAddress = defiTransactionRequest.getUserAddress();

        DSCEngine transactions = blockchainService.loadContract();
        DefiUser user = defiUserRepository.findByWalletAddress(defiTransactionRequest.getUserAddress());
        Boolean isValidToken = transactions.isValidToken(walletAddress).send();



        if(ResponseUtils.isValidAddress(walletAddress) && isValidToken) {
            transactionReceipt = transactions.mintDsc(defiTransactionRequest.getUserAddress(),defiTransactionRequest.getAmount().toBigInteger()).send();
            if(!transactionReceipt.getTransactionHash().isEmpty()) {


                DefiTransaction transaction = DefiTransaction.builder()
                        .transactionHash(transactionReceipt.getTransactionHash())
                        .transactionType("Mint Dsc")
                        .amount(defiTransactionRequest.getAmount())
                        .userAddress(defiTransactionRequest.getUserAddress())
                        .build();
                defiTransactionRepository.save(transaction);
            }
        }
        else
        {
            throw new IllegalArgumentException("Invalid User or Insufficient Account Balance");
        }

        blockchainEventServiceImplementation.listenTokenMintEvent();


        EmailDetails tokenMintAlert = EmailDetails.builder()
                .subject(" DSC MINT ALERT")
                .recipient(user.getEmail())
                .messageBody("Token amount of " + defiTransactionRequest.getAmount()  +
                        "has been minted to your wallet address")
                .build();
        EmailService.sendSimpleEmail(tokenMintAlert);

        return Response.builder()
                .responseCode(ResponseUtils.TOKEN_MINT_SUCCESSFUL_CODE)
                .responseMessage(ResponseUtils.TOKEN_MINT_SUCCESSFUL_MESSAGE)
                .tokenData(TokenData.builder()
                        .transactionHash(transactionReceipt.getTransactionHash())
                        .amount(defiTransactionRequest.getAmount())
                        .userAddress(defiTransactionRequest.getUserAddress())
                        .build())
                .build();

    }

    @Override
    public Response convertDscToCash(DefiTransactionRequest defiTransactionRequest) throws Exception {
        boolean isUserExists = defiUserRepository.existsByWalletAddress(defiTransactionRequest.getUserAddress());
        TransactionReceipt transactionReceipt;
        if (!isUserExists ) {
            return Response.builder()
                    .responseCode(ResponseUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(ResponseUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .data(null)
                    .build();
        }
        String walletAddress = defiTransactionRequest.getUserAddress();

        DSCEngine transactions = blockchainService.loadContract();
        DefiUser user = defiUserRepository.findByWalletAddress(defiTransactionRequest.getUserAddress());
        Boolean isValidToken = transactions.isValidToken(walletAddress).send();



        if(ResponseUtils.isValidAddress(walletAddress) && isValidToken) {
            transactionReceipt = transactions.transferCollateralToBankAddressAndBurnEquivalentDSc
                    (defiTransactionRequest.getUserAddress(),defiTransactionRequest.getBankAddress(),defiTransactionRequest.getAmount().toBigInteger(),
                           defiTransactionRequest.getTokenCollateralAddress() ).send();
            if(!transactionReceipt.getTransactionHash().isEmpty()) {


                DefiTransaction transaction = DefiTransaction.builder()
                        .transactionHash(transactionReceipt.getTransactionHash())
                        .transactionType("Token Cash Conversion")
                        .userAddress(defiTransactionRequest.getUserAddress())
                        .tokenCollateralAddress(defiTransactionRequest.getTokenCollateralAddress())
                        .amount(defiTransactionRequest.getAmount())
                        .build();
                defiTransactionRepository.save(transaction);
            }
        }
        else
        {
            throw new IllegalArgumentException("Invalid User or Insufficient Account Balance");
        }
blockchainEventServiceImplementation.listenCollateralTransferToBankEvent();

        EmailDetails tokenToCashConversionAlert = EmailDetails.builder()
                .subject("Token to Cash Conversion ALERT")
                .recipient(user.getEmail())
                .messageBody("Token amount of " + defiTransactionRequest.getAmount()  +
                        "has been converted to cash and an equivalent amount is burnt from your wallet address")
                .build();
        EmailService.sendSimpleEmail(tokenToCashConversionAlert);

        return Response.builder()
                .responseCode(ResponseUtils.TOKEN_CONVERSION_SUCCESSFUL_CODE)
                .responseMessage(ResponseUtils.TOKEN_CONVERSION_SUCCESSFUL_MESSAGE)
                .tokenData(TokenData.builder()
                        .transactionHash(transactionReceipt.getTransactionHash())
                        .amount(defiTransactionRequest.getAmount())
                        .userAddress(defiTransactionRequest.getUserAddress())
                        .tokenCollateralAddress(defiTransactionRequest.getTokenCollateralAddress())
                        .build())
                .build();


    }

    @Override
    public Tuple2<String,BigInteger> getUnderCollateralizedUsersAndTheirDebt() throws Exception {
        DSCEngine transactions = blockchainService.loadContract();
        return transactions.getUnderCollateralizedUsersAndTheirDebt().send();
    }
}
