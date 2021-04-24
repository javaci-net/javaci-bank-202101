package net.javaci.bank202101.db.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CustomerTest {

    @Test
    void testHashCodeForCustomerWithSameIdAndSameCitizenNumber() {

        Customer cust1 = new Customer();
        cust1.setId(1L);
        cust1.setCitizenNumber("12345678901");
        
        Customer cust2 = new Customer();
        cust2.setId(1L);
        cust2.setCitizenNumber("12345678901");
        
        assertEquals(cust1.hashCode(), cust2.hashCode());
    }
    
    @Test
    void testHashCodeForCustomerWithSameIdAndDifferentCitizenNumber() {
        Customer cust1 = new Customer();
        cust1.setId(1L);
        cust1.setCitizenNumber("12345678901");
        
        Customer cust2 = new Customer();
        cust2.setId(1L);
        cust2.setCitizenNumber("10987654321");
        
        assertNotEquals(cust1.hashCode(), cust2.hashCode());
    }

    @Test
    void testEqualsWithSameIdAndSameCitizenNumber() {
        Customer cust1 = new Customer();
        cust1.setId(1L);
        cust1.setCitizenNumber("12345678901");
        
        Customer cust2 = new Customer();
        cust2.setId(1L);
        cust2.setCitizenNumber("12345678901");
        assertTrue(cust1.equals(cust2));
    }
    
    @Test
    void testEqualsWithSameIdAndDifferentCitizenNumber() {
        Customer cust1 = new Customer();
        cust1.setId(1L);
        cust1.setCitizenNumber("12345678901");
        
        Customer cust2 = new Customer();
        cust2.setId(1L);
        cust2.setCitizenNumber("10987654321");
        assertFalse(cust1.equals(cust2));
    }

}
