package com.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.customer.repository.EmailService;
import com.utils.Views;

import jakarta.mail.MessagingException;

@Controller
@RequestMapping("contact")
public class ContactController {

	@Autowired
	EmailService emailservice;

	@GetMapping("")
	public String showpage() {

		return Views.CUS_CONTACTPAGE;
	}

	@PostMapping("sendc")
	public String sendc(@RequestParam("name") String name, @RequestParam("email") String email,
			@RequestParam("message") String message) {

		try {
			emailservice.sendcontact(name, email, message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return Views.CUS_CONTACTPAGE;
	}

}
