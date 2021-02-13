package net.javaci.bank202101.backoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

	@GetMapping({"/", "index", "index.html"})
	public String renderTemp() {
		/*
		if (true) {
			throw new RuntimeException();
		}
		*/
		return "dashboard";
	}
	
	
	@GetMapping("page1")
	public String renderpage1() {
		return "page1";
	}
	
	@GetMapping("page2")
	public String renderpage2() {
		return "page2";
	}
}
