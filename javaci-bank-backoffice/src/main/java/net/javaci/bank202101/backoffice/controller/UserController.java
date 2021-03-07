package net.javaci.bank202101.backoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

	@GetMapping("/profile")
	public String renderProfilePage() {
		// TODO
		return "employee/update";
	}
	
	@GetMapping("/changePassword")
	public String renderChangePasswordPage() {
		// TODO
		return "user/changePassword";
	}
	
	@GetMapping("/tasks")
	public String renderTasksPage() {
		// TODO
		return "user/tasks";
	}
}
