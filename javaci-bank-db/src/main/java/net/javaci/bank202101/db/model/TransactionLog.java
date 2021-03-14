package net.javaci.bank202101.db.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import net.javaci.bank202101.db.model.enumaration.TransactionLogType;

@Entity
@Getter @Setter
public class TransactionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    private Account account;
    
    @ManyToOne
    private Account toAccount;
    
    @Column(nullable = false)
    private BigDecimal amount;
    
    @Column(nullable = false)
    private BigDecimal balance;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionLogType type;
    
    @Column(nullable = false)
    private LocalDate date;
    
    private String description;
    
}
