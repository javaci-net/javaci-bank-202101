package net.javaci.bank202101.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.javaci.bank202101.api.dto.AccountListDto;
import net.javaci.bank202101.api.dto.AccountSaveDto;
import net.javaci.bank202101.db.model.Customer;

@RestController
@RequestMapping(AccountApi.API_ACCOUNT_BASE_URL)
public class AccountApi {

    public static final String API_ACCOUNT_BASE_URL = "/api/account";

    @GetMapping("/list")
    public List<Object> listAll(){
        Customer customer = null;
        return null;
    }
    
    @PostMapping("/create")
    public Long create(@RequestBody AccountSaveDto newAccountDto) {
        return null;
    }
    
    @GetMapping("/getInfo")
    public AccountListDto getInfo(@RequestBody Long accountId) {
        return null;
    }
    
    @PostMapping("/close")
    public AccountListDto close(@RequestBody Long accountId) {
        return null;
    }
    
}
