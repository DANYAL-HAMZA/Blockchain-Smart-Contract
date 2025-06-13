package org.example.Entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@Builder
public class TokenData {
    private String accountNumber;
    private String accountName;
    private BigDecimal tokenBalance;
    private BigDecimal accountBalance;
    private String tokenCollateralAddress;
    private BigDecimal amount;
    private String userAddress;
    private String liquidator;
    private BigDecimal debtToCover;
    private String bankAddress;
    private String transactionHash;
}
