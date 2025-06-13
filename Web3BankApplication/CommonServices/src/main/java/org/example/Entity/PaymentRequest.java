package org.example.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PaymentRequest {
    private String phoneNumber;
    private double amount;
    private String currency;
    private String externalId;
}
