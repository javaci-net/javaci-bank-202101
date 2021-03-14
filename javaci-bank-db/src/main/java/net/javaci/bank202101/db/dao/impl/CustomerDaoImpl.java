package net.javaci.bank202101.db.dao.impl;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import net.javaci.bank202101.db.dao.CustomerDao;
import net.javaci.bank202101.db.model.Customer;

@Transactional
@Repository
public class CustomerDaoImpl implements CustomerDao {

    private EntityManager entityManager;

    @Override
    public Optional<Customer> findByCitizenNumber(String citizenNumber) {
        
        // entityManager.createQuery("from Customer as c WHERE ")
        
        return null;
    }
    
}
