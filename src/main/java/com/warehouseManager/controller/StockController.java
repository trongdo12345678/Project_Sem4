package com.warehouseManager.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.models.Employee;
import com.models.Stock;
import com.models.StockSumByWarehouseId;
import com.utils.Views;
import com.warehouseManager.repository.StockRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("warehouseManager")
public class StockController {
	@Autowired
	private StockRepository repst;
	
	@GetMapping("/showStock")
	public String showStock(HttpSession session, Model model,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
	        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

		Integer warehouseId = (Integer) session.getAttribute("warehouseId");
			
	    if (startDate == null) {
	        startDate = LocalDate.of(2020, 1, 1); 
	    }
	    if (endDate == null) {
	        endDate = LocalDate.now(); 
	    }

	    List<StockSumByWarehouseId> stock = repst.findAllStock(warehouseId, startDate, endDate);
	    List<Stock> stockdetail = repst.findStockDetail(warehouseId, startDate, endDate);			   

	    model.addAttribute("stocks", stock);	
	    model.addAttribute("stockdetail", stockdetail);	
	    model.addAttribute("startDate",startDate); 
	    model.addAttribute("endDate",endDate);
	    
		return Views.SHOW_STOCK;
	}
	
	@GetMapping("/showStockAllJson")
	@ResponseBody
	public List<StockSumByWarehouseId> showStockAllJson(HttpSession session,
	        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
	        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {


	    Integer warehouseId = (Integer) session.getAttribute("warehouseId");
	    if (warehouseId == null) {
	        throw new IllegalStateException("Session expired or warehouseId not found.");
	    }
	    if (startDate == null) startDate = LocalDate.of(2020, 1, 1);
	    if (endDate == null) endDate = LocalDate.now();

	    return repst.findAllStock(warehouseId, startDate, endDate); 
	}

	
	@GetMapping("/showStockDetailJson")
	@ResponseBody
    public List<Stock> showStockDetailJson(HttpSession session,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        
        Integer warehouseId = (Integer) session.getAttribute("warehouseId");
        if (warehouseId == null) {
            throw new IllegalStateException("Session expired or warehouseId not found.");
        }
        
        return repst.findStockDetail(warehouseId, startDate, endDate); 
    }
	
	@GetMapping("/showStockDetail")
	public String showStockDetail(HttpSession session, Model model,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
	        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

		
		Integer warehouseId = (Integer) session.getAttribute("warehouseId");

		    List<Stock> stock = repst.findStockDetail(warehouseId,startDate,endDate);
		    if (startDate == null) {
		        startDate = LocalDate.of(2020, 1, 1); 
		    }
		    if (endDate == null) {
		        endDate = LocalDate.now(); 
		    }
		    model.addAttribute("stocks", stock);
		    model.addAttribute("startDate", startDate); 
		    model.addAttribute("endDate", endDate);
		    
		return Views.SHOW_STOCK_DETAIL;
	}
	
	
	@GetMapping("/lowStockApi")
	@ResponseBody
	public List<Map<String, Object>> getLowStockData(@RequestParam(required = false) Integer minQuantity, HttpSession session) {
	    Integer warehouseId = (Integer) session.getAttribute("warehouseId");

	    if (minQuantity == null ) {
	        return repst.findAllLowStock(warehouseId);
	    }

	    
	    return repst.findLowStock(warehouseId, minQuantity);
	}


	
	@GetMapping("/chartStock")
	public String chart(HttpSession session) {
	    Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");

	    if (loggedInEmployee == null) {
	        return "redirect:/employee/login"; 
	    }

	    return Views.SHOW_STOCK_CHAR; 
	}

	@GetMapping("/inventory-stats")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getInventoryStats(
	        HttpSession session,
	        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
	        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
	    

	    Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
	    Integer warehouseId = (Integer) session.getAttribute("warehouseId");

	    
	    if (loggedInEmployee != null && warehouseId != null) {
	        try {

	            List<Map<String, Object>> inventoryStats = repst.getInventoryStats(warehouseId, startDate, endDate);
	            return ResponseEntity.ok(inventoryStats);
	        } catch (Exception e) {
	            System.err.println("Error occurred while fetching inventory stats: " + e.getMessage());
	            e.printStackTrace();


	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body(List.of());
	        }
	    }


	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(List.of());
	}

	@GetMapping("/gettLineChart")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> gettLineChart(
	        HttpSession session,
	        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
	        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
	    

	    Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
	    Integer warehouseId = (Integer) session.getAttribute("warehouseId");

	    
	    if (loggedInEmployee != null && warehouseId != null) {
	        try {

	            List<Map<String, Object>> inventoryStats = repst.listProReceipt(warehouseId, startDate, endDate);
	            return ResponseEntity.ok(inventoryStats);
	        } catch (Exception e) {
	            System.err.println("Error occurred while fetching inventory stats: " + e.getMessage());
	            e.printStackTrace();


	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body(List.of());
	        }
	    }


	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(List.of());
	}


}
