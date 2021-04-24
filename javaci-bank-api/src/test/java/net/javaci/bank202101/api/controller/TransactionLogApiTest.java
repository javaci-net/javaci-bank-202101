package net.javaci.bank202101.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.javaci.bank202101.api.dto.TransferTransactionDto;
import net.javaci.bank202101.api.helper.ModelValidationHelper;
import net.javaci.bank202101.db.dao.AccountDao;
import net.javaci.bank202101.db.dao.impl.TransactionLogDaoImpl;
import net.javaci.bank202101.db.model.Account;
import net.javaci.bank202101.db.model.Customer;
import net.javaci.bank202101.db.model.TransactionLog;
import net.javaci.bank202101.db.model.enumaration.AccountStatusType;
import net.javaci.bank202101.db.model.enumaration.TransactionLogType;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class TransactionLogApiTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private TransactionLogApi controller;
    
    @MockBean private TransactionLogDaoImpl transactionLogDao; 
    @MockBean private ModelValidationHelper modelValidationHelper;
    @MockBean private AccountDao accountDao;
    
    @Test
    void testList() throws Exception {
        // Given
        Long toAccountId = 111L;
        Long fromAccountId = 999L;

        Account fromAccount = new Account();
        fromAccount.setId(fromAccountId);
        fromAccount.setStatus(AccountStatusType.ACTIVE);
        Mockito.when(modelValidationHelper.findAndCheckAccount(any())).thenReturn(fromAccount);
        
        Account toAccount = new Account();
        Customer customer = new Customer();
        customer.setFirstName("Jane");
        customer.setLastName("Doe");
        toAccount.setCustomer(customer);
        toAccount.setId(toAccountId);
        
        List<TransactionLog> transactionLogs = new ArrayList<>();
        TransactionLog dummy1 = new TransactionLog();
        dummy1.setAccount(fromAccount);
        dummy1.setDate(LocalDate.of(2021, 1, 1));
        dummy1.setAmount(BigDecimal.ONE);
        dummy1.setType(TransactionLogType.WITHDRAW);
        dummy1.setDescription("Test operation");
        dummy1.setToAccount(toAccount);
        transactionLogs.add(dummy1);
        Mockito.when(transactionLogDao.findAllByAccount(fromAccount)).thenReturn(transactionLogs);
        String expectedJsonResult = 
                "[{\"id\":null,\"toCustomerName\":null,\"toAccountId\":null,\"amount\":1,\"type\":\"WITHDRAW\",\"description\":\"Test operation\"}]";
        
        // When
        ResultActions perform =  this.mockMvc.perform(
                get(TransactionLogApi.API_TRANSACTION_BASE_URL + "/list")
        );
        
        // Then
        MvcResult result = perform
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
        
        String resultAsStr = result.getResponse().getContentAsString();
        assertThat(resultAsStr).isEqualTo(
                expectedJsonResult
        );
        assertEquals(expectedJsonResult, resultAsStr);
    }
    
    @Test
    void testTransferFromAnInactiveAccount() throws Exception {
        
        // Given
        Long accountId = 999L;
        
        Account fromAccount = new Account();
        fromAccount.setId(accountId);
        fromAccount.setStatus(AccountStatusType.CLOSED);
        Mockito.when(modelValidationHelper.findAndCheckAccount(any())).thenReturn(fromAccount);
        
        TransferTransactionDto transferRequest = new TransferTransactionDto();
        transferRequest.setAccountId(accountId);
        transferRequest.setAmount(BigDecimal.TEN);
        transferRequest.setToAccountNumber("1234567-01");
        
        // When
        ResultActions perform =  this.mockMvc.perform(
                post(TransactionLogApi.API_TRANSACTION_BASE_URL + "/transfer")
                .content(new ObjectMapper().writeValueAsString(transferRequest))
                .contentType(MediaType.APPLICATION_JSON)
        );
        
        // Then
        perform
            .andDo(print())
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
            .andExpect(status().reason(containsString("Given account is not active")))
            .andReturn();
        
    }
    
    @Test
    void testTransferFromZeroBalanceAccount() throws Exception {
        
        // Given
        Long accountId = 999L;
        
        Account fromAccount = new Account();
        fromAccount.setId(accountId);
        fromAccount.setStatus(AccountStatusType.ACTIVE);
        fromAccount.setBalance(BigDecimal.ZERO);
        Mockito.when(modelValidationHelper.findAndCheckAccount(any())).thenReturn(fromAccount);
        
        TransferTransactionDto transferRequest = new TransferTransactionDto();
        transferRequest.setAccountId(accountId);
        transferRequest.setAmount(BigDecimal.TEN);
        transferRequest.setToAccountNumber("1234567-01");
        
        // When
        ResultActions perform =  this.mockMvc.perform(
                post(TransactionLogApi.API_TRANSACTION_BASE_URL + "/transfer")
                .content(new ObjectMapper().writeValueAsString(transferRequest))
                .contentType(MediaType.APPLICATION_JSON)
        );
        
        // Then
        perform
            .andDo(print())
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
            .andExpect(status().reason(containsString("Given account does not have enough balance")))
            .andReturn();
        
    }
    
    @Test
    void testTransfer() throws Exception {
        
        // Given
        Long accountId = 999L;
        
        Account fromAccount = new Account();
        fromAccount.setId(accountId);
        fromAccount.setStatus(AccountStatusType.ACTIVE);
        fromAccount.setBalance(new BigDecimal("1000"));
        Mockito.when(modelValidationHelper.findAndCheckAccount(any())).thenReturn(fromAccount);
        Mockito.when(accountDao.findByAccountNumber(any())).thenReturn(fromAccount);
        Mockito.doNothing().when(accountDao).save(any());
        Mockito.when(transactionLogDao.save(any(TransactionLog.class))).then(AdditionalAnswers.returnsFirstArg());
        
        TransferTransactionDto transferRequest = new TransferTransactionDto();
        transferRequest.setAccountId(accountId);
        transferRequest.setAmount(BigDecimal.TEN);
        transferRequest.setToAccountNumber("1234567-01");
        
        String expectedJsonResult = 
                "{\"id\":null,\"toCustomerName\":null,\"toAccountId\":null,\"amount\":10,\"type\":\"MONEY_TRANSFER\",\"description\":\"\"}";
        
        
        // When
        ResultActions perform =  this.mockMvc.perform(
                post(TransactionLogApi.API_TRANSACTION_BASE_URL + "/transfer")
                .content(new ObjectMapper().writeValueAsString(transferRequest))
                .contentType(MediaType.APPLICATION_JSON)
        );
        
        // Then
        MvcResult result = perform
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
        
        String resultAsStr = result.getResponse().getContentAsString();
        assertThat(resultAsStr).isEqualTo(
                expectedJsonResult
        );
        assertEquals(expectedJsonResult, resultAsStr);
    }

}
