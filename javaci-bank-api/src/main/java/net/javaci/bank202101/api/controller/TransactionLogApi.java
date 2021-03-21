package net.javaci.bank202101.api.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import net.javaci.bank202101.api.dto.TransactionLogDto;
import net.javaci.bank202101.api.helper.ModelValidationHelper;
import net.javaci.bank202101.db.dao.AccountDao;
import net.javaci.bank202101.db.dao.TransactionLogDao;
import net.javaci.bank202101.db.dao.impl.TransactionLogDaoImpl;
import net.javaci.bank202101.db.model.Account;
import net.javaci.bank202101.db.model.TransactionLog;
import net.javaci.bank202101.db.model.enumaration.AccountStatusType;
import net.javaci.bank202101.db.model.enumaration.TransactionLogType;

@RestController
@RequestMapping(TransactionLogApi.API_TRANSACTION_BASE_URL)
public class TransactionLogApi {

    static final String API_TRANSACTION_BASE_URL = "/api/transaction";

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private ModelValidationHelper modelValidationHelper;
    
    @Autowired
    private AccountDao accountDao;
    
    @Autowired
    private TransactionLogDaoImpl transactionLogDao; 
    
    @GetMapping("/list")
    public List<TransactionLogDto> listAll(Long accountId){
        
        Account account = modelValidationHelper.findAndCheckAccount(accountId);
  
        List<TransactionLog> transactionLogs = transactionLogDao.findAllByAccount(account);
        
        List<TransactionLogDto> resultList = new ArrayList<>();
        for (TransactionLog transactionLog : transactionLogs) {
            TransactionLogDto convertedDto = modelMapper.map(transactionLog, TransactionLogDto.class);
            resultList.add(convertedDto);
        }
        
        return resultList;
    }
    
    @Transactional
    @PostMapping("/transfer")
    public TransactionLogDto transfer(Long accountId, String toAccountNumber, BigDecimal amount) {
        
        Account fromAccount = modelValidationHelper.findAndCheckAccount(accountId);
        checkAccountIsActive(fromAccount);
        if(fromAccount.getBalance().compareTo(amount) < 0 ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Given account does not have enough balance. Account no: " + fromAccount.getAccountNumber());
        }
        
        Account toAccount = accountDao.findByAccountNumber(toAccountNumber);
        checkAccountIsActive(toAccount);
        
        BigDecimal afterBalanceFromAccount = fromAccount.getBalance().subtract(amount);
        fromAccount.setBalance(afterBalanceFromAccount);
        accountDao.save(fromAccount);
        
        BigDecimal afterBalanceToAccount = toAccount.getBalance().add(amount);
        toAccount.setBalance(afterBalanceToAccount);
        accountDao.save(toAccount);
        
        TransactionLog transactionLog = new TransactionLog();
        transactionLog.setAmount(amount);
        transactionLog.setAccount(fromAccount);
        transactionLog.setToAccount(toAccount);
        transactionLog.setBalance(afterBalanceFromAccount);
        transactionLog.setDate(LocalDate.now());
        transactionLog.setDescription("");
        transactionLog.setType(TransactionLogType.MONEY_TRANSFER);
        transactionLogDao.save(transactionLog);
        
        return modelMapper.map(transactionLog, TransactionLogDto.class);
    }

    @PostMapping("/withdraw")
    public TransactionLogDto withdraw(Long accountId, BigDecimal amount) {
        return null;
    }
    
    @PostMapping("/deposit")
    public TransactionLogDto deposit(Long accountId, BigDecimal amount) {
        return null;
    }
    
    // *****************************************************************
    // HELPER METHOD(S)
    // *****************************************************************
    
    private void checkAccountIsActive(Account account) {
        if(!account.getStatus().equals(AccountStatusType.ACTIVE)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Given account is not active. Account no: " + account.getAccountNumber());
        }
    }
    
}
