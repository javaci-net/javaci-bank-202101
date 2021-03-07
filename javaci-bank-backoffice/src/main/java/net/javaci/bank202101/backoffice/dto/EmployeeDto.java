package net.javaci.bank202101.backoffice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.javaci.bank202101.db.model.Employee;

@Getter @Setter @ToString
public class EmployeeDto extends Employee {
    
    private String confirmPassword;
	
}
