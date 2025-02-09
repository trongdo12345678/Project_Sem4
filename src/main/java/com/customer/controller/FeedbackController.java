package com.customer.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.customer.repository.FeedbackRepository;
import com.models.Feedback;

@Controller
@RequestMapping("feedback")
public class FeedbackController {
	@Autowired
	FeedbackRepository rep;

	@PostMapping("/submit")
	public String submitFeedback(@RequestParam(value = "productId", required = true) String productId,
			@RequestParam(value = "orderdetailId", required = true) String orderdetailId,
			@RequestParam(value = "rating", required = true) String rating,
			@RequestParam(value = "comment", required = false) String comment,
			@RequestParam(value = "orderId", required = true) String orderId, RedirectAttributes redirectAttributes) {
		try {
			Integer productIdInt = Integer.parseInt(productId);
			Integer orderdetailIdInt = Integer.parseInt(orderdetailId);
			Integer ratingInt = Integer.parseInt(rating);

			
			Feedback fb = new Feedback();
			fb.setProduct_Id(productIdInt);
			fb.setOrderDetail_Id(orderdetailIdInt);
			fb.setRating(ratingInt);
			fb.setComment(comment);
			fb.setStatus(null);
			rep.addFeedback(fb);
			return "redirect:/order/showdetailor?id=" + orderId;

		} catch (NumberFormatException e) {

			return "redirect:/order/showdetailor?id=" + orderId;
		} catch (Exception e) {

			return "redirect:/order/showdetailor?id=" + orderId;
		}
	}
	@PostMapping("/submitapi")
    public ResponseEntity<Map<String, Object>> submitFeedback(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Parse parameters from request body
            Integer productId = Integer.parseInt(request.get("productId").toString());
            Integer orderDetailId = Integer.parseInt(request.get("orderDetailId").toString());
            Integer rating = Integer.parseInt(request.get("rating").toString());
            String comment = request.get("comment") != null ? request.get("comment").toString() : null;

            // Create feedback object
            Feedback fb = new Feedback();
            fb.setProduct_Id(productId);
            fb.setOrderDetail_Id(orderDetailId);
            fb.setRating(rating);
            fb.setComment(comment);
            fb.setStatus(null);

            // Save feedback
            rep.addFeedback(fb);

            response.put("status", true);
            response.put("message", "Feedback submitted successfully");
            response.put("feedback", fb);
            
            return ResponseEntity.ok(response);

        } catch (NumberFormatException e) {
            response.put("status", false);
            response.put("message", "Invalid input format");
            return ResponseEntity.badRequest().body(response);
            
        } catch (Exception e) {
            response.put("status", false);
            response.put("message", "Error submitting feedback: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
