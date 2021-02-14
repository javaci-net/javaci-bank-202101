package net.javaci.bank202101.backoffice.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

import net.javaci.bank202101.backoffice.dto.EmployeeDto;
import net.javaci.bank202101.db.dao.EmployeeDao;
import net.javaci.bank202101.db.model.Employee;

@Controller
@RequestMapping("/employee")
public class EmployeeController {
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping("/add")
	public String renderAddPage(Model model) {
		EmployeeDto employeeDto = new EmployeeDto();
		
		employeeDto.setCitizenNumber("1234");
		
		model.addAttribute("employeeDto", employeeDto);
		
		return "employee/add";
	}
	
	@PostMapping("/add")
	public String addEmployee(@ModelAttribute EmployeeDto employeeDto) {
		
		Employee employee = new Employee();
		modelMapper.map(employeeDto, employee);
		
		employeeDao.add(employee);
		
		return "redirect:/employee/list";
	}
	
	@GetMapping("/list")
	public String renderListPage(Model model, HttpServletRequest request) {
		model.addAttribute("employees", employeeDao.findAll());
		
		Locale locale = RequestContextUtils.getLocale(request);
		
		if (locale.equals(Locale.ENGLISH)) {
			model.addAttribute("date_format", "MM-dd-yyyy");
		} else {
			model.addAttribute("date_format", "dd-MM-yyyy");
		}
		return "employee/list";
	}
	
}
