package com.customer.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.customer.repository.AccountRepository;
import com.customer.repository.EmailService;
import com.customer.repository.OrderRepository;
import com.customer.repository.ReturnOrderRepository;
import com.models.Customer;
import com.models.Order;
import com.models.ReturnOrder;
import com.models.ReturnOrderDetail;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Data;

@RestController
@RequestMapping("/api/v2/return-orders")
public class RestReturnOrderController {
	@Autowired
	private ReturnOrderRepository returnOrderRepository;

	@Autowired
	private OrderRepository orderRepository;

	@PostMapping("/create")
    public ResponseEntity<?> createReturnOrder(@RequestBody ReturnOrderRequest request) {
        try {
            
            ReturnOrder returnOrder = new ReturnOrder();
            returnOrder.setOrderId(request.getOrderId());
            returnOrder.setReturnDate(LocalDate.now());
            returnOrder.setStatus("Processing");
            returnOrder.setNote(request.getReason());
            returnOrder.setEmployee_id(0);

            
            double totalAmount = 0;
            for (ReturnItemRequest item : request.getItems()) {
                totalAmount += item.getAmount();
            }

            
            Order originalOrder = orderRepository.getOrderById(request.getOrderId());
            if (originalOrder == null) {
                return ResponseEntity.badRequest().body("Order not found");
            }

            
            double totalDiscount = originalOrder.getDiscount();
            returnOrder.setTotalAmount(totalAmount);
            returnOrder.setDiscountAmount(totalDiscount);
            returnOrder.setFinalAmount(totalAmount - totalDiscount);

            
            int returnOrderId = returnOrderRepository.insertReturnOrder(returnOrder);
            if (returnOrderId == 0) {
                return ResponseEntity.badRequest().body("Failed to create return order");
            }
            returnOrder.setId(returnOrderId);

            
            boolean allDetailsInserted = true;
            for (ReturnItemRequest item : request.getItems()) {
                ReturnOrderDetail detail = new ReturnOrderDetail();
                detail.setReturnOrderId(returnOrder.getId());
                detail.setOrderDetailId(item.getOrderDetailId());
                detail.setQuantity(item.getQuantity());
                detail.setReason(item.getReason());
                detail.setAmount(item.getAmount());

                boolean insertDetailSuccess = returnOrderRepository.insertReturnOrderDetail(detail);
                if (!insertDetailSuccess) {
                    allDetailsInserted = false;
                    break;
                }
            }

            if (!allDetailsInserted) {
                return ResponseEntity.badRequest().body("Failed to create return order details");
            }

            
            ReturnOrder completeReturnOrder = returnOrderRepository.getCompleteReturnOrder(returnOrderId);
            
            return ResponseEntity.ok(completeReturnOrder);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

	@PostMapping("/cancel/{id}")
	@ResponseBody
	public ResponseEntity<?> cancelReturnOrder(@PathVariable("id") int returnOrderId) {
		try {
			boolean updated = returnOrderRepository.updateStatusAndMessage(returnOrderId, "Cancelled", "", 0);

			if (updated) {
				Map<String, String> response = new HashMap<>();
				response.put("message", "Return order cancelled successfully");
				return ResponseEntity.ok(response);
			} else {
				return ResponseEntity.badRequest().body("Failed to cancel return order");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error cancelling return order: " + e.getMessage());
		}
	}
    
    
}
