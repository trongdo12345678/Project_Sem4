package com.customer.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.customer.repository.MomoService;
import com.customer.repository.OrderAPIRepository;
import com.customer.repository.OrderRepository;
import com.models.Order;

@RestController
@RequestMapping("/api/v2/order")
public class OrderAPIController {
    
    @Autowired
    private OrderAPIRepository repoapi;
    @Autowired
    OrderRepository repo;
    @Autowired
    MomoService momoser;
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getOrdersByCustomerId(@PathVariable int customerId) {
        try {
            List<Order> orders = repoapi.getOrdersByCustomerIdAPI(customerId);
            if (orders.isEmpty()) {
                return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new HashMap<String, Object>() {{
                        put("status", false);
                        put("message", "No orders found for customer ID: " + customerId);
                    }});
            }
            
            return ResponseEntity.ok(new HashMap<String, Object>() {{
                put("status", true);
                put("message", "Orders retrieved successfully");
                put("orders", orders);
            }});
            
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new HashMap<String, Object>() {{
                    put("status", false);
                    put("message", "Error retrieving orders: " + e.getMessage());
                }});
        }
    }
    @GetMapping("/gocancel/{id}")
    public ResponseEntity<Map<String, Object>> gocancel(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Order order = repo.getOrderById(id);
            if (order == null) {
                response.put("status", false);
                response.put("message", "Không tìm thấy đơn hàng");
                return ResponseEntity.ok(response);
            }

            
            String orderStatus = order.getStatus().trim();
            if (!"Waiting for confirmation".equals(orderStatus)) {
                response.put("status", false);
                response.put("message", "Chỉ có thể hủy đơn hàng ở trạng thái chờ xác nhận");
                return ResponseEntity.ok(response);
            }

            // Kiểm tra payment status
            String paymentStatus = order.getPay_status().trim().toLowerCase();

            if ("not pay yet".equals(paymentStatus)) {
                
                repo.updateOrderStatusToCanceled(id, "Canceled");
                response.put("status", true);
                response.put("message", "Đã hủy đơn hàng thành công");
                response.put("order", order);
                
            } else if ("paid".equals(paymentStatus)) {
                
                if (order.getTransactionId() == null || order.getTransactionId().isEmpty()) {
                    response.put("status", false);
                    response.put("message", "Không tìm thấy mã giao dịch MoMo");
                    return ResponseEntity.ok(response);
                }

                boolean refundSuccess = momoser.refundPayment(order, "Refund order: " + order.getOrderID());

                if (refundSuccess) {
                    repo.updateOrderStatusToCanceled(id, "Canceled");
                    repo.updateOrderpaymentstatus(id, "Refunded");
                    response.put("status", true);
                    response.put("message", "Đã hủy đơn hàng và hoàn tiền thành công");
                    response.put("order", order);
                } else {
                    response.put("status", false);
                    response.put("message", "Không thể hoàn tiền, vui lòng liên hệ CSKH");
                }
            } else {
                response.put("status", false);
                response.put("message", "Trạng thái thanh toán không hợp lệ");
            }

        } catch (Exception e) {
            response.put("status", false);
            response.put("message", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
    
}
