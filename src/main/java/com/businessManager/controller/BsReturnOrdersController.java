package com.businessManager.controller;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.businessManager.repository.RequestOrderRepository;
import com.customer.repository.CommentRepository;
import com.customer.repository.ReturnOrderRepository;
import com.models.Comment;
import com.models.Employee;
import com.models.PageView;
import com.models.Product;
import com.models.Request;
import com.models.Request_detail;
import com.models.ReturnOrder;
import com.models.ReturnOrderDetail;
import com.utils.Views;
import com.warehouseManager.repository.ReleasenoteRepository;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("businessManager/returnorder")
public class BsReturnOrdersController {
    
	@Autowired
	ReturnOrderRepository repreor;
	
	@GetMapping("/showreturnlist")
	public String showreturn(Model model,
	        @RequestParam(name = "cp", required = false, defaultValue = "1") int cp) {
	    try {
	        PageView pv = new PageView();
	        pv.setPage_current(cp);
	        pv.setPage_size(8); 
	        
	        List<ReturnOrder> returnOrders = repreor.findAllProcessingReturnOrders(pv);
	        model.addAttribute("returnOrders", returnOrders);
	        model.addAttribute("pv", pv);
	        
	        return Views.PRORESS_RETURN_ORDER;
	        
	    } catch (Exception e) {
	        return Views.PRORESS_RETURN_ORDER;
	    }
	}
	@GetMapping("/showreturnlistcompletebyempod")
	public String showreturnlistcompletebyempod(Model model,
	        @RequestParam(name = "cp", required = false, defaultValue = "1") int cp,
	        HttpServletRequest request) {
	    try {
	    	Employee emp = (Employee) request.getSession().getAttribute("loggedInEmployee");
	        PageView pv = new PageView();
	        pv.setPage_current(cp);
	        pv.setPage_size(8); 
	        
	        List<ReturnOrder> returnOrders = repreor.findProcessedReturnOrdersByEmployee(pv,emp.getId());
	        model.addAttribute("returnOrders", returnOrders);
	        model.addAttribute("pv", pv);
	        
	        return Views.PRORESS_RETURN_ORDER_COMPLETE_ID;
	        
	    } catch (Exception e) {
	        return Views.PRORESS_RETURN_ORDER_COMPLETE_ID;
	    }
	}
	@GetMapping("/showreturnlistcomplete")
	public String showreturnlistcomplete(Model model,
	        @RequestParam(name = "cp", required = false, defaultValue = "1") int cp) {
	    try {
	    	
	        PageView pv = new PageView();
	        pv.setPage_current(cp);
	        pv.setPage_size(8); 
	        
	        List<ReturnOrder> returnOrders = repreor.findAllProcessedReturnOrders(pv);
	        model.addAttribute("returnOrders", returnOrders);
	        model.addAttribute("pv", pv);
	        
	        return Views.PRORESS_RETURN_ORDER_COMPLETE;
	        
	    } catch (Exception e) {
	        return Views.PRORESS_RETURN_ORDER_COMPLETE;
	    }
	}
    
	@GetMapping("/showdetailor")
	public String showReturnOrderDetail(@RequestParam("id") int returnOrderId, Model model) {
	    try {
	        // Lấy thông tin ReturnOrder
	        ReturnOrder returnOrder = repreor.findReturnOrderById(returnOrderId);
	        
	        if (returnOrder != null) {
	            // Lấy danh sách ReturnOrderDetail
	            List<ReturnOrderDetail> returnDetails = repreor.findReturnOrderDetailsByReturnOrderId(returnOrderId);
	            returnOrder.setReturnDetails(returnDetails);
	            
	            // Add vào model
	            model.addAttribute("returnOrder", returnOrder);
	            return Views.RETURN_BS_ORDER_DETAIL;
	        }
	        
	        // Nếu không tìm thấy, redirect về list
	        return "redirect:/businessManager/returnorder/showreturnlist";
	        
	    } catch (Exception e) {
	        System.err.println("Error in showReturnOrderDetail: " + e.getMessage());
	        return "redirect:/businessManager/returnorder/showreturnlist";
	    }
	}
	@PostMapping("/reject")
	@ResponseBody
	public Map<String, Object> rejectReturnOrder(@RequestParam("id") int returnOrderId,
	                                           @RequestParam("message") String message,
	                                           HttpServletRequest request) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	    	Employee emp = (Employee) request.getSession().getAttribute("loggedInEmployee");
	        boolean updated = repreor.updateStatusAndMessage(returnOrderId, "Rejected", message,emp.getId());
	        response.put("success", updated);
	        if (updated) {
	            
	            response.put("redirectUrl", "/businessManager/returnorder/showdetailor?id=" + returnOrderId);
	        } else {
	            response.put("message", "Failed to update return order status");
	        }
	    } catch (Exception e) {
	        response.put("success", false);
	        response.put("message", "Error: " + e.getMessage());
	    }
	    return response;
	}

	@PostMapping("/accept")
	@ResponseBody
	public Map<String, Object> acceptReturnOrder(@RequestParam("id") int returnOrderId,
	                                          @RequestParam("message") String message,
	                                          HttpServletRequest request) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	    	Employee emp = (Employee) request.getSession().getAttribute("loggedInEmployee");
	        boolean updated = repreor.updateStatusAndMessage(returnOrderId, "Accepted", message,emp.getId());
	        response.put("success", updated);
	        if (updated) {
	           
	            response.put("redirectUrl", "/businessManager/returnorder/showdetailor?id=" + returnOrderId);
	        } else {
	            response.put("message", "Failed to update return order status");
	        }
	    } catch (Exception e) {
	        response.put("success", false);
	        response.put("message", "Error: " + e.getMessage());
	    }
	    return response;
	}
	@GetMapping("/getReturnDetails")
	@ResponseBody
	public ReturnOrder getReturnDetails(@RequestParam("id") int id) {
	    ReturnOrder returnOrder = repreor.findReturnOrderById(id);
	    // Load return details if needed
	    List<ReturnOrderDetail> details = repreor.findReturnOrderDetailsByReturnOrderId(returnOrder.getId());
	    
	    returnOrder.setReturnDetails(details);
	    return returnOrder;
	}
}
