package net.javaci.bank202101.db.dao.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import net.javaci.bank202101.db.dao.CustomerDao;
import net.javaci.bank202101.db.model.Customer;

@Slf4j
@Transactional
@Repository
public class CustomerDaoImpl implements CustomerDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Customer> findByCitizenNumber(String citizenNumber) {
        
        List<Customer> resultList = entityManager
            .createQuery("from Customer as c WHERE c.citizenNumber = ?1")
            .setParameter(1, citizenNumber)
            .getResultList();
        
        if (resultList.isEmpty()) {
            return Optional.empty();
        }
        
        if (resultList.size() > 1) {
            log.error("Customer by citizen number query returned more than 1 result: {} result", resultList.size());
        }
        
        return Optional.of(resultList.get(0));
    }

    @Override
    public void save(Customer customer) {    
        if (customer.getId() == null) {
            entityManager.persist(customer);
        } else {
            entityManager.merge(customer);
            entityManager.flush();
        }
    }
    
}
