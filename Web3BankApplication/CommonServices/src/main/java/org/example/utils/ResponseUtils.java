package org.example.utils;

import org.example.dto.BlockchainTransactionRequest;
import org.example.Entity.User;
import org.example.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;


public class ResponseUtils {
    @Autowired
    private static UserRepository userRepository;
    private static BlockchainTransactionRequest blockchainTransactionRequest;



    //handling custom responses
    public static final String USER_EXISTS_CODE = "001";
    public static final String USER_EXISTS_MESSAGE = "User with provided email already exists!";
    public static final String SUCCESS = "002";
    public static final String USER_REGISTERED_SUCCESS = "User has been successfully registered!";
    public static final String SUCCESS_MESSAGE = "Successfully Done!";
    public static final String USER_NOT_FOUND_MESSAGE = "This User Doesn't Exist!";
    public static final String USER_NOT_FOUND_CODE = "003";
    public static final String SUCCESSFUL_TRANSACTION = "004";
    public static final String ACCOUNT_CREDITED = "Account has been credited";

    public static final int LENGTH_OF_ACCOUNT_NUMBER = 10;
    public static final String ACCOUNT_NOT_EXIST_CODE = "005";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE = "Account does not exist";
    public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient balance";
    public static final String INSUFFICIENT_BALANCE_CODE = "006";

    public static final String ACCOUNT_DEBITED_MESSAGE = "Account debited successfully";
    public static final String ACCOUNT_DEBITED_SUCCESS_CODE = "007";
    public static final String TRANSFER_SUCCESSFUL_MESSAGE = "transfer was successfully";
    public static final String TRANSFER_SUCCESSFUL_CODE = "008";
    public static final String TOKEN_TRANSFER_SUCCESSFUL_MESSAGE = "Token transfer was successfully";
    public static final String TOKEN_TRANSFER_SUCCESSFUL_CODE = "009";
    public static final String TOKEN_MINT_SUCCESSFUL_MESSAGE = "Token mint was successfully";
    public static final String TOKEN_MINT_SUCCESSFUL_CODE = "0010";
    public static final String TOKEN_BURN_SUCCESSFUL_MESSAGE = "Token burn was successfully";
    public static final String TOKEN_BURN_SUCCESSFUL_CODE = "0011";
    public static final String UPDATE_SUCCESSFUL_MESSAGE = "User information successfully updated";
    public static final String UPDATE_SUCCESSFUL_CODE = "0012";
    public static final String COLLATERAL_REDEEM_SUCCESSFUL_MESSAGE = "Collateral is successfully redeemed";
    public static final String COLLATERAL_REDEEMED_SUCCESSFULLY = "0013";
    public static final String LIQUIDATION_SUCCESSFULL_MESSAGE = "Liquidation Successfull";
    public static final String LIQUIDATION_SUCCESSFULL_CODE = "0014";
    public static final String COLLATERAL_DEPOSIT_SUCCESSFUL_MESSAGE = " Collateral Deposit Successfull";
    public static final String COLLATERAL_DEPOSIT_SUCCESSFUL_CODE = " 0015";
    public static final String TOKEN_CONVERSION_SUCCESSFUL_MESSAGE = " Token Conversion Successfull";
    public static final String TOKEN_CONVERSION_SUCCESSFUL_CODE = " 0016";






    public static String generateAccountNumber(int len) {
        String accountNumber = "";
        int x;
        char[] stringChars = new char[len];

        for (int i = 0; i < len; i++) //4
        {
            Random random = new Random();
            x = random.nextInt(9);
            stringChars[i] = Integer.toString(x).toCharArray()[0];
        }
        accountNumber = new String(stringChars);
        return accountNumber.trim();

    }

    /*public static BigDecimal calculateTokenPerCashSpecified(BigDecimal amount) {
        final BigDecimal TOKEN_CONSTANT = BigDecimal.valueOf(10000);
        return amount.divide(TOKEN_CONSTANT);

    }*/

    /*public static BigDecimal calculateCashPerTokenAmountSpecified(BigDecimal tokenAmount) {
        final BigDecimal TOKEN_CONSTANT = BigDecimal.valueOf(10000);
        return tokenAmount.multiply(TOKEN_CONSTANT);

    }*/

    public static Boolean isValidAddress(String address) {
        if (address == null || address.length() != 42 || !address.startsWith("ox")) {
            return false;
        }
        try {
            new BigInteger(address.substring(2), 16);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isSufficientAmount(BigDecimal amount) {
        User user = userRepository.findByAccountNumber(blockchainTransactionRequest.getSenderAccountNumber());
        if (user.getAccountBalance().compareTo(amount) >= 0) {
            return true;
        }

        return false;
    }


}