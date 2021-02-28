package net.javaci.bank202101.db.model;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.javaci.bank202101.db.model.enumaration.EmployeeRoleType;
import net.javaci.bank202101.db.model.enumaration.EmployeeStatusType;

@Getter @Setter @ToString
public class Employee extends UserEntityBase {
	
    private EmployeeStatusType status;
    
    private EmployeeRoleType role = EmployeeRoleType.USER;

}
