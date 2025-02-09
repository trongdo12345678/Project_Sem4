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
import com.models.Comment;
import com.models.Employee;
import com.models.PageView;
import com.models.Product;
import com.models.Request;
import com.models.Request_detail;
import com.utils.Views;
import com.warehouseManager.repository.ReleasenoteRepository;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("businessManager/comment")
public class BsCommentController {
    
	@Autowired
	CommentRepository repcm;
	@GetMapping("/showcomment")
	public String showComments(Model model,
	        @RequestParam(name = "cp", required = false, defaultValue = "1") int cp) {
	    try {
	        // Khởi tạo PageView
	        PageView pv = new PageView();
	        pv.setPage_current(cp);
	        pv.setPage_size(8); 
	        
	        List<Comment> comments = repcm.getCommentsBypage(pv);
	        
	        
	        model.addAttribute("comments", comments);
	        model.addAttribute("pv", pv);
	        
	        return Views.REPLY_COMMENT;
	        
	    } catch (Exception e) {
	       
	        System.err.println("Error in showComments: " + e.getMessage());
	       
	       
	        return Views.REPLY_COMMENT;
	    }
	}
    
	@PostMapping("/staffreply")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> staffReply(@RequestBody Comment comment, HttpServletRequest request) {
	    try {
	        
	        Employee employee = (Employee) request.getSession().getAttribute("loggedInEmployee");
	        if (employee == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	        }

	        // Set các giá trị cần thiết cho comment
	        comment.setEmployeeId(employee.getId());
	        comment.setStatus("APPROVED"); 
	        comment.setCustomerId(0);
	        
	        // Insert comment
	        Comment insertedReply = repcm.insertComment(comment);
	        
	        if (insertedReply != null) {
	            Map<String, Object> response = new HashMap<>();
	            response.put("commentId", insertedReply.getId());
	            response.put("content", insertedReply.getContent());
	            response.put("employeeName", insertedReply.getEmployeeFirstName() + " " + insertedReply.getEmployeeLastName());
	            response.put("employeeId", insertedReply.getEmployeeId());
	            
	            // Format LocalDate
	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	            response.put("createdAt", insertedReply.getCreatedAt().format(formatter));
	            
	            return ResponseEntity.ok(response);
	        } else {
	            return ResponseEntity.badRequest().build();
	        }
	        
	    } catch (Exception e) {
	        System.err.println("Error in staffReply: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}



}
