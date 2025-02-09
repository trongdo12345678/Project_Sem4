package com.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.customer.repository.AccountRepository;

import com.customer.repository.EmailService;
import com.models.Customer;

import com.utils.Views;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("account")
public class AccountController {
	@Autowired
	EmailService emailService;

	@Autowired
	AccountRepository rep;

	@GetMapping("/signin")
	public String showpagesignin(Model model) {

		return Views.CUS_SIGNINPAGE;
	}

	@GetMapping("/signup")
	public String showpagesignup(Model model) {
		
		return Views.CUS_SIGNUPPAGE;
	}

	@PostMapping("/goregister")
	public String gosent(@RequestParam("email") String email, @RequestParam("password") String password, Model model,
			HttpServletRequest request) {

		String token = EmailService.generateToken();

		request.getSession().setAttribute("emailregister", email);
		request.getSession().setAttribute("passwordregister", password);
		request.getSession().setAttribute("tokenregister", token);
		model.addAttribute("email", email);
		
		String confirmationLink = "http://localhost:8080/account/confirm?email=" + email + "&token=" + token;

		
		try {
			emailService.sendConfirmationEmail(email, confirmationLink);
		} catch (MessagingException e) {
			
			e.printStackTrace();
		}
		return Views.CUS_COFIRMPAGE;
	}

	@GetMapping("/confirm")
	public String confirmRegistration(@RequestParam("email") String email, @RequestParam("token") String token,
			HttpServletRequest request, Model model) {
		
		String sessionToken = (String) request.getSession().getAttribute("tokenregister");
		String sessionEmail = (String) request.getSession().getAttribute("emailregister");
		String sessionPw = (String) request.getSession().getAttribute("passwordregister");
		
		if (sessionEmail != null && sessionEmail.equals(email) && sessionToken != null && sessionToken.equals(token)) {
			Customer newcus = new Customer();
			newcus.setEmail(sessionEmail);
			newcus.setPassword((String) request.getSession().getAttribute("passwordregister"));

			rep.createAccount(newcus);
			
			Customer cus = rep.login(email, sessionPw);
			
			request.getSession().removeAttribute("emailregister");
			request.getSession().removeAttribute("passwordregister");
			request.getSession().removeAttribute("tokenregister");
			request.getSession().setAttribute("logined", cus.getId());

			return "redirect:/account/cusinfo";
		} else {

			System.out.println("Invalid confirmation token or email for email: " + email);
			model.addAttribute("error", "Invalid confirmation token or email.");
			return "redirect:/account/signup";
		}
	}

	@GetMapping("/checkemail")
	@ResponseBody
	public boolean checkEmail(@RequestParam("email") String email) {
		return rep.isEmailRegistered(email);
	}

	@PostMapping("/checklogin")
	@ResponseBody
	public ResponseEntity<Boolean> checklogin(@RequestParam("email") String email,
			@RequestParam("password") String password, HttpServletRequest request) {
		Customer customer = rep.login(email, password);
		if (customer != null) {
			request.getSession().setAttribute("logined", customer.getId());
		}
		boolean success = customer != null;
		return ResponseEntity.ok(success);
	}

	@GetMapping("/cusinfo")
	public String showpagecusinfo(Model model, HttpServletRequest request) {
		Customer cus = rep.finbyid((int) request.getSession().getAttribute("logined"));
		model.addAttribute("cusinfo", cus);

		return Views.CUS_CUSINFOPAGE;
	}

	@PostMapping("/saveprofile")
	public String saveprofile(@ModelAttribute Customer cusinfo, HttpServletRequest request) {
		rep.updateAccount(cusinfo);
		return "redirect:/account/cusinfo";
	}

	@GetMapping("/gochangepassword")
	public String showpagechangepassword(Model model, HttpServletRequest request) {

		model.addAttribute("cusid", (int) request.getSession().getAttribute("logined"));

		return Views.CUS_CUSCHANGEPASSWORDPAGE;
	}

	@GetMapping("/validateCurrentPassword")
	@ResponseBody
	public boolean checcurrentpw(@RequestParam("password") String password, HttpServletRequest request) {

		int customerId = (int) request.getSession().getAttribute("logined");


		return rep.verifyPassword(customerId, password);
	}

	@PostMapping("/changepassword")
	public String savenewpw(@RequestParam("newPassword") String npw, HttpServletRequest request) {
		rep.updatePassword((int) request.getSession().getAttribute("logined"), npw);
		return "redirect:/account/cusinfo";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		request.getSession().setAttribute("logined", null);
		return "redirect:/";
	}
}
