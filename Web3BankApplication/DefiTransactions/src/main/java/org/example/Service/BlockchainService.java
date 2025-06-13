package org.example.Service;

import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.model.DSCEngine;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.DefaultGasProvider;



@Service
public class BlockchainService {
    private final Web3j web3j;
    private final Credentials credentials;


    public BlockchainService(Web3j web3j, Credentials credentials) {
        this.web3j = web3j;
        this.credentials = credentials;
    }
//
    public DSCEngine loadContract()throws Exception {
        String contractAddress = "0xddf70a799c50eae68235dbdb0c2c8c9593bd207d";
        return DSCEngine.load(contractAddress,web3j,credentials,new DefaultGasProvider());


    }
}
