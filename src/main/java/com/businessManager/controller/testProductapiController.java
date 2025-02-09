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
import org.springframework.web.bind.annotation.RestController;

import com.businessManager.repository.RequestOrderRepository;
import com.customer.repository.CommentRepository;
import com.customer.repository.ShoppingpageRepository;
import com.models.Comment;
import com.models.Employee;
import com.models.PageView;
import com.models.Product;
import com.models.Request;
import com.models.Request_detail;
import com.utils.Views;
import com.warehouseManager.repository.ReleasenoteRepository;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/product")
public class testProductapiController {
    @Autowired
    ShoppingpageRepository rep;
	@GetMapping("/takeproduct")
	public ResponseEntity<List<Product>> takeproduct() {
		
		 try {
			 int[] idCategories = {}; 
			    int[] idBrands = {}; 
			    String[] statusesnew = {"NewRelease"};
			    PageView pv = new PageView();
			    pv.setPage_current(1);
			    List<Product> products = rep.findAllnopaging(new PageView(), "", idCategories, idBrands, statusesnew);
	            return new ResponseEntity<List<Product>>(products,HttpStatus.OK);
	        } catch (Exception e) {
	            return new ResponseEntity<List<Product>>(HttpStatus.BAD_REQUEST);
	        }
	}
	@GetMapping("/takedemo")
	public ResponseEntity<String> takedemo() {	
		 try {

	            return new ResponseEntity<String>("gaga",HttpStatus.OK);
	        } catch (Exception e) {
	            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	        }
	    
	   
	}



}
