package net.javaci.bank202101.db.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import net.javaci.bank202101.db.dao.TransactionLogDao;
import net.javaci.bank202101.db.model.TransactionLog;

@Slf4j
@Transactional
@Repository
public class TransactionLogDaoImpl implements TransactionLogDao {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public List<TransactionLog> findByAccountId(Long accountId) {
        List<TransactionLog> resultList = entityManager
                .createQuery("from TransactionLog as txn where txn.account.id = ?1")
                .setParameter(1, accountId)
                .getResultList();
        return resultList;
    }

    @Override
    public void save(TransactionLog entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity);
        } else {
            entityManager.merge(entity);
            entityManager.flush();
        }
    }

}
