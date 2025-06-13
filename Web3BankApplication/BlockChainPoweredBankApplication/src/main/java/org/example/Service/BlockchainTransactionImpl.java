package org.example.Service;

import org.example.Entity.Response;
import org.example.Entity.User;
import org.example.dto.BlockchainTransactionRequest;
import org.example.Entity.TokenData;
import org.example.email.dto.EmailDetails;
import org.example.email.service.EmailService;
import org.example.Entity.BlockchainTransaction;
import org.example.Repository.BlockchainTransactionRepository;
import org.example.Repository.UserRepository;
import org.example.utils.ResponseUtils;
import org.web3j.model.DSCEngine;
import org.web3j.protocol.core.methods.response.TransactionReceipt;


public class BlockchainTransactionImpl implements BlockchainTransactionService {
    private final BlockchainTransactionRepository blockchainTransactionRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final BlockchainService blockchainService;
    private final BlockchainEventService blockchainEventService;



    public BlockchainTransactionImpl(BlockchainTransactionRepository blockchainTransactionRepository, UserRepository userRepository, EmailService emailService, BlockchainService blockchainService, BlockchainEventService blockchainEventService) {
        this.blockchainTransactionRepository = blockchainTransactionRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.blockchainService = blockchainService;
        this.blockchainEventService = blockchainEventService;
    }

    @Override
    //convert cash to token
    public Response mintTokenToAddress(BlockchainTransactionRequest blockchainTransactionRequest) throws Exception {
        boolean isSourceAccountExists = userRepository.existsByAccountNumber(blockchainTransactionRequest.getSenderAccountNumber());

        if (!isSourceAccountExists ) {
            return Response.builder()
                    .responseCode(ResponseUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(ResponseUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .data(null)
                    .build();
        }
        User userToMint = userRepository.findByAccountNumber(blockchainTransactionRequest.getSenderAccountNumber());
        String walletAddress = userToMint.getWalletAddress();
        //BigDecimal tokenAmountToMint = ResponseUtils.calculateTokenPerCashSpecified(blockchainTransactionRequest.getAmount());

        DSCEngine transactions = blockchainService.loadContract();


        if(ResponseUtils.isValidAddress(walletAddress) && ResponseUtils.isSufficientAmount(blockchainTransactionRequest.getAmount())) {
           TransactionReceipt transactionReceipt = transactions.mintDsc(walletAddress, blockchainTransactionRequest.getAmount().toBigInteger()).send();
           if(!transactionReceipt.getTransactionHash().isEmpty()) {



// -fixes- we should also use the @Transactional annotation to ensure a safe transaction.


               userToMint.setAccountBalance(userToMint.getAccountBalance().subtract(blockchainTransactionRequest.getAmount()));
               userToMint.setTokenBalance(userToMint.getTokenBalance().add(blockchainTransactionRequest.getAmount()));
               userRepository.save(userToMint);

               BlockchainTransaction transaction = BlockchainTransaction.builder()
                       .amount(blockchainTransactionRequest.getAmount())
                       .tokenAmount(blockchainTransactionRequest.getAmount())
                       .transactionTitle("MINT")
                       .transactionHash(transactionReceipt.getTransactionHash())
                       .senderAccountNumber(blockchainTransactionRequest.getSenderAccountNumber())
                       .build();
               blockchainTransactionRepository.save(transaction);
           }
        }
        else
        {
            throw new IllegalArgumentException("Invalid Wallet Address or Insufficient Account Balance");
        }

        blockchainEventService.listenToMintEvent();

        EmailDetails tokenMintAlert = EmailDetails.builder()
                .subject("DANYAL BANK BLOCKCHAIN TOKEN MINT ALERT")
                .recipient(userToMint.getEmail())
                .messageBody("A token amount of " + blockchainTransactionRequest.getAmount() + "which isequivalent to " + blockchainTransactionRequest.getAmount() +
                        "has been minted to your wallet address")
                .build();
        emailService.sendSimpleEmail(tokenMintAlert);

        return Response.builder()
                .responseCode(ResponseUtils.TOKEN_MINT_SUCCESSFUL_CODE)
                .responseMessage(ResponseUtils.TOKEN_MINT_SUCCESSFUL_MESSAGE)
                .tokenData(TokenData.builder()
                        .tokenBalance(userToMint.getTokenBalance())
                        .accountName(userToMint.getFirstName() + userToMint.getLastName())
                        .accountNumber(userToMint.getAccountNumber())
                        .accountBalance(userToMint.getAccountBalance())
                        .build())
                .build();

    }

    @Override
    public Response transferTokensToNonAccountHolder(BlockchainTransactionRequest blockchainTransactionRequest) throws Exception {

        boolean isSourceAccountExists = userRepository.existsByAccountNumber(blockchainTransactionRequest.getSenderAccountNumber());

        if (!isSourceAccountExists ) {
            return Response.builder()
                    .responseCode(ResponseUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(ResponseUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .data(null)
                    .build();
        }

        User tokenSender = userRepository.findByAccountNumber(blockchainTransactionRequest.getSenderAccountNumber());
        String senderAddress = tokenSender.getWalletAddress();
        String receiverAddress = blockchainTransactionRequest.getNonAccountHolderWalletAddress();
        //BigDecimal amountToTransfer = ResponseUtils.calculateTokenPerCashSpecified(blockchainTransactionRequest.getAmount());
        //This is where we send the token here and also check if token transfer was successful before updating variables
        DSCEngine transactions = blockchainService.loadContract();

        if( transactions.isValidToken(senderAddress) && ResponseUtils.isValidAddress(senderAddress) && ResponseUtils.isValidAddress(receiverAddress) && ResponseUtils.isSufficientAmount(blockchainTransactionRequest.getAmount())) {
            TransactionReceipt transactionReceipt = transactions.transferDscFromUser1ToUser2(blockchainTransactionRequest.getAmount().toBigInteger(), senderAddress, receiverAddress).send();
            if (!transactionReceipt.getTransactionHash().isEmpty()) {


// -fixes- we should also use the @Transactional annotation to ensure a safe transaction.

                tokenSender.setTokenBalance(tokenSender.getTokenBalance().subtract(blockchainTransactionRequest.getAmount()));
                userRepository.save(tokenSender);

                BlockchainTransaction transaction = BlockchainTransaction.builder()
                        .isBankAccountHolder(false)
                        .amount(blockchainTransactionRequest.getAmount())
                        .transactionHash(transactionReceipt.getTransactionHash())
                        .transactionTitle("TOKEN TRANSFER TO A NON ACCOUNT HOLDER")
                        .senderAccountNumber(blockchainTransactionRequest.getSenderAccountNumber())
                        .receiverWalletAddress(blockchainTransactionRequest.getNonAccountHolderWalletAddress())
                        .build();
                blockchainTransactionRepository.save(transaction);

            }
        }
        else
        {
            throw new IllegalArgumentException("Invalid Wallet Address or Insufficient Account Balance");
        }


blockchainEventService.listenToTransferEvent();
        EmailDetails tokenDebitAlert = EmailDetails.builder()
                .subject("DANYAL BANK BLOCKCHAIN TOKEN DEBIT ALERT")
                .recipient(tokenSender.getEmail())
                .messageBody("A token amount equivalent to " + blockchainTransactionRequest.getAmount() +
                        "has been transferred from your wallet to"+ blockchainTransactionRequest.getNonAccountHolderWalletAddress())
                .build();
        emailService.sendSimpleEmail(tokenDebitAlert);

        EmailDetails tokenCreditAlert = EmailDetails.builder()
                .subject("DANYAL BANK BLOCKCHAIN TOKEN CREDIT ALERT")
                .recipient(blockchainTransactionRequest.getNonAccountHolderEmail())
                .messageBody("A token amount of" + blockchainTransactionRequest.getAmount() +
                        "has been transferred from" + tokenSender.getLastName() + tokenSender.getFirstName() +
                        " to your blockchain wallet address " + blockchainTransactionRequest.getNonAccountHolderWalletAddress()
                )
                .build();
        emailService.sendSimpleEmail(tokenCreditAlert);

        return Response.builder()
                .responseCode(ResponseUtils.TOKEN_TRANSFER_SUCCESSFUL_CODE)
                .responseMessage(ResponseUtils.TOKEN_TRANSFER_SUCCESSFUL_MESSAGE)
                .tokenData(TokenData.builder()
                        .tokenBalance(tokenSender.getTokenBalance())
                        .accountName(tokenSender.getFirstName() + tokenSender.getLastName())
                        .accountNumber(tokenSender.getAccountNumber())
                        .accountBalance(tokenSender.getAccountBalance())
                        .build())
                .build();

    }

    @Override
    public Response transferTokensToAccountHolder(BlockchainTransactionRequest blockchainTransactionRequest) throws Exception {
        boolean isSourceAccountExists = userRepository.existsByAccountNumber(blockchainTransactionRequest.getSenderAccountNumber());
        boolean isDestinationAccountExists = userRepository.existsByAccountNumber(blockchainTransactionRequest.getSenderAccountNumber());

        if (!isSourceAccountExists || !isDestinationAccountExists) {
            return Response.builder()
                    .responseCode(ResponseUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(ResponseUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .data(null)
                    .build();
        }

        User tokenSender = userRepository.findByAccountNumber(blockchainTransactionRequest.getSenderAccountNumber());
        User tokenReceiver = userRepository.findByAccountNumber(blockchainTransactionRequest.getReceiverAccountNumber());

        String senderAddress = tokenSender.getWalletAddress();
        String receiverAddress = tokenReceiver.getWalletAddress();
        //BigDecimal amountToTransfer = ResponseUtils.calculateTokenPerCashSpecified(blockchainTransactionRequest.getAmount());

        DSCEngine transactions = blockchainService.loadContract();

        //check if token transfer was successful before updating variables
        //we do not update account balance here because the account balance only decreases when tokens
        //are minted to a user's address(ie converting cash into token) and increases when tokens
        // are burnt from a user's address(ie converting tokens back to cash)
        if(transactions.isValidToken(senderAddress) && ResponseUtils.isValidAddress(senderAddress) && ResponseUtils.isValidAddress(receiverAddress) && ResponseUtils.isSufficientAmount(blockchainTransactionRequest.getAmount())) {
            TransactionReceipt transactionReceipt = transactions.transferDscFromUser1ToUser2(blockchainTransactionRequest.getAmount().toBigInteger(), senderAddress, receiverAddress).send();
if(!transactionReceipt.getTransactionHash().isEmpty()) {


    // -Fixes- Here we should also check for the success of blockchain transaction before updating states.
    // We can return and verify the transaction receipt to do so
// -fixes- we should also use the @Transactional annotation to ensure a safe transaction.

    tokenSender.setTokenBalance(tokenSender.getTokenBalance().subtract(blockchainTransactionRequest.getAmount()));
    tokenReceiver.setTokenBalance(tokenReceiver.getTokenBalance().add(blockchainTransactionRequest.getAmount()));
    userRepository.save(tokenSender);
    userRepository.save(tokenReceiver);

    BlockchainTransaction transaction = BlockchainTransaction.builder()
            .isBankAccountHolder(true)
            .amount(blockchainTransactionRequest.getAmount())
            .transactionTitle("TOKEN TRANSFER TO AN ACCOUNT HOLDER")
            .senderAccountNumber(blockchainTransactionRequest.getSenderAccountNumber())
            .receiverAccountNumber(blockchainTransactionRequest.getReceiverAccountNumber())
            .build();
    blockchainTransactionRepository.save(transaction);

}

        }
        else {
            throw new IllegalArgumentException("Invalid Address or Insufficient Account Balance");
        }
blockchainEventService.listenToTransferEvent();

        EmailDetails tokenDebitAlert = EmailDetails.builder()
                .subject("DANYAL BANK BLOCKCHAIN TOKEN DEBIT ALERT")
                .recipient(tokenSender.getEmail())
                .messageBody("A token amount equivalent to " + blockchainTransactionRequest.getAmount() +
                        "has been transfered from your wallet to"+ blockchainTransactionRequest.getReceiverAccountNumber())
                .build();
        emailService.sendSimpleEmail(tokenDebitAlert);

        EmailDetails tokenCreditAlert = EmailDetails.builder()
                .subject("DANYAL BANK BLOCKCHAIN TOKEN CREDIT ALERT")
                .recipient(tokenReceiver.getEmail())
                .messageBody("A token amount of" + blockchainTransactionRequest.getAmount() +
                        "has been transferred from" + tokenSender.getLastName() + tokenSender.getFirstName() +
                        " to your blockchain wallet address associated with your bank account number"
                        + blockchainTransactionRequest.getReceiverAccountNumber())
                        .build();
        emailService.sendSimpleEmail(tokenCreditAlert);

        return Response.builder()
                .responseCode(ResponseUtils.TOKEN_TRANSFER_SUCCESSFUL_CODE)
                .responseMessage(ResponseUtils.TOKEN_TRANSFER_SUCCESSFUL_MESSAGE)
                .tokenData(TokenData.builder()
                        .tokenBalance(tokenSender.getTokenBalance())
                        .accountName(tokenSender.getFirstName() + tokenSender.getLastName())
                        .accountNumber(tokenSender.getAccountNumber())
                        .accountBalance(tokenSender.getAccountBalance())
                        .build()
                )
                .build();
    }

// This function converts a specified token amount to cash and updates user bank properties
    @Override
    public Response convertTokenToCash(BlockchainTransactionRequest blockchainTransactionRequest) throws Exception {
        boolean isSourceAccountExists = userRepository.existsByAccountNumber(blockchainTransactionRequest.getSenderAccountNumber());

        if (!isSourceAccountExists) {
            return Response.builder()
                    .responseCode(ResponseUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(ResponseUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .data(null)
                    .build();
        }
        User userToConvert = userRepository.findByAccountNumber(blockchainTransactionRequest.getSenderAccountNumber());
        String userWalletAddress = userToConvert.getWalletAddress();
        //BigDecimal tokenAmountToBurn = ResponseUtils.calculateCashPerTokenAmountSpecified(blockchainTransactionRequest.getAmount());
        //This is where we burn the token from the user's address
        //we check for a success
        DSCEngine transactions = blockchainService.loadContract();

        if(transactions.isValidToken(userWalletAddress) && ResponseUtils.isValidAddress(userWalletAddress)) {
            TransactionReceipt transactionReceipt = transactions.burnDsc(userWalletAddress, blockchainTransactionRequest.getAmount().toBigInteger()).send();

            if (!transactionReceipt.getTransactionHash().isEmpty()) {
                userToConvert.setTokenBalance(userToConvert.getTokenBalance().subtract(blockchainTransactionRequest.getAmount()));
                userToConvert.setAccountBalance(userToConvert.getAccountBalance().add(blockchainTransactionRequest.getAmount()));
                userRepository.save(userToConvert);

                BlockchainTransaction transaction = BlockchainTransaction.builder()
                        .isBankAccountHolder(true)
                        .amount(blockchainTransactionRequest.getAmount())
                        .tokenAmount(blockchainTransactionRequest.getAmount())
                        .transactionTitle("TOKEN BURN")
                        .senderAccountNumber(blockchainTransactionRequest.getSenderAccountNumber())
                        .build();
                blockchainTransactionRepository.save(transaction);

            }
        }
// -fixes- we should also use the @Transactional annotation to ensure a safe transaction.
        else {
            throw new IllegalArgumentException("Invalid Address or Insufficient Account Balance");
        }

        blockchainEventService.listenToBurnEvent();

        EmailDetails tokenBurnAlert = EmailDetails.builder()
                .subject("DANYAL BANK BLOCKCHAIN TOKEN BURN ALERT")
                .recipient(userToConvert.getEmail())
                .messageBody("A token amount of " + blockchainTransactionRequest.getAmount() + " which is equivalent to " + blockchainTransactionRequest.getAmount() +
                        "has been burnt from your wallet ")
                .build();
        emailService.sendSimpleEmail(tokenBurnAlert);

        return Response.builder()
                .responseCode(ResponseUtils.TOKEN_BURN_SUCCESSFUL_CODE)
                .responseMessage(ResponseUtils.TOKEN_BURN_SUCCESSFUL_MESSAGE)
                .tokenData(TokenData.builder()
                        .tokenBalance(userToConvert.getTokenBalance())
                        .accountName(userToConvert.getFirstName() + userToConvert.getLastName())
                        .accountNumber(userToConvert.getAccountNumber())
                        .accountBalance(userToConvert.getAccountBalance())
                        .build())
                .build();
    }
}
