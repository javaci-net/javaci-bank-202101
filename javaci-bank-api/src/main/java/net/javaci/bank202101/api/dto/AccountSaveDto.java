package net.javaci.bank202101.api.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AccountSaveDto {

    @NotEmpty
    private String accountName;
    
    private String description;
    
    private BigDecimal balance;
    
    @NotEmpty
    private String currency;
    
    @NotEmpty
    private String status;
}
