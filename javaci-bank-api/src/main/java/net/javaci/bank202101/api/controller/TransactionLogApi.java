package net.javaci.bank202101.api.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import net.javaci.bank202101.api.dto.ExchangeTransactionDto;
import net.javaci.bank202101.api.dto.SingleAccountTransactionDto;
import net.javaci.bank202101.api.dto.TransactionLogDto;
import net.javaci.bank202101.api.dto.TransferTransactionDto;
import net.javaci.bank202101.api.helper.ModelValidationHelper;
import net.javaci.bank202101.db.dao.AccountDao;
import net.javaci.bank202101.db.dao.impl.ExchangeRateDaoImpl;
import net.javaci.bank202101.db.dao.impl.TransactionLogDaoImpl;
import net.javaci.bank202101.db.model.Account;
import net.javaci.bank202101.db.model.ExchangeRate;
import net.javaci.bank202101.db.model.TransactionLog;
import net.javaci.bank202101.db.model.enumaration.AccountCurrency;
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
    
    @Autowired
    private ExchangeRateDaoImpl exchangeRateDao;
    
    @GetMapping("/list")
    public List<TransactionLogDto> listAll(Long accountId){
        
        Account account = modelValidationHelper.findAndCheckAccount(accountId);
  
        List<TransactionLog> transactionLogs = transactionLogDao.findAllByAccount(account);
        
        List<TransactionLogDto> resultList = new ArrayList<>();
        for (TransactionLog transactionLog : transactionLogs) {
            TransactionLogDto convertedDto = convertToDto(transactionLog);
            resultList.add(convertedDto);
        }
        
        return resultList;
    }
    
    @Transactional(rollbackOn = Exception.class)
    @PostMapping("/transfer")
    public TransactionLogDto transfer(@RequestBody @Valid TransferTransactionDto transferRequest) {
        
        Account fromAccount = modelValidationHelper.findAndCheckAccount(transferRequest.getAccountId());
        checkAccountIsActive(fromAccount);
        if(fromAccount.getBalance().compareTo(transferRequest.getAmount()) < 0 ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Given account does not have enough balance. Account no: " + fromAccount.getAccountNumber());
        }
        
        Account toAccount = accountDao.findByAccountNumber(transferRequest.getToAccountNumber());
        checkAccountIsActive(toAccount);
        
        BigDecimal afterBalanceFromAccount = fromAccount.getBalance().subtract(transferRequest.getAmount());
        fromAccount.setBalance(afterBalanceFromAccount);
        accountDao.save(fromAccount);
        
        BigDecimal afterBalanceToAccount = toAccount.getBalance().add(transferRequest.getAmount());
        toAccount.setBalance(afterBalanceToAccount);
        accountDao.save(toAccount);
        
        TransactionLog transactionLog = new TransactionLog();
        transactionLog.setAmount(transferRequest.getAmount());
        transactionLog.setAccount(fromAccount);
        transactionLog.setToAccount(toAccount);
        transactionLog.setBalance(afterBalanceFromAccount);
        transactionLog.setDate(LocalDate.now());
        transactionLog.setDescription("");
        transactionLog.setType(TransactionLogType.MONEY_TRANSFER);
        transactionLogDao.save(transactionLog);
        
        return convertToDto(transactionLog);
    }

    @Transactional(rollbackOn = Exception.class)
    @PostMapping("/withdraw")
    public TransactionLogDto withdraw(@RequestBody @Valid SingleAccountTransactionDto withdrawRequest) {
        
        Account fromAccount = modelValidationHelper.findAndCheckAccount(withdrawRequest.getAccountId());
        checkAccountIsActive(fromAccount);
        if(fromAccount.getBalance().compareTo(withdrawRequest.getAmount()) < 0 ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Given account does not have enough balance. Account no: " + fromAccount.getAccountNumber());
        }
        
        BigDecimal afterBalanceFromAccount = fromAccount.getBalance().subtract(withdrawRequest.getAmount());
        fromAccount.setBalance(afterBalanceFromAccount);
        accountDao.save(fromAccount);
        
        TransactionLog transactionLog = new TransactionLog();
        transactionLog.setAmount(withdrawRequest.getAmount());
        transactionLog.setAccount(fromAccount);
        transactionLog.setBalance(afterBalanceFromAccount);
        transactionLog.setDate(LocalDate.now());
        transactionLog.setDescription("");
        transactionLog.setType(TransactionLogType.WITHDRAW);
        transactionLogDao.save(transactionLog);
        
        return convertToDto(transactionLog);
    }
    
    @Transactional(rollbackOn = Exception.class)
    @PostMapping("/deposit")
    public TransactionLogDto deposit(@RequestBody @Valid SingleAccountTransactionDto depositRequest) {
        
        Account toAccount = modelValidationHelper.findAndCheckAccount(depositRequest.getAccountId());
        checkAccountIsActive(toAccount);
        
        BigDecimal afterBalanceToAccount = toAccount.getBalance().add(depositRequest.getAmount());
        toAccount.setBalance(afterBalanceToAccount);
        accountDao.save(toAccount);
        
        TransactionLog transactionLog = new TransactionLog();
        transactionLog.setAmount(depositRequest.getAmount());
        transactionLog.setAccount(toAccount);
        transactionLog.setBalance(afterBalanceToAccount);
        transactionLog.setDate(LocalDate.now());
        transactionLog.setDescription("");
        transactionLog.setType(TransactionLogType.DEPOSIT);
        transactionLogDao.save(transactionLog);
        
        return convertToDto(transactionLog);
    }
    
    @Transactional(rollbackOn = Exception.class)
    @PostMapping("/buy-exchange")
    public TransactionLogDto buyExchange(@RequestBody @Valid ExchangeTransactionDto exchangeRequest) {
        Account account = modelValidationHelper.findAndCheckAccount(exchangeRequest.getAccountId());
        checkAccountIsActive(account);
        if( !account.getCurrency().equals(AccountCurrency.TL) ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Exchange transactions are only allowed for TRY accounts. Given account currency: " + account.getCurrency());
        }
        
        if(!exchangeRateDao.existsByDateAndCurrency(LocalDate.now(), exchangeRequest.getCurrency())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    "No exchange rate data for today and currency " + exchangeRequest.getCurrency());
        }
        
        ExchangeRate exchangeRate = exchangeRateDao.findByDateAndCurrency(LocalDate.now(), exchangeRequest.getCurrency());
        BigDecimal tryValue = exchangeRate.getRate().multiply(exchangeRequest.getCount());
        
        if(account.getBalance().compareTo(tryValue) < 0 ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Given account does not have enough balance. Account no: " + account.getAccountNumber());
        }
        
        BigDecimal afterBalance = account.getBalance().subtract(tryValue);
        account.setBalance(afterBalance);
        accountDao.save(account);
        
        TransactionLog transactionLog = new TransactionLog();
        transactionLog.setAmount(tryValue);
        transactionLog.setAccount(account);
        transactionLog.setBalance(afterBalance);
        transactionLog.setDate(LocalDate.now());
        transactionLog.setDescription("");
        transactionLog.setType(TransactionLogType.MONEY_EXHANGE);
        transactionLogDao.save(transactionLog);
        
        return convertToDto(transactionLog);
    }
    
    @Transactional(rollbackOn = Exception.class)
    @PostMapping("/sell-exchange")
    public TransactionLogDto sellExchange(@RequestBody @Valid ExchangeTransactionDto exchangeRequest) {
        Account account = modelValidationHelper.findAndCheckAccount(exchangeRequest.getAccountId());
        checkAccountIsActive(account);
        
        if(!exchangeRateDao.existsByDateAndCurrency(LocalDate.now(), exchangeRequest.getCurrency())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    "No exchange rate data for today and currency " + exchangeRequest.getCurrency());
        }
        
        return null;
    }
    
    
    // *****************************************************************
    // HELPER METHOD(S)
    // *****************************************************************
    
    private TransactionLogDto convertToDto(TransactionLog transactionLog) {
        return modelMapper.map(transactionLog, TransactionLogDto.class);
    }

    private void checkAccountIsActive(Account account) {
        if(!account.getStatus().equals(AccountStatusType.ACTIVE)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Given account is not active. Account no: " + account.getAccountNumber());
        }
    }
    
}
