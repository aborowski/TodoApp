package com.borowski.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller()
public class TestController {
	
	@RequestMapping("home")
	public String showPage(String test, String test2, HttpSession session) {
		session.setAttribute("test", test);
		session.setAttribute("test2", test2);
		return "home";
	}
}
