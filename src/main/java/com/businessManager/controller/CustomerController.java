package com.businessManager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.businessManager.repository.CustomerRepository;
import com.models.Customer;
import com.models.Employee;
import com.models.PageView;
import com.models.Order;
import com.models.Order_detail;
import com.utils.Views;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("businessManager")
public class CustomerController {

	   @Autowired
	    private CustomerRepository cus;
	   
	   @GetMapping("/showCustomer")
	   public String showCustomer(Model model, HttpSession session,
	           @RequestParam(name = "cp", required = false, defaultValue = "1") int cp) {
	       PageView pv = new PageView();
	       pv.setPage_current(cp);
	       pv.setPage_size(8);
	       Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");

	       if (loggedInEmployee != null) {
	           List<Customer> customers = cus.findAllCustomers(pv);
	           model.addAttribute("customers", customers);
	           model.addAttribute("pv", pv);
	       } else {
	           return "redirect:/employee/login";  
	       }

	       return Views.SHOW_CUSTOMER_BUSINESS;
	   }
	   
	   @GetMapping("/cusOrderDetail") 
	   public String cusOrderDetail(@RequestParam("id") int id, Model model) {  
	       List<Order> orders = cus.findCusOrder(id);	       	      

	       model.addAttribute("orders", orders);
	       
	       return Views.SHOW_CUSTOMER_ORDER_BUSINESS; 
	   }
	   @GetMapping("/getOrderDetails")
	   @ResponseBody
	   public List<Order_detail> getOrderDetails(@RequestParam("orderId") int orderId) {
	       return cus.findCusOrderdetail(orderId); 
	   }


}
