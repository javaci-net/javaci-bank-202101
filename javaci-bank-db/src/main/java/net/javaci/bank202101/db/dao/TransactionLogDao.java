package net.javaci.bank202101.db.dao;

import java.util.List;

import net.javaci.bank202101.db.model.TransactionLog;

public interface TransactionLogDao {

    List<TransactionLog> findByAccountId(Long accountId);

    void save(TransactionLog transactionLog);

}
