package org.example.Controller;

import org.example.Entity.Response;
import org.example.dto.BlockchainTransactionRequest;
import org.example.Service.BlockchainTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users/blockchain")

@Tag(name = "User blockchain account Management Apis")

public class BlockchainController {
private final BlockchainTransactionService blockchainTransactionService;
@Autowired
    public BlockchainController(BlockchainTransactionService blockchainTransactionService) {
        this.blockchainTransactionService = blockchainTransactionService;
    }

    @Operation(
            summary = "mint token ",
            description = "Mints a token that represents an amount to the blockchain wallet address of the user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 SUCCESS"
    )
    @PostMapping("/mintDsc")
    public Response mintTokenToAddress(@RequestBody BlockchainTransactionRequest blockchainTransactionRequest) throws Exception {
        return blockchainTransactionService.mintTokenToAddress(blockchainTransactionRequest);

    }

    @Operation(
            summary = "convert token to cash",
            description = "Converts a specified amount of the token to be converted back into cash"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 SUCCESS"
    )
    @PostMapping("/convertTokenToCash")
    public Response convertTokenToCash(@RequestBody BlockchainTransactionRequest blockchainTransactionRequest) throws Exception {
        return blockchainTransactionService.convertTokenToCash(blockchainTransactionRequest);

    }

    @Operation(
            summary = "transfer token to non account holder",
            description = "Transfers a bank token amount from an account holder to a non account holder"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 SUCCESS"
    )
    @PostMapping("/transferTokenToNonAccountHolder")
    public Response transferTokenToNonAccountHolder(@RequestBody BlockchainTransactionRequest blockchainTransactionRequest) throws Exception {
        return blockchainTransactionService.transferTokensToNonAccountHolder(blockchainTransactionRequest);
    }

    @Operation(
            summary = "transfer bank tokens to account holder",
            description = "Transfers a bank token amount to an account holder"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 SUCCESS"
    )
    @PostMapping("/transferTokenToAccountHolder")
    public Response transferTokenToAccountHolder(@RequestBody BlockchainTransactionRequest blockchainTransactionRequest) throws Exception {
        return blockchainTransactionService.transferTokensToAccountHolder(blockchainTransactionRequest);

    }
}









