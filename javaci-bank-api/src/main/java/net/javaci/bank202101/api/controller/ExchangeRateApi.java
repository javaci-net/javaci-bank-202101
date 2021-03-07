package net.javaci.bank202101.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.javaci.bank202101.api.dto.ExchangeRateDto;

@RestController
@RequestMapping(ExchangeRateApi.API_FOREIGN_EXCHANGE_BASE_URL)
public class ExchangeRateApi {

    static final String API_FOREIGN_EXCHANGE_BASE_URL = "/api/foreign-exchange";
    
    @GetMapping("findByLocalDate")
    public List<ExchangeRateDto> findByLocalDate(
            @RequestParam(required = false, defaultValue = "") String dateStr){
        return null;
    }

}
