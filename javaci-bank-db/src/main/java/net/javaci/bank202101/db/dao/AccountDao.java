package net.javaci.bank202101.db.dao;

import java.util.Optional;

import net.javaci.bank202101.db.model.Account;

public interface AccountDao {

    int countByCustomerId(Long customerId);

    void save(Account entity);

    Optional<Account> findById(Long accountId);

    Account findByAccountNumber(String toAccountNumber);
  
}
