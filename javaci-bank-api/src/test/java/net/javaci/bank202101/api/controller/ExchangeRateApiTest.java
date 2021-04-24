package net.javaci.bank202101.api.controller;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    void testFindByLocalDate() throws Exception {
        
        // Given
        List<ExchangeRate> dummyExhangeRateList = new ArrayList<>();
        ExchangeRate dummyData1 = new ExchangeRate();
        dummyData1.setCurrency(AccountCurrency.EURO);
        dummyData1.setRate(BigDecimal.TEN);
        dummyExhangeRateList.add(dummyData1);
        Mockito.when(exchangeRateDao.findAllByDate(any())).thenReturn(dummyExhangeRateList);
        Mockito.when(exchangeRateDao.existsByDate(any())).thenReturn(true);
        
        // When
        ResultActions perform =  this.mockMvc.perform(
                get(ExchangeRateApi.API_FOREIGN_EXCHANGE_BASE_URL + "/findByLocalDate")
        );
        
        // Then
        perform
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].currency", comparesEqualTo("EURO")))
            .andExpect(jsonPath("$[0].rate", comparesEqualTo(10)))
        ;
    }

}
