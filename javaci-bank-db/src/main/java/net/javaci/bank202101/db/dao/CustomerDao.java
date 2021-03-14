package net.javaci.bank202101.db.dao;

import java.util.Optional;

import net.javaci.bank202101.db.model.Customer;

public interface CustomerDao {

    Optional<Customer> findByCitizenNumber(String citizenNumber);

}
