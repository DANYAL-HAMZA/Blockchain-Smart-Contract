package org.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.web3j.crypto.Credentials;
import org.web3j.model.DSCEngine;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;

public class SepoliaTest {

    private final Web3j web3j;
    private final Credentials credentials;

    private final String address1 = "0x78723527393356e3BB25D030720E692eF3Cbb242";
    private final String address2 = "0xf3E0F03651bBd52C21acAd80150BC0c0A0CA1a82";

    private final String sepoliaContractAddress = "0xddf70a799c50eae68235dbdb0c2c8c9593bd207d";
    private final String wethPriceFeedAddress = "0x694AA1769357215DE4FAC081bf1f309aDC325306"; // WETH / USD

    private final String wbtcPriceFeedAddress = "0x1b44F3514812d835EB1BDB0acB33d3fA3351Ee43";    //WBTC/USD
    private final String wethTokenAddress = "0xdd13E55209Fd76AfE204dBda4007C227904f0a81"; // WETH/USD
    private final String wbtcTokenAddress = "0x8f3Cf7ad23Cd3CaDbD9735AFf958023239c6A063"; // WBTC/USD

    @Autowired
    public SepoliaTest(Web3j web3j, Credentials credentials) {
        this.web3j = web3j;
        this.credentials = credentials;
    }

@Test
public void mintDscTokenTest () {
        DSCEngine dscEngine = DSCEngine.load(sepoliaContractAddress, web3j, credentials, new DefaultGasProvider());
        dscEngine.mintDsc(address1, BigInteger.valueOf(1));


        }


    }
