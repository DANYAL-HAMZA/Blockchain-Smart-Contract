package org.example.Controller;

import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import org.example.Entity.DefiTransaction;
import org.example.Service.BlockChainBankStatement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/blockchainBankStatement")
public class BlockchainBankStatementController {
    private BlockChainBankStatement bankStatement;
    @GetMapping("/generateBlockchainStatement")
    public List<DefiTransaction> generateBlockchainBankStatement(@RequestParam String accountNumber,
                                                                 @RequestParam String startDate, @RequestParam String endDate) throws DocumentException, FileNotFoundException {
        return bankStatement.generateBlockchainStatement(accountNumber, startDate, endDate);

    }
}
