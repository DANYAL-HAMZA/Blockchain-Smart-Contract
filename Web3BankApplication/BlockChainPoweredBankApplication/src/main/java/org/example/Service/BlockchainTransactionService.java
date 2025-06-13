package org.example.Service;

import org.example.Entity.Response;
import org.example.dto.BlockchainTransactionRequest;

public interface BlockchainTransactionService {
    Response mintTokenToAddress(BlockchainTransactionRequest blockchainTransactionRequest) throws Exception;
    Response transferTokensToNonAccountHolder(BlockchainTransactionRequest blockchainTransactionRequest) throws Exception;

    Response transferTokensToAccountHolder(BlockchainTransactionRequest blockchainTransactionRequest) throws Exception;

    Response convertTokenToCash(BlockchainTransactionRequest blockchainTransactionRequest) throws Exception;

    }

