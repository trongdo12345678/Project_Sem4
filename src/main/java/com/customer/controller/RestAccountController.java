package com.customer.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.customer.repository.AccountRepository;
import com.customer.repository.EmailService;
import com.models.Customer;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Data;

@RestController
@RequestMapping("/api/v2/customer")
public class RestAccountController {
	@Autowired
    private EmailService emailService;
	@Autowired
	AccountRepository rep;
	
	@PostMapping("/checklogin")
	@ResponseBody
	public ResponseEntity<Customer> checklogin(
	    @RequestParam("email") String email,
	    @RequestParam("password") String password, 
	    HttpServletRequest request) {
	    
	    Customer customer = rep.login(email, password);
	    if (customer != null) {
	        request.getSession().setAttribute("logined", customer.getId());
	        return ResponseEntity.ok(customer);
	    }
	    return ResponseEntity.notFound().build();
	}
	
	@PostMapping("/register/initiate")
    public ResponseEntity<?> initiateRegistration(
            @RequestParam String email,
            @RequestParam String password) {
        try {
        	
        	var check = rep.isEmailRegistered(email);
        	if(check) {
        		return ResponseEntity.ok()
                .body(Map.of(
                        "status", "true",
                        "message", "EEmail already exists"
                    ));
        	}
        	
            emailService.sendOTPEmail(email, password);
            return ResponseEntity.ok()
                .body(Map.of(
                    "status", "success",
                    "message", "OTP sent successfully"
                ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
                ));
        }
    }

    @PostMapping("/register/verify")
    public ResponseEntity<?> verifyAndRegister(
            @RequestParam("email") String email,
            @RequestParam("otp") String otp,
            HttpServletRequest request) {

        try {
        	if (emailService.verifyOTP(email, otp)) {
                // Lấy password đã lưu
                String password = emailService.getStoredPassword(email);
                if (password == null) {
                    return ResponseEntity.badRequest()
                        .body(Map.of(
                            "status", "error",
                            "message", "Registration session expired"
                        ));
                }

            
            Customer newCustomer = new Customer();
            newCustomer.setEmail(email);
            newCustomer.setPassword(password);
            rep.createAccount(newCustomer);

            emailService.clearRegistrationData(email);

            return ResponseEntity.ok()
                .body(Map.of(
                    "status", "success",
                    "message", "Registration successful"
                ));
        } else {
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "status", "error",
                    "message", "Invalid or expired OTP"
                ));
        }
    } catch (Exception e) {
        return ResponseEntity.badRequest()
            .body(Map.of(
                "status", "error",
                "message", e.getMessage()
            ));
    }
}
    @PostMapping("/register/resend-otp")
    public ResponseEntity<?> resendOTP(@RequestParam String email) {
        try {
            
            long remainingTime = emailService.getRemainingTimeForResend(email);
            if (remainingTime > 0) {
                return ResponseEntity.badRequest()
                    .body(Map.of(
                        "status", "error",
                        "message", "Please wait " + remainingTime + " seconds before requesting a new OTP",
                        "remainingTime", remainingTime
                    ));
            }

            // Thực hiện gửi lại OTP
            boolean sent = emailService.resendOTP(email);
            if (sent) {
                return ResponseEntity.ok()
                    .body(Map.of(
                        "status", "success",
                        "message", "New OTP sent successfully"
                    ));
            } else {
                return ResponseEntity.badRequest()
                    .body(Map.of(
                        "status", "error",
                        "message", "No active registration found for this email"
                    ));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
                ));
        }
    }
    @PostMapping("/update")
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer) {
        try {
            if (customer == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of(
                        "status", "error", 
                        "message", "Customer data is required"
                    ));
            }

          
            Customer existingCustomer = rep.finbyid(customer.getId());
            if (existingCustomer == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of(
                        "status", "error",
                        "message", "Customer not found"
                    ));
            }

            // Cập nhật thông tin
            existingCustomer.setFirst_name(customer.getFirst_name());
            existingCustomer.setLast_name(customer.getLast_name());
            existingCustomer.setAddress(customer.getAddress());
            existingCustomer.setBirthDay(customer.getBirthDay());

            // Lưu vào database
            boolean updated = rep.updateAccount(existingCustomer);

            if (updated) {
                return ResponseEntity.ok()
                    .body(Map.of(
                        "status", "success",
                        "message", "Profile updated successfully",
                        "customer", existingCustomer
                    ));
            } else {
                return ResponseEntity.badRequest()
                    .body(Map.of(
                        "status", "error",
                        "message", "Failed to update profile"
                    ));
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
                ));
        }
    }
    
    
    
    @PostMapping("/change-password")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> changePassword(
        @RequestParam("customerId") int customerId,
        @RequestParam("currentPassword") String currentPassword,
        @RequestParam("newPassword") String newPassword) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            
            if (!rep.verifyPassword(customerId, currentPassword)) {
                response.put("status", "error");
                response.put("message", "Current password is incorrect");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            
            rep.updatePassword(customerId, newPassword);
            
            response.put("status", "success");
            response.put("message", "Password changed successfully");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to change password");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    
}
