package net.javaci.bank202101.api.helper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AccountNumberGeneratorTest {

    @Test
    public void testGenerateAccountNumber() {
        String accountNumber = AccountNumberGenerator.generateAccountNumber(1234L, 12);
        assertEquals("0001234-13", accountNumber);
    }

}
