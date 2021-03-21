package net.javaci.bank202101.api.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import net.javaci.bank202101.api.dto.ExchangeRateDto;
import net.javaci.bank202101.db.dao.impl.ExchangeRateDaoImpl;
import net.javaci.bank202101.db.model.ExchangeRate;

@RestController
@RequestMapping(ExchangeRateApi.API_FOREIGN_EXCHANGE_BASE_URL)
public class ExchangeRateApi {

    static final String API_FOREIGN_EXCHANGE_BASE_URL = "/api/foreign-exchange";
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private ExchangeRateDaoImpl exchangeRateDao;
    
    @GetMapping("findByLocalDate")
    public List<ExchangeRateDto> findByLocalDate(
            @RequestParam(required = false, defaultValue = "") String dateStr){
        
        LocalDate exchangeRateDate = null;
        if(StringUtils.isEmpty(dateStr)) {
            exchangeRateDate = LocalDate.now();
        } else {
            exchangeRateDate = LocalDate.parse(dateStr);
        }
        
        if(!exchangeRateDao.existsByDate(exchangeRateDate)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    "No exchange rate data for " + exchangeRateDate);
        }
        
        List<ExchangeRateDto> resultList = new ArrayList<>();
        List<ExchangeRate> findAllByDate = exchangeRateDao.findAllByDate(exchangeRateDate);
        for (ExchangeRate exchangeRate : findAllByDate) {
            resultList.add(modelMapper.map(exchangeRate, ExchangeRateDto.class));
        }
        
        return resultList;
    }

}
