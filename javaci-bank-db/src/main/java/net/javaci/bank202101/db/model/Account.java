package net.javaci.bank202101.db.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.javaci.bank202101.db.model.enumaration.AccountCurrency;
import net.javaci.bank202101.db.model.enumaration.AccountStatusType;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Account {

    private Long id;
    
    // ManyToOne
    private Customer customer;
    
    private String accountNumber;
    
    private String accountName;
    
    private String description;
    
    private BigDecimal balance;
    
    // @Enumarated
    private AccountCurrency currency;
    
    // @Enumarated
    private AccountStatusType status;
}
