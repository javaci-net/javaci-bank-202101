package net.javaci.bank202101.api.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.javaci.bank202101.api.dto.TransferTransactionDto;
import net.javaci.bank202101.api.helper.ModelValidationHelper;
import net.javaci.bank202101.db.model.Account;
import net.javaci.bank202101.db.model.enumaration.AccountStatusType;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class TransactionLogApiTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @InjectMocks
    private TransactionLogApi controller;
    
    @MockBean
    private ModelValidationHelper modelValidationHelper;
    
    @Test
    void testTransferFromZeroBalanceAccount() throws Exception {
        
        // Given
        Long accountId = 999L;
        TransferTransactionDto transferRequest = new TransferTransactionDto();
        transferRequest.setAccountId(accountId);
        transferRequest.setAmount(BigDecimal.TEN);
        transferRequest.setToAccountNumber("0001234-01");
        
        Account fromAccount = new Account();
        fromAccount.setId(accountId);
        fromAccount.setStatus(AccountStatusType.ACTIVE);
        fromAccount.setBalance(BigDecimal.ZERO); // !!!!!!!!!!
        
        Mockito.when(modelValidationHelper.findAndCheckAccount(any()))
            .thenReturn(fromAccount);
        
        // When
        ResultActions perform = mockMvc.perform(
          post(TransactionLogApi.API_TRANSACTION_BASE_URL + "/transfer")
          .content(new ObjectMapper().writeValueAsString(transferRequest))
          .contentType(MediaType.APPLICATION_JSON)
        );
        
        
        // Then
        perform
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(status().reason(containsString("Given account does not have enough balance")));
        
    }

}
