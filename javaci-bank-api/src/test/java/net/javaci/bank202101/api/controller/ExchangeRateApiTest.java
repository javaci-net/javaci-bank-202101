package net.javaci.bank202101.api.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;

import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import net.javaci.bank202101.db.dao.impl.ExchangeRateDaoImpl;
import net.javaci.bank202101.db.model.ExchangeRate;
import net.javaci.bank202101.db.model.enumaration.AccountCurrency;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class ExchangeRateApiTest {

    @Autowired
    private MockMvc mockMvc;
    
    @InjectMocks
    private ExchangeRateApi controller;
    
    @MockBean
    private ExchangeRateDaoImpl exchangeRateDao;
    
    @Test
    void testFindByLocalDateWhenNoExchangeRateData() throws Exception {
        
        // Given
        Mockito
            .when(exchangeRateDao.existsByDate(any()))
            .thenReturn(false);

        // When
        ResultActions perform = mockMvc.perform(
          get(ExchangeRateApi.API_FOREIGN_EXCHANGE_BASE_URL + "/findByLocalDate")      
        );
        
        
        // Then
        perform
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(status().reason(containsString("No exchange rate data")))
            ;
    }
    
    @Test
    void testFindByLocalDate() throws Exception {
        
        // Given
        List<ExchangeRate> dummyExchangeRateList = new ArrayList<>();
        ExchangeRate dummyData1 = new ExchangeRate();
        dummyData1.setCurrency(AccountCurrency.EURO);
        dummyData1.setRate(BigDecimal.TEN);
        dummyData1.setDate(LocalDate.of(2021,4,24));
        dummyExchangeRateList.add(dummyData1);
        ExchangeRate dummyData2 = new ExchangeRate();
        dummyData2.setCurrency(AccountCurrency.USD);
        dummyData2.setRate(new BigDecimal("8.5"));
        dummyData2.setDate(LocalDate.of(2021,4,24));
        dummyExchangeRateList.add(dummyData2);
        
        Mockito.when(exchangeRateDao.findAllByDate(any()))
            .thenReturn(dummyExchangeRateList);

        Mockito
            .when(exchangeRateDao.existsByDate(any()))
            .thenReturn(true);
        
        String expectedResultStr = "[{\"date\":\"2021-04-24\",\"currency\":\"EURO\",\"rate\":10},{\"date\":\"2021-04-24\",\"currency\":\"USD\",\"rate\":8.5}]";
        
        
        // When
        ResultActions perform = mockMvc.perform(
          get(ExchangeRateApi.API_FOREIGN_EXCHANGE_BASE_URL + "/findByLocalDate")      
        );
        
        
        // Then
        MvcResult mvcResult = perform
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].currency", comparesEqualTo("EURO")))
            .andExpect(jsonPath("$[1].currency", comparesEqualTo("USD")))
            .andReturn()
            ;
        
        String resultStr = mvcResult.getResponse().getContentAsString();
        assertEquals(expectedResultStr, resultStr);
    }

}
