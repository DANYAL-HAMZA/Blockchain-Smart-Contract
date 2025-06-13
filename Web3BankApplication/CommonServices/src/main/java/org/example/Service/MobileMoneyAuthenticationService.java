package org.example.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class MobileMoneyAuthenticationService {


        @Value("${momo.base.url}")
        private String baseUrl;

        @Value("${momo.api.key}")
        private String apiKey;

        @Value("${momo.api.user}")
        private String apiUser;

        public String generateToken() {
            try {
                String url = baseUrl + "/collection/token/";

                HttpPost post = new HttpPost(url);
                post.setHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((apiUser + ":" + apiKey).getBytes()));
                post.setHeader("Ocp-Apim-Subscription-Key", apiKey);

                HttpClient client = HttpClients.createDefault();
                HttpResponse response = client.execute(post);

                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseBody = EntityUtils.toString(response.getEntity());
                    JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
                    return jsonObject.get("access_token").getAsString();
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to generate token: " + e.getMessage(), e);
            }
            return null;
        }
    }


