package org.example.Service;

import org.example.Entity.DefiUser;
import org.example.Entity.Response;
import org.example.Entity.User;
import org.example.Repository.DefiUserRepository;
import org.example.dto.Data;
import org.example.dto.DefiUserRequest;
import org.example.dto.EmailDetails;
import org.example.dto.UserRequest;
import org.example.utils.ResponseUtils;

import java.util.List;
import java.util.stream.Collectors;

public class DefiUserServiceImplementation implements DefiUserService{
    private DefiUserRepository defiUserRepository;
    @Override
    public Response registerUser(DefiUserRequest userRequest) {
        boolean isEmailExist = defiUserRepository.existsByEmail(userRequest.getEmail());
        if (isEmailExist) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_EXISTS_CODE)
                    .responseMessage(ResponseUtils.USER_EXISTS_MESSAGE)
                    .data(null)
                    .build();

        }

        DefiUser user = DefiUser.builder()
                .userName(userRequest.getUserName())
                .passWord(userRequest.getPassword())
                .email(userRequest.getEmail())
                .walletAddress(userRequest.getWalletAddress())
                .build();


        DefiUser savedUser = defiUserRepository.save(user);

        String accountDetails = savedUser.getUserName() + savedUser.getWalletAddress();
        //Send email alert
        EmailDetails message = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT DETAILS")
                .messageBody("Congratulations! Your account has been successfully created! Kindly find your details below: \n" + accountDetails)
                .build();

        EmailService.sendSimpleEmail(message);

        return Response.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.USER_REGISTERED_SUCCESS)
                .data(Data.builder()
                        .accountName(savedUser.getUserName())
                        .walletAddress(savedUser.getWalletAddress())
                        .build())
                .build();

    }

    @Override
    public Response updateUser(DefiUserRequest userRequest) {
        boolean isAccountExist = defiUserRepository.existsByWalletAddress(userRequest.getWalletAddress());
        if (!isAccountExist) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();

        }
        DefiUser user = defiUserRepository.findByWalletAddress(userRequest.getWalletAddress());
        user.setEmail(userRequest.getEmail());
        user.setUserName(userRequest.getUserName());
        user.setWalletAddress(userRequest.getWalletAddress());
        user.setPassWord(userRequest.getPassword());
        DefiUser savedUser = defiUserRepository.save(user);

        String accountDetails = savedUser.getUserName() + savedUser.getWalletAddress();
        //Send email alert
        EmailDetails message = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT DETAILS")
                .messageBody("Congratulations! Your account has been successfully updated! Kindly find your details below: \n" + accountDetails)
                .build();

        EmailService.sendSimpleEmail(message);

        return Response.builder()
                .responseCode(ResponseUtils.UPDATE_SUCCESSFUL_CODE)
                .responseMessage(ResponseUtils.UPDATE_SUCCESSFUL_MESSAGE)
                .data(Data.builder()
                        .walletAddress(savedUser.getWalletAddress())
                        .accountName(savedUser.getUserName())
                        .build())

                .build();
    }

    @Override
    public List<Response> allUsers() {
        List<DefiUser> usersList = defiUserRepository.findAll();

        return usersList.stream().map(user -> Response.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .data(Data.builder()
                        .accountName(user.getUserName())
                        .walletAddress(user.getWalletAddress())
                        .build())
                .build()).collect(Collectors.toList());


    }

    @Override
    public Response fetchUser(Long id) {
        if (!defiUserRepository.existsById(id)) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
        DefiUser user = defiUserRepository.findById(id).get();

        return Response.builder()
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .responseCode(ResponseUtils.SUCCESS)
                .data(Data.builder()
                        .walletAddress(user.getWalletAddress())
                        .accountName(user.getUserName())
                        .build())
                .build();

    }
}
