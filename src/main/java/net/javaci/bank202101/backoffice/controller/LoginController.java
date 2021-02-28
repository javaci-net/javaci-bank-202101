package net.javaci.bank202101.backoffice.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	
	@GetMapping("/giris")
	public String  girisPage(Model model, Principal user) {
		if (user != null) {
			return "redirect:/";
		}
		return "login";
	}
	
}
