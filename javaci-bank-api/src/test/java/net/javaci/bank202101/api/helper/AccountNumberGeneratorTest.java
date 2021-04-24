package net.javaci.bank202101.api.helper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class AccountNumberGeneratorTest {

    @Test
    void testGenerateAccountNumber() {
        
        // Given
        Long customerId = 65432L;
        int existingAccountCount = 5;
        
        // When
        String actual = AccountNumberGenerator.generateAccountNumber(customerId, existingAccountCount);
        
        // Then
        assertEquals("0065432-06", actual);
    }
    
    @Test
    void testGenerateAccountNumberWithNullCustomerId() {
        
        // Given
        Long customerId = null;
        int existingAccountCount = 5;
        
        // When
        Executable executable = () -> {
            AccountNumberGenerator.generateAccountNumber(customerId, existingAccountCount);
        };
        
        // Then
        RuntimeException exception = assertThrows(RuntimeException.class, executable); 
        assertEquals("Customer Id cannot be null", exception.getMessage());
    }

}
