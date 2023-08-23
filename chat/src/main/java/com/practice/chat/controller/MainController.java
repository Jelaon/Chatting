package com.practice.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	@GetMapping("/")
	public String chat() {
		return "chat";
	}
	
	@GetMapping("/modal")
	public String modal() {
		return "imojiModal";
	}
}
