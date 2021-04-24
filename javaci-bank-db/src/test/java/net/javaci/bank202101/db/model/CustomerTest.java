package net.javaci.bank202101.db.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class CustomerTest {

    // TODO Test hashCode() method
    
    @Test
    @DisplayName("Test Equals with Objects Having Same Citizen Numbers but Different Other Field Values")
    public void testEqualsWithOnlySameCitizenNumber() {
        
        // Given
        Customer customer1 = new Customer();
        customer1.setCitizenNumber("1234567890123");
        customer1.setId(1L);
        customer1.setBirthDate(LocalDate.of(2011, 10, 21));
        
        Customer customer2 = new Customer();
        customer2.setCitizenNumber("1234567890123");
        customer2.setId(2L);
        customer2.setBirthDate(LocalDate.of(2001, 10, 21));
        
        // When
        boolean actual = customer1.equals(customer2);
        
        // Then
        assertEquals(true, actual);
   
    }
    
    @Test
    @DisplayName("Test Equals with Objects Having Different Citizen Numbers and Different Other Field Values")
    public void testEqualsWithDifferentCitizenNumber() {
        
        // Given
        Customer customer1 = new Customer();
        customer1.setCitizenNumber("9874567890123");
        customer1.setId(1L);
        customer1.setBirthDate(LocalDate.of(2011, 10, 21));
        
        Customer customer2 = new Customer();
        customer2.setCitizenNumber("1234567890123");
        customer2.setId(2L);
        customer2.setBirthDate(LocalDate.of(2001, 10, 21));
        
        // When
        boolean actual = customer1.equals(customer2);
        
        // Then
        assertEquals(false, actual);
    }
    
    @Test
    @DisplayName("Test Equals with Objects Having Null Citizen Numbers and Different Other Field Values")
    public void testEqualsWithNullCitizenNumber() {
        
        // Given
        Customer customer1 = new Customer();
        customer1.setCitizenNumber(null);
        customer1.setId(1L);
        customer1.setBirthDate(LocalDate.of(2011, 10, 21));
        
        Customer customer2 = new Customer();
        customer2.setCitizenNumber(null);
        customer2.setId(2L);
        customer2.setBirthDate(LocalDate.of(2001, 10, 21));
        
        // When
        boolean actual = customer1.equals(customer2);
        
        // Then
        assertEquals(true, actual);
    }
    
    @Test
    @DisplayName("Test Equals with Only one Object Having Null Citizen Numbers and Different Other Field Values")
    public void testEqualsWithOneNullCitizenNumber() {
        
        // Given
        Customer customer1 = new Customer();
        customer1.setCitizenNumber(null);
        customer1.setId(1L);
        customer1.setBirthDate(LocalDate.of(2011, 10, 21));
        
        Customer customer2 = new Customer();
        customer2.setCitizenNumber("12321133424323424");
        customer2.setId(2L);
        customer2.setBirthDate(LocalDate.of(2001, 10, 21));
        
        // When
        boolean actual = customer1.equals(customer2);
        
        // Then
        assertEquals(false, actual);
    }
}
