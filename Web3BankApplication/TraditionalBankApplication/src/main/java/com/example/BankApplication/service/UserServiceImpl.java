package com.example.BankApplication.service;

import com.example.BankApplication.dto.*;
import org.example.Entity.Response;
import org.example.dto.Data;
import org.example.dto.EmailDetails;
import org.example.Service.EmailService;
import org.example.Entity.User;
import org.example.Repository.UserRepository;
import org.example.utils.ResponseUtils;
import org.example.dto.UserRequest;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TransactionService transactionService;
    private final EmailService emailService;


    public UserServiceImpl(UserRepository userRepository, TransactionService transactionService, EmailService emailService) {
        this.userRepository = userRepository;
        this.transactionService = transactionService;
        this.emailService = emailService;
    }

    @Override
    public Response registerUser(UserRequest userRequest) {


        boolean isEmailExist = userRepository.existsByEmail(userRequest.getEmail());
        if (isEmailExist) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_EXISTS_CODE)
                    .responseMessage(ResponseUtils.USER_EXISTS_MESSAGE)
                    .data(null)
                    .build();

        }

        User user = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(ResponseUtils.generateAccountNumber(ResponseUtils.LENGTH_OF_ACCOUNT_NUMBER))
                .accountBalance(userRequest.getAccountBalance())
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .dateOfBirth(userRequest.getDateOfBirth())
                .build();


        User savedUser = userRepository.save(user);

        String accountDetails = savedUser.getFirstName() + savedUser.getLastName()
                + savedUser.getOtherName() + "\nAccount Number: " + savedUser.getAccountNumber();
        //Send email alert
        EmailDetails message = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT DETAILS")
                .messageBody("Congratulations! Your account has been successfully created! Kindly find your details below: \n" + accountDetails)
                .build();

        emailService.sendSimpleEmail(message);

        return Response.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.USER_REGISTERED_SUCCESS)
                .data(Data.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName())
                        .build())
                .build();

    }

    @Override
    public List<Response> allUsers() {
        List<User> usersList = userRepository.findAll();

        return usersList.stream().map(user -> Response.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .data(Data.builder()
                        .accountBalance(user.getAccountBalance())
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .accountNumber(user.getAccountNumber())
                        .build())
                .build()).collect(Collectors.toList());


    }

    @Override
    public Response fetchUser(Long userId) {

        if (!userRepository.existsById(userId)) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
        User user = userRepository.findById(userId).get();

        return Response.builder()
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .responseCode(ResponseUtils.SUCCESS)
                .data(Data.builder()
                        .accountNumber(user.getAccountNumber())
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .accountBalance(user.getAccountBalance())
                        .build())
                .build();

    }

    @Override
    public Response balanceEnquiry(String accountNumber) {

        boolean isAccountExist = userRepository.existsByAccountNumber(accountNumber);
        if (!isAccountExist) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }

        User user = userRepository.findByAccountNumber(accountNumber);
        return Response.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .data(Data.builder()
                        .accountBalance(user.getAccountBalance())
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .accountNumber(accountNumber)
                        .build())
                .build();
    }

    @Override
    public Response nameEnquiry(String accountNumber) {
        boolean isAccountExist = userRepository.existsByAccountNumber(accountNumber);
        if (!isAccountExist) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
        User user = userRepository.findByAccountNumber(accountNumber);
        return Response.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .data(Data.builder()
                        .accountBalance(null)
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .accountNumber(accountNumber)
                        .build())
                .build();
    }

    @Override
    public Response credit(TransactionRequest transactionRequest) {

        User receivingUser = userRepository.findByAccountNumber(transactionRequest.getAccountNumber());
        if (!userRepository.existsByAccountNumber(transactionRequest.getAccountNumber())) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }

        receivingUser.getAccountBalance().add(transactionRequest.getAmount());
        userRepository.save(receivingUser);

        EmailDetails creditAlert = EmailDetails.builder()
                .subject("CREDIT ALERT")
                .recipient(receivingUser.getEmail())
                .messageBody("The sum of " + transactionRequest.getAmount() + " has been credited to your account!" +
                        " your current balance is " + receivingUser.getAccountBalance())
                .build();
        emailService.sendSimpleEmail(creditAlert);

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionType("Credit");
        transactionDto.setAccountNumber(transactionRequest.getAccountNumber());
        transactionDto.setAmount(transactionRequest.getAmount());

        transactionService.saveTransaction(transactionDto);


        return Response.builder()
                .responseCode(ResponseUtils.SUCCESSFUL_TRANSACTION)
                .responseMessage(ResponseUtils.ACCOUNT_CREDITED)
                .data(Data.builder()
                        .accountNumber(transactionRequest.getAccountNumber())
                        .accountBalance(receivingUser.getAccountBalance())
                        .accountName(receivingUser.getFirstName() + " " + receivingUser.getLastName() + receivingUser.getOtherName())
                        .build())
                .build();
    }

    @Override
    public Response debit(TransactionRequest transactionRequest) {
        //check if the account exists
        //check if the amount you intend to withdraw is not more than the current account balance
        boolean isAccountExist = userRepository.existsByAccountNumber(transactionRequest.getAccountNumber());
        if (!isAccountExist) {
            return Response.builder()
                    .responseCode(ResponseUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(ResponseUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .data(null)
                    .build();
        }

        User userToDebit = userRepository.findByAccountNumber(transactionRequest.getAccountNumber());
        BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
        BigInteger debitAmount = transactionRequest.getAmount().toBigInteger();
        if (availableBalance.intValue() < debitAmount.intValue()) {
            return Response.builder()
                    .responseCode(ResponseUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(ResponseUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .data(null)
                    .build();
        } else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(transactionRequest.getAmount()));
            userRepository.save(userToDebit);

            EmailDetails debitAlert = EmailDetails.builder()
                    .subject("DEBIT ALERT")
                    .recipient(userToDebit.getEmail())
                    .messageBody("The sum of " + transactionRequest.getAmount() + " has been deducted from your account!" +
                            " your current balance is " + userToDebit.getAccountBalance())
                    .build();
            emailService.sendSimpleEmail(debitAlert);

            TransactionDto transactionDto = new TransactionDto();
            transactionDto.setTransactionType("debit");
            transactionDto.setAccountNumber(transactionRequest.getAccountNumber());
            transactionDto.setAmount(transactionRequest.getAmount());

            transactionService.saveTransaction(transactionDto);

            return Response.builder()
                    .responseCode(ResponseUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
                    .responseMessage(ResponseUtils.ACCOUNT_DEBITED_MESSAGE)
                    .data(Data.builder()
                            .accountNumber(transactionRequest.getAccountNumber())
                            .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName() + " " + userToDebit.getOtherName())
                            .accountBalance(userToDebit.getAccountBalance())
                            .build())
                    .build();
        }

    }



    @Override
    public Response transfer(TransferRequest request) {
        boolean isSourceAccountExists = userRepository.existsByAccountNumber(request.getSourceAccountNumber());
        boolean isDestinationAccountExists = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());

        if (!isDestinationAccountExists || !isSourceAccountExists) {
            return Response.builder()
                    .responseCode(ResponseUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(ResponseUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .data(null)
                    .build();
        }

        User sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());
        if (request.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0) {
            return Response.builder()
                    .responseCode(ResponseUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(ResponseUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .data(null)
                    .build();
        }
        sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
        userRepository.save(sourceAccountUser);
        EmailDetails debitAlert = EmailDetails.builder()
                .subject("DEBIT ALERT")
                .recipient(sourceAccountUser.getEmail())
                .messageBody("The sum of " + request.getAmount() + " has been deducted from your account!" +
                        " your current balance is " + sourceAccountUser.getAccountBalance())
                .build();

        emailService.sendSimpleEmail(debitAlert);
        User destinationAccountUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
        userRepository.save(destinationAccountUser);

        EmailDetails creditAlert = EmailDetails.builder()
                .subject("CREDIT ALERT")
                .recipient(destinationAccountUser.getEmail())
                .messageBody("The sum of " + request.getAmount() + " has been added to your account!" +
                        " your current balance is " + destinationAccountUser.getAccountBalance())

                .build();


        emailService.sendSimpleEmail(creditAlert);

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionType("debit");
        transactionDto.setAccountNumber(request.getDestinationAccountNumber());
        transactionDto.setAmount(request.getAmount());

        transactionService.saveTransaction(transactionDto);


        return Response.builder()
                .responseCode(ResponseUtils.TRANSFER_SUCCESSFUL_CODE)
                .responseMessage(ResponseUtils.TRANSFER_SUCCESSFUL_MESSAGE)
                .data(null)
                .build();


    }

    @Override
    public Response updateUser(UserRequest userRequest) {
            boolean isAccountExist = userRepository.existsByAccountNumber(userRequest.getAccountNumber());
            if (!isAccountExist) {
                return Response.builder()
                        .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                        .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                        .data(null)
                        .build();

            }
            User user = userRepository.findByAccountNumber(userRequest.getAccountNumber());
            user.setAddress(userRequest.getAddress());
            user.setEmail(userRequest.getEmail());
            user.setGender(userRequest.getGender());
            user.setAlternativePhoneNumber(userRequest.getAlternativePhoneNumber());
            user.setDateOfBirth(userRequest.getDateOfBirth());
            user.setFirstName(userRequest.getFirstName());
            user.setLastName(userRequest.getLastName());
            user.setOtherName(userRequest.getOtherName());
            user.setStateOfOrigin(userRequest.getStateOfOrigin());
            user.setStatus(userRequest.getStatus());

            User savedUser = userRepository.save(user);

            String accountDetails = savedUser.getFirstName() + savedUser.getLastName()
                    + savedUser.getOtherName() + "\nAccount Number: " + savedUser.getAccountNumber();
            //Send email alert
            EmailDetails message = EmailDetails.builder()
                    .recipient(savedUser.getEmail())
                    .subject("ACCOUNT DETAILS")
                    .messageBody("Congratulations! Your account has been successfully updated! Kindly find your details below: \n" + accountDetails)
                    .build();

            emailService.sendSimpleEmail(message);

            return Response.builder()
                    .responseCode(ResponseUtils.UPDATE_SUCCESSFUL_CODE)
                    .responseMessage(ResponseUtils.UPDATE_SUCCESSFUL_MESSAGE)
                    .data(Data.builder()
                            .accountBalance(savedUser.getAccountBalance())
                            .accountNumber(savedUser.getAccountNumber())
                            .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName())
                            .build())
                    .build();
        }


    }
