package org.example.Controller;

import org.example.Entity.PaymentRequest;
import org.example.Service.MobileMoneyPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MobileMoneyController {
    @RestController
    @RequestMapping("/momo")
    public class MomoController {

        @Autowired
        private MobileMoneyPaymentService mobileMoneyPaymentService;

        @PostMapping("/pay")
        public ResponseEntity<String> initiatePayment(@RequestBody PaymentRequest paymentRequest) {
            try {
                String response = mobileMoneyPaymentService.initiatePayment(paymentRequest);
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }
    }

}
