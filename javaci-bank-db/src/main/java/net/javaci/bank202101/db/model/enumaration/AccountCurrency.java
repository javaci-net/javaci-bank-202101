package net.javaci.bank202101.db.model.enumaration;

public enum AccountCurrency {

    TL("TRY"),
    USD("USD"),
    EURO("EUR"),
    GOLD("GOLD");
    
    public final String code;
    
    AccountCurrency(String code) {
        this.code = code;
    }
}
