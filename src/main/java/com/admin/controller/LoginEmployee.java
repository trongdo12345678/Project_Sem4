package com.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.admin.repository.LoginEmploy;
import com.models.Employee;
import com.utils.Views;
import com.warehouseManager.repository.ReleasenoteRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("employee")
public class LoginEmployee {
	@Autowired
	private LoginEmploy emprep;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String showLoginPage() {
	    return Views.EMPLOYEE_LOGIN; 
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam("uid") String uid, 
	                    @RequestParam("pwd") String pwd, 
	                    HttpSession session, 
	                    Model model) {
	    Employee emp = emprep.login(uid, pwd);
	    if (emp != null) {
	        if (emp.getRole_id() == 2) {
	            Integer warehouseId = emprep.findWarehouseIdByEmployeeId(emp.getId());
	            if (warehouseId == null) {
	                model.addAttribute("error", "Employee is not assigned to any warehouse");
	                return Views.EMPLOYEE_LOGIN; 
	            }
	            session.setAttribute("warehouseId", warehouseId);
	        }
	        
	        session.setAttribute("loggedInEmployee", emp);
	        
	        switch (emp.getRole_id()) {
	            case 1: 
	                return "redirect:/admin/employee/showEmp";
	            case 2: 
	                return "redirect:/warehouseManager/warehouseReceipt/showWhReceipt?warehouseId=" + session.getAttribute("warehouseId");
	            case 3: 
	                return "redirect:/businessManager/showOrderRequest";
	            default: 
	                return "redirect:/login?error=role";
	        }
	    } else {
	        model.addAttribute("error", "Wrong login information");
	        return Views.EMPLOYEE_LOGIN; 
	    }
	}
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
	    session.invalidate();
	    return "redirect:login";
	}
}
