package org.web3j.model;

import java.math.BigInteger;
import java.util.Arrays;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.11.0.
 */
@SuppressWarnings("rawtypes")
public class IERC1822ProxiableUpgradeable extends Contract {
    public static final String BINARY = "";

    public static final String FUNC_PROXIABLEUUID = "proxiableUUID";

    @Deprecated
    protected IERC1822ProxiableUpgradeable(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected IERC1822ProxiableUpgradeable(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected IERC1822ProxiableUpgradeable(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected IERC1822ProxiableUpgradeable(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<byte[]> proxiableUUID() {
        final Function function = new Function(FUNC_PROXIABLEUUID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    @Deprecated
    public static IERC1822ProxiableUpgradeable load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new IERC1822ProxiableUpgradeable(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static IERC1822ProxiableUpgradeable load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new IERC1822ProxiableUpgradeable(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static IERC1822ProxiableUpgradeable load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new IERC1822ProxiableUpgradeable(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static IERC1822ProxiableUpgradeable load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new IERC1822ProxiableUpgradeable(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<IERC1822ProxiableUpgradeable> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(IERC1822ProxiableUpgradeable.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<IERC1822ProxiableUpgradeable> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(IERC1822ProxiableUpgradeable.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<IERC1822ProxiableUpgradeable> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(IERC1822ProxiableUpgradeable.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<IERC1822ProxiableUpgradeable> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(IERC1822ProxiableUpgradeable.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
