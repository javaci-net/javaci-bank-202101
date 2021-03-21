package net.javaci.bank202101.db.dao.impl;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import net.javaci.bank202101.db.dao.AccountDao;
import net.javaci.bank202101.db.model.Account;

@Slf4j
@Transactional
@Repository
public class AccountDaoImp implements AccountDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int countByCustomerId(Long customerId) {
        return entityManager
            .createQuery("from Account as acc where acc.customer.id = ?1")
            .setParameter(1, customerId)
            .getResultList().size();
    }
   

    @Override
    public void save(Account entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity);
        } else {
            entityManager.merge(entity);
            entityManager.flush();
        }
    }

    @Override
    public Optional<Account> findById(Long accountId) {
        Account dbAccount = entityManager.find(Account.class, accountId);
        return Optional.ofNullable(dbAccount);
    }


    @Override
    public Account findByAccountNumber(String toAccountNumber) {
        return (Account) entityManager
            .createQuery("from Account as acc where acc.accountNumber = ?1")
            .setParameter(1, toAccountNumber)
            .getSingleResult();
    }
}
