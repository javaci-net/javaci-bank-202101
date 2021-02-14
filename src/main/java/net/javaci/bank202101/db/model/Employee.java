package net.javaci.bank202101.db.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.javaci.bank202101.db.model.enumaration.EmployeeRoleType;
import net.javaci.bank202101.db.model.enumaration.EmployeeStatusType;

@Getter @Setter @ToString
public class Employee {
	
	private Long id;

    private String citizenNumber;
    
    private String firstName;
    
    private String middleName;

    private String lastName;    
    
    private String email;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    
    private String phoneNumber;

    private String password;
    
    private EmployeeStatusType status;
    
    private EmployeeRoleType role = EmployeeRoleType.USER;

}
