package net.javaci.bank202101.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.javaci.bank202101.api.dto.AccountListDto;
import net.javaci.bank202101.api.dto.AccountSaveDto;
import net.javaci.bank202101.api.helper.ModelValidationHelper;
import net.javaci.bank202101.db.dao.AccountDao;
import net.javaci.bank202101.db.model.Account;
import net.javaci.bank202101.db.model.Customer;
import net.javaci.bank202101.db.model.enumaration.AccountStatusType;

@Tag(description = "User Bank Accounts API", name = "account-api")
@Slf4j
@RestController
@RequestMapping(AccountApi.API_ACCOUNT_BASE_URL)
public class AccountApi {

    public static final String API_ACCOUNT_BASE_URL = "/api/account";

    @Autowired
    private ModelValidationHelper modelValidationHelper;
    
    @Autowired
    private AccountDao accountDao;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Operation(summary = "listall", description = "Returns list of all accounts")
    @GetMapping("/list")
    public List<AccountListDto> listAll(String citizenNumber){
        
        Optional<Customer> existingCustomer = modelValidationHelper.findAndCheckCustomer(citizenNumber);
        
        List<AccountListDto> accountListDto = new ArrayList<>();
        Set<Account> dbAccounts = existingCustomer.get().getAccounts();
        for (Account account : dbAccounts) {
            AccountListDto convertedDto = convertToDto(account);
            accountListDto.add(convertedDto); 
        }
        
        /*--
        // Yukaridaki for dongusunun java stream hali
        List<AccountListDto> accountListDto = dbAccounts
            .stream()
            .map( acc -> modelMapper.map(acc, AccountListDto.class) )
            .collect(Collectors.toList());
         */
        
        return accountListDto;
    }

    @PostMapping("/create/{citizenNumber}")
    public Long create(
            @PathVariable("citizenNumber") String citizenNumber, 
            @RequestBody AccountSaveDto newAccountDto
    ) {
        Optional<Customer> existingCustomer = modelValidationHelper.findAndCheckCustomer(citizenNumber);
        
        Account dbAccount = modelMapper.map(newAccountDto, Account.class);
        dbAccount.setCustomer(existingCustomer.get());
        
        Long customerId = existingCustomer.get().getId();
        int existingAccountCount = accountDao.countByCustomerId(customerId);
        String accountNumber = String.format("%07d-%02d", customerId, existingAccountCount+1 );
        dbAccount.setAccountNumber(accountNumber);
        
        accountDao.save(dbAccount);
        
        log.info("Account added with {} id", dbAccount.getId());
        
        return dbAccount.getId();
    }
    
    @GetMapping("/getInfo")
    public AccountListDto getInfo(Long accountId) {
        Account dbAccount = modelValidationHelper.findAndCheckAccount(accountId);
        return convertToDto(dbAccount);
    }

    @PostMapping("/close")
    public AccountListDto close(@RequestBody Long accountId) {
        Account dbAccount = modelValidationHelper.findAndCheckAccount(accountId);
        dbAccount.setStatus(AccountStatusType.CLOSED);
        accountDao.save(dbAccount);
        return convertToDto(dbAccount);
    }
    
    
    // *****************************************************************
    // HELPER METHOD(S)
    // *****************************************************************
    
    private AccountListDto convertToDto(Account dbAccount) {
        return modelMapper.map(dbAccount, AccountListDto.class);
    }

}
