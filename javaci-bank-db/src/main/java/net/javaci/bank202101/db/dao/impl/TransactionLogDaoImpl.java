package net.javaci.bank202101.db.dao.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.javaci.bank202101.db.model.Account;
import net.javaci.bank202101.db.model.TransactionLog;

@Transactional
@Repository
public interface TransactionLogDaoImpl extends JpaRepository<TransactionLog, Long> {

    List<TransactionLog> findAllByAccount(Account account);
    
    @Query("from TransactionLog as txn where txn.account.id = ?1")
    List<TransactionLog> findAllByAccountId(Long accountId);
}
