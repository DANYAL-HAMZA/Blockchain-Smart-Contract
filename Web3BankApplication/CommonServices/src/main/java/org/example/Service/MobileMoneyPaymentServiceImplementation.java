package org.example.Service;

import org.example.Entity.Payer;
import org.example.Entity.PaymentRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MobileMoneyPaymentServiceImplementation implements MobileMoneyPaymentService{


        @Value("${momo.base.url}")
        private String baseUrl;

        @Value("${momo.api.key}")
        private String subscriptionKey;

        @Autowired
        private MobileMoneyAuthenticationService mobileMoneyAuthenticationService;


        @Override
    public String initiatePayment(PaymentRequest paymentRequest) {
        try {
            String url = baseUrl + "/collection/v1_0/requesttopay";
            String token = mobileMoneyAuthenticationService.generateToken();

            HttpPost post = new HttpPost(url);
            post.setHeader("Authorization", "Bearer " + token);
            post.setHeader("X-Reference-Id", UUID.randomUUID().toString());
            post.setHeader("X-Target-Environment", "sandbox");
            post.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
            post.setHeader("Content-Type", "application/json");

            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("amount", String.valueOf(paymentRequest.getAmount()));
            requestBody.addProperty("currency", paymentRequest.getCurrency());
            requestBody.addProperty("externalId", paymentRequest.getExternalId());
            requestBody.addProperty("payer", new Gson().toJson(new Payer("MSISDN", paymentRequest.getPhoneNumber())));
            requestBody.addProperty("payerMessage", "Payment for services");
            requestBody.addProperty("payeeNote", "Thank you!");

            StringEntity entity = new StringEntity(requestBody.toString());
            post.setEntity(entity);

            HttpClient client = HttpClients.createDefault();
            HttpResponse response = client.execute(post);

            if (response.getStatusLine().getStatusCode() == 202) {
                return "Payment initiated successfully!";
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initiate payment: " + e.getMessage(), e);
        }
        return "Payment initiation failed!";
    }
}









