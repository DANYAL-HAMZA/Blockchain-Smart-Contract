package org.example.Service;

import org.example.Entity.Response;
import org.example.dto.DefiTransactionRequest;
import org.springframework.stereotype.Service;
import org.web3j.tuples.generated.Tuple2;
@Service
public interface DefiTransactionService {
    Response depositCollateralAndMintDsc(DefiTransactionRequest defiTransactionRequest) throws Exception;
    Response redeemCollateralForDsc(DefiTransactionRequest defiTransactionRequest) throws Exception;
    Response redeemCollateral(DefiTransactionRequest defiTransactionRequest) throws Exception;
    Response burnDsc(DefiTransactionRequest defiTransactionRequest) throws Exception;
    Response liquidateUser(DefiTransactionRequest defiTransactionRequest) throws Exception;
    Response depositCollateral(DefiTransactionRequest defiTransactionRequest) throws Exception;
    Response mintDsc(DefiTransactionRequest defiTransactionRequest) throws Exception;
    Response convertDscToCash(DefiTransactionRequest defiTransactionRequest) throws Exception;
    Tuple2 getUnderCollateralizedUsersAndTheirDebt() throws Exception;
}
