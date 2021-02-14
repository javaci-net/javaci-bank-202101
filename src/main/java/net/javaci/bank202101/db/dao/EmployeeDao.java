package net.javaci.bank202101.db.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

import org.springframework.stereotype.Component;

import net.javaci.bank202101.db.model.Employee;

@Component
public class EmployeeDao {

	private List<Employee> db = new ArrayList<Employee>();

	public void add(Employee employee) {
		/*
		OptionalLong maxOption = db.stream().mapToLong(e -> e.getId()).max();
		long id;
		
		if (maxOption.isEmpty()) {
			id = 1;
		} else {
			id = maxOption.getAsLong() + 1;
		}
		*/
		employee.setId(1L);
		
		db.add(employee);
	}

	public List<Employee>  findAll() {
		return db;
	}

}
