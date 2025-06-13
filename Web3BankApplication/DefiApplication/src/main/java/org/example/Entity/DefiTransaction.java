package org.example.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class DefiTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String transactionId;
    private String transactionType;
    private String tokenCollateralAddress;
    private BigDecimal amount;
    private String userAddress;
    private String liquidator;
    private BigDecimal debtToCover;
    private String bankAddress;
    private String transactionHash;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
