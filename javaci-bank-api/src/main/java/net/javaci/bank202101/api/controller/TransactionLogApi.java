package net.javaci.bank202101.api.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.javaci.bank202101.api.dto.TransactionLogDto;

@RestController
@RequestMapping(TransactionLogApi.API_TRANSACTION_BASE_URL)
public class TransactionLogApi {

    static final String API_TRANSACTION_BASE_URL = "/api/transaction";

    @GetMapping("/list")
    public List<TransactionLogDto> listAll(){
        return null;
    }
    
    @PostMapping("/transfer")
    public TransactionLogDto transfer(Long accountId, String toAccountNumber, BigDecimal amount) {
        return null;
    }
    
    @PostMapping("/withdraw")
    public TransactionLogDto withdraw(Long accountId, BigDecimal amount) {
        return null;
    }
    
    @PostMapping("/deposit")
    public TransactionLogDto deposit(Long accountId, BigDecimal amount) {
        return null;
    }
}
