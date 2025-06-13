package org.example.Controller;

import org.example.Entity.Response;
import org.example.Service.DefiTransactionService;
import org.example.dto.DefiTransactionRequest;
import org.example.dto.DefiUserRequest;
import org.springframework.web.bind.annotation.*;
import org.web3j.tuples.generated.Tuple2;

@RestController
@RequestMapping("/defiTransactions")
public class DefiTransactionsController {
    private final DefiTransactionService defiTransactionService;

    public DefiTransactionsController(DefiTransactionService defiTransactionService) {
        this.defiTransactionService = defiTransactionService;
    }
    @PostMapping("/depositCollateralAndMintDsc")
    public Response depositCollateralAndMintDsc(@RequestBody DefiTransactionRequest defiTransactionRequest) throws Exception {
        return defiTransactionService.depositCollateralAndMintDsc(defiTransactionRequest);
    }
    @PostMapping("/redeemCollateralForDsc")
    public Response redeemCollateralForDsc(@RequestBody DefiTransactionRequest defiTransactionRequest) throws Exception {
        return defiTransactionService.redeemCollateralForDsc(defiTransactionRequest);
    }
    @PostMapping("/redeemCollateral")
    public Response redeemCollateral(@RequestBody DefiTransactionRequest defiTransactionRequest) throws Exception {
        return defiTransactionService.redeemCollateral(defiTransactionRequest);
    }
    @PostMapping("/burnDscForDefi")
    public Response burnDsc(@RequestBody DefiTransactionRequest defiTransactionRequest) throws Exception {
        return defiTransactionService.burnDsc(defiTransactionRequest);
    }
    @PostMapping("/liquidateUser")
    public Response liquidateUser(@RequestBody DefiTransactionRequest defiTransactionRequest) throws Exception {
        return defiTransactionService.liquidateUser(defiTransactionRequest);
    }
@PostMapping("/depositCollateral")
    public Response depositeCollateral(@RequestBody DefiTransactionRequest defiTransactionRequest) throws Exception {
        return defiTransactionService.depositCollateral(defiTransactionRequest);
    }
    @PostMapping("/mintDscForDefi")

    public Response mintDsc(@RequestBody DefiTransactionRequest defiTransactionRequest) throws Exception {
        return defiTransactionService.mintDsc(defiTransactionRequest);
    }
@GetMapping("/getUnderCollateralizedUsersAndTheirDebts")
    public Tuple2 getUnderCollateralizedUsersAndTheirDebts() throws Exception {
        return defiTransactionService.getUnderCollateralizedUsersAndTheirDebt();
    }
}
