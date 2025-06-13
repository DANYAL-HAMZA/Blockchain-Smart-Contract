package com.example.BankApplication.service;

import com.example.BankApplication.dto.*;
import org.example.Entity.Response;
import org.example.dto.UserRequest;

import java.util.List;

public interface UserService {
    Response registerUser(UserRequest userRequest);
    Response updateUser(UserRequest userRequest);
    List<Response> allUsers();
    Response fetchUser(Long id);
    Response balanceEnquiry(String accountNumber);
    Response nameEnquiry(String accountNumber);
    Response credit(TransactionRequest transactionRequest);
    Response debit(TransactionRequest transactionRequest);
    Response transfer(TransferRequest request);

}
