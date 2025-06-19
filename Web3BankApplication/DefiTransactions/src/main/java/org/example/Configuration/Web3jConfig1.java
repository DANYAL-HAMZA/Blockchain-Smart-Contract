package org.example.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.web3j.crypto.Credentials;

import org.web3j.model.DSCEngine;
import org.web3j.model.DecentralizedCoin;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.math.BigInteger;

/*
Contract Address: 0x6B175474E89094C44Da98b954EedeAC495271d0F

Token Type: ERC-20

Decimals: 18

2. Bitcoin (BTC) on Ethereum
Contract Address: 0x1f3143518092a2dc93728a11afcc6d4b100b0959

Token Type: ERC-20 (Wrapped BTC representation)

Decimals: 18


3. Ether (ETH)
Note: Ether (ETH) is the native currency of the Ethereum network and does not have a contract address like ERC-20 tokens. However, for applications requiring an ERC-20 representation of ETH, you can use Wrapped Ether (WETH).
*/
    @Configuration
    public class Web3jConfig1 {



    @Value("${web3j.provider.url}")
    String providerUrl;

    @Value("${wallet.private.key}")
    String privateKey;

        @Bean
        public Web3j web3j() {
           // String providerUrl = env.getProperty("NODE_URL");
            return Web3j.build(new HttpService(providerUrl));
        }

        @Bean
        public Credentials credentials() {
           // String privateKey = env.getProperty("PRIVATE_KEY"); // Use a more secure environment variable name
            return Credentials.create(privateKey);
        }





    }

