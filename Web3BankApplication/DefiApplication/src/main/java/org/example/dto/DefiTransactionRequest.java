package org.example.dto;

import jakarta.persistence.Entity;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DefiTransactionRequest {
    private String transactionType;
    private String tokenCollateralAddress;
    private BigDecimal amount;
    private String userAddress;
    private String liquidator;
    private BigDecimal debtToCover;
    private String bankAddress;
}
