package com.customer.controller;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.customer.repository.AccountRepository;
import com.customer.repository.CommentRepository;
import com.models.Comment;

@Controller
@RequestMapping("comment")
public class CommentController {

	@Autowired
	AccountRepository repacc;

	@Autowired
	CommentRepository commentRepository;

	@PostMapping("/add")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> addComment(@RequestBody Comment comment, HttpServletRequest request) {
		if (request.getSession().getAttribute("logined") == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		Integer customerId = (Integer) request.getSession().getAttribute("logined");
		comment.setCustomerId(customerId);

		Comment insertedComment = commentRepository.insertComment(comment);

		if (insertedComment != null) {
			Map<String, Object> response = new HashMap<>();
			response.put("commentId", insertedComment.getId());
			response.put("content", insertedComment.getContent());
			response.put("customerName",
					insertedComment.getCustomerFirstName() + " " + insertedComment.getCustomerLastName());

			// Format LocalDate
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			response.put("createdAt", insertedComment.getCreatedAt().format(formatter));

			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping("/reply")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> replyComment(@RequestBody Comment comment, HttpServletRequest request) {
		if (request.getSession().getAttribute("logined") == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		Integer customerId = (Integer) request.getSession().getAttribute("logined");
		comment.setCustomerId(customerId);

		Comment insertedReply = commentRepository.insertComment(comment);

		if (insertedReply != null) {
			Map<String, Object> response = new HashMap<>();
			response.put("commentId", insertedReply.getId());
			response.put("content", insertedReply.getContent());
			response.put("customerName",
					insertedReply.getCustomerFirstName() + " " + insertedReply.getCustomerLastName());

			// Format LocalDate
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			response.put("createdAt", insertedReply.getCreatedAt().format(formatter));

			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}

}