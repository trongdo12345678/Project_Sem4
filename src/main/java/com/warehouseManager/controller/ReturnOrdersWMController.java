package com.warehouseManager.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.customer.repository.ReturnOrderRepository;
import com.models.PageView;
import com.models.ReturnOrder;
import com.models.ReturnOrderDetail;
import com.models.Stock;
import com.utils.Views;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("warehouseManager/returnorders")
public class ReturnOrdersWMController {
	@Autowired
	ReturnOrderRepository repreor;
	
	@GetMapping("/showreturnlist")
	public String showReturnOrders(Model model,
	        @RequestParam(name = "cp", required = false, defaultValue = "1") int cp,
	        HttpServletRequest request) {
	    try {
	        // Lấy warehouseId từ session
	        Integer warehouseId = (Integer) request.getSession().getAttribute("warehouseId");
	        if (warehouseId == null) {
	           
	            model.addAttribute("error", "Warehouse ID not found in session.");
	            return Views.RETURN_ORDERS_WAM;
	        }

	        PageView pv = new PageView();
	        pv.setPage_current(cp);
	        pv.setPage_size(8);

	        // Lấy danh sách ReturnOrder theo warehouseId
	        List<ReturnOrder> returnOrders = repreor.findAcceptedReturnOrdersByWarehouse(pv, warehouseId);
	        model.addAttribute("returnOrders", returnOrders);
	        model.addAttribute("pv", pv);

	        return Views.RETURN_ORDERS_WAM;

	    } catch (Exception e) {
	        model.addAttribute("error", "An error occurred while fetching return orders.");
	        return Views.RETURN_ORDERS_WAM;
	    }
	}
	@GetMapping("/showreturnlistcomplete")
	public String findCompleteReturnOrdersByWarehouse(Model model,
	        @RequestParam(name = "cp", required = false, defaultValue = "1") int cp,
	        HttpServletRequest request) {
	    try {
	        // Lấy warehouseId từ session
	        Integer warehouseId = (Integer) request.getSession().getAttribute("warehouseId");
	        if (warehouseId == null) {
	           
	            model.addAttribute("error", "Warehouse ID not found in session.");
	            return Views.RETURN_ORDERS_WAMCOM;
	        }

	        PageView pv = new PageView();
	        pv.setPage_current(cp);
	        pv.setPage_size(8);

	        // Lấy danh sách ReturnOrder theo warehouseId
	        List<ReturnOrder> returnOrders = repreor.findCompleteReturnOrdersByWarehouse(pv, warehouseId);
	        model.addAttribute("returnOrders", returnOrders);
	        model.addAttribute("pv", pv);

	        return Views.RETURN_ORDERS_WAMCOM;

	    } catch (Exception e) {
	        model.addAttribute("error", "An error occurred while fetching return orders.");
	        return Views.RETURN_ORDERS_WAMCOM;
	    }
	}
	
	@GetMapping("/return-order/{id}")
	@ResponseBody
	public Map<String, Object> getReturnOrderDetails(@PathVariable int id) {
	    try {
	        Map<String, Object> response = new HashMap<>();
	        
	        // Lấy thông tin return order
	        ReturnOrder returnOrder = repreor.findReturnOrderById(id);
	        if (returnOrder == null) {
	            throw new RuntimeException("Return order not found");
	        }
	        
	        // Lấy danh sách ReturnOrderDetails
	        List<ReturnOrderDetail> returnDetails = repreor.findReturnOrderDetailsByReturnOrderId(id);
	        returnOrder.setReturnDetails(returnDetails);
	        
	       
	        List<Stock> stocks = repreor.getUniqueStocksByOrderId(returnOrder.getOrderId());

	        response.put("returnOrder", returnOrder);
	        response.put("stocks", stocks);
	        
	        return response;
	        
	    } catch (Exception e) {
	        throw new RuntimeException("Error fetching return order details: " + e.getMessage());
	    }
	}
	
	@PostMapping("/return-all-to-stock")
    public ResponseEntity<?> returnAllToStock(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> stockItems = (List<Map<String, Object>>) request.get("stockItems");
            
            boolean success = repreor.processReturnToStock(stockItems);
            if (success) {
                return ResponseEntity.ok().body(Map.of("message", "Successfully processed return items"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("message", "Failed to process return items"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("message", "Error: " + e.getMessage()));
        }
    }
}
