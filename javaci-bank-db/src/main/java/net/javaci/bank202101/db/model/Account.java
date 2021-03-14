package net.javaci.bank202101.db.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.javaci.bank202101.db.model.enumaration.AccountCurrency;
import net.javaci.bank202101.db.model.enumaration.AccountStatusType;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    private Customer customer;
    
    @Column(nullable = false)
    private String accountNumber;
    
    @Column(nullable = false)
    private String accountName;
    
    private String description;
    
    @Column(nullable = false)
    private BigDecimal balance;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountCurrency currency;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatusType status;
}
