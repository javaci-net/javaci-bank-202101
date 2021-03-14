package net.javaci.bank202101.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.javaci.bank202101.db.model.enumaration.CustomerStatusType;

@Entity
@Getter @Setter @NoArgsConstructor
public class Customer extends UserEntityBase {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerStatusType status;

   // private List<Account> accounts = new ArrayList<>();

}
