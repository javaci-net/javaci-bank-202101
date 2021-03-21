package net.javaci.bank202101.db.dao.impl;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.javaci.bank202101.db.model.ExchangeRate;
import net.javaci.bank202101.db.model.enumaration.AccountCurrency;

@Transactional
@Repository
public interface ExchangeRateDaoImpl extends JpaRepository<ExchangeRate, Long> {

    List<ExchangeRate> findAllByDate(LocalDate exchangeRateDate);
    
    boolean existsByDate(LocalDate date);
    
    boolean existsByDateAndCurrency(LocalDate date, AccountCurrency currency);

    ExchangeRate findByDateAndCurrency(LocalDate date, AccountCurrency currency);

}
