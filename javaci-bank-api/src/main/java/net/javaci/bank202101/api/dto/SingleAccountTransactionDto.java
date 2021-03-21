package net.javaci.bank202101.api.dto;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SingleAccountTransactionDto {

    @NotNull
    private Long accountId;
    
    @NotNull
    @DecimalMin(value = "0.01", inclusive = false)
    private BigDecimal amount;
    
}
