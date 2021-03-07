package net.javaci.bank202101.api.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TransactionLogDto {

    private Long id;
    
    private String toCustomerName;
    
    private Long toAccountId;
    
    private BigDecimal amount;
    
    private String type;
    
    private String description;
}
