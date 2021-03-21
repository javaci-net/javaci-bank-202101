package net.javaci.bank202101.api.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import net.javaci.bank202101.db.model.enumaration.AccountCurrency;

@Getter @Setter
public class ExchangeTransactionDto {

    @NotNull
    private Long accountId;
    
    private AccountCurrency currency;
    
    @Min(value = 1)
    private BigDecimal count;
    
}
