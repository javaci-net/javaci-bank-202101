package net.javaci.bank202101.api.helper;

public class AccountNumberGenerator {

    public static String generateAccountNumber(Long customerId, int existingAccountCount) {
        return String.format("%07d-%02d", customerId, existingAccountCount+1 );
    }
    
}
