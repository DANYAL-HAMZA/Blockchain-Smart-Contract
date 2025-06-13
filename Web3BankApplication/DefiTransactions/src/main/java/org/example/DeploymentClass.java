package org.example;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.web3j.crypto.Credentials;
import org.web3j.model.DSCEngine;
import org.web3j.model.DecentralizedCoin;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.DefaultGasProvider;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class DeploymentClass {

  private final Web3j web3j;

    private final Credentials credentials;




    List<String> tokenAddresses = Arrays.asList(

     "0xdd13E55209Fd76AfE204dBda4007C227904f0a81", // WETH/USD
     "0x8f3Cf7ad23Cd3CaDbD9735AFf958023239c6A063" // WBTC/USD
    );

    List<String> priceFeedAddresses = Arrays.asList(
            "0x694AA1769357215DE4FAC081bf1f309aDC325306", // WETH / USD
     "0x1b44F3514812d835EB1BDB0acB33d3fA3351Ee43"     //WBTC/USD





    );

    @Autowired

    public DeploymentClass(Web3j web3j, Credentials credentials) {
        this.web3j = web3j;
        this.credentials = credentials;

    }

    public String deploy() throws Exception {
        DecentralizedCoin decentralizedCoin = DecentralizedCoin.deploy(web3j,credentials,new DefaultGasProvider()).send();

         String dscAddress = decentralizedCoin.getContractAddress();


         decentralizedCoin.transferOwnership(dscAddress);
        TransactionReceipt transactionReceipt =
                DSCEngine.deploy(web3j,credentials, new DefaultGasProvider(),tokenAddresses,priceFeedAddresses,dscAddress).send().getTransactionReceipt().get();

        return transactionReceipt.getContractAddress();
    }
    @PostConstruct
    public void init() throws Exception {
        String contractAddress = deploy();
        System.out.println("Smart contract deployed at:" + contractAddress);
    }
    public static void main(String[] args){
        SpringApplication.run(DeploymentClass.class,args);

    }


}
