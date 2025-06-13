package org.example.Service;

import org.example.Entity.PaymentRequest;
import org.springframework.stereotype.Service;

@Service
public interface MobileMoneyPaymentService {
    String initiatePayment(PaymentRequest paymentRequest);


}
