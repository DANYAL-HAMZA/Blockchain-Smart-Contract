package org.example;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.web3j.EVMTest;
import org.web3j.crypto.Credentials;
import org.web3j.model.DSCEngine;
import org.web3j.model.DecentralizedCoin;
import org.web3j.model.ERC20Mock;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.evm.utils.TestAccountsConstants;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

/**
 * Unit test for simple App.
 */
@EVMTest
public class AppTest extends TestCase {
    private Web3j web3j = null;
    private Credentials credentials = null;

    private final String userAddress1 = Credentials.create("0xaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa").getAddress();
    private final String userAddress2 = Credentials.create("0xbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb").getAddress();

    ERC20Mock mockETH = ERC20Mock.deploy(web3j,credentials,new DefaultGasProvider(),BigInteger.ZERO,"Mock ETH",
            "mETH",credentials.getAddress(),BigInteger.valueOf(1000000).multiply(BigInteger.TEN.pow(18))).send();
    ERC20Mock mockDAI = ERC20Mock.deploy(web3j,credentials,new DefaultGasProvider(),BigInteger.ZERO,"Mock DAI",
            "mDAI", credentials.getAddress(), BigInteger.valueOf(1000000).multiply(BigInteger.TEN.pow(18))).send();
    ERC20Mock mockBTC = ERC20Mock.deploy(web3j,credentials,new DefaultGasProvider(),BigInteger.ZERO,"Mock BTC",
            "mBTC",credentials.getAddress(),BigInteger.valueOf(1000000).multiply(BigInteger.TEN.pow(18))).send();


    List<String> priceFeedAddresses = Arrays.asList(
            "0x694AA1769357215DE4FAC081bf1f309aDC325306", // WETH / USD
            "0x1b44F3514812d835EB1BDB0acB33d3fA3351Ee43"     //WBTC/USD

    );

    List<String> tokenAddresses = Arrays.asList(
            mockETH.getContractAddress(), // WETH / USD
                mockBTC.getContractAddress(), //WBTC/USD
            mockDAI.getContractAddress()

    );

    @Autowired
    public AppTest(Web3j web3j, Credentials credentials) throws Exception {
        this.web3j = web3j;
        this.credentials = credentials;
    }

    public void getAccountAddress(){

    }

@Test
    public void testDeployContract() throws Exception {
        DecentralizedCoin decentralizedCoin = DecentralizedCoin.deploy(web3j,credentials,new DefaultGasProvider()).send();

        String dscAddress = decentralizedCoin.getContractAddress();

        decentralizedCoin.transferOwnership(dscAddress);
        DSCEngine dscEngine = DSCEngine.deploy(web3j,credentials, new DefaultGasProvider(),tokenAddresses,priceFeedAddresses,dscAddress).send();
        assertTrue(dscEngine.isValid());
        System.out.println("contractAddress:" + dscEngine.getContractAddress());
    }


    @Test
     public void mintDscTokenTest(){
        String contractAddress = "";
        DSCEngine dscEngine = DSCEngine.load(contractAddress,web3j,credentials,new DefaultGasProvider());
        dscEngine.mintDsc(userAddress1, BigInteger.valueOf(1));


     }

}
