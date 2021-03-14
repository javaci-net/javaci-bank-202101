package net.javaci.bank202101.db.model.enumaration;

public enum CustomerStatusType {

    ACTIVE,
    INACTIVE,
    UNAPPROVED;

    public static CustomerStatusType fromStr(String status) {
        return CustomerStatusType.valueOf(status.toUpperCase());
    }
}