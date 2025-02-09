package com.admin.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

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

import com.admin.repository.TaxRepository;
import com.models.Employee;
import com.models.PageView;
import com.models.TaxHistory;
import com.utils.Views;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/tax")
public class TaxController {
	@Autowired
	private TaxRepository taxRepo;

	@GetMapping("/showtax")
	public String showtax(Model model, HttpSession session,
			@RequestParam(name = "cp", required = false, defaultValue = "1") int cp) {

		try {
			PageView pv = new PageView();
			pv.setPage_current(cp);
			pv.setPage_size(8);

			// Lấy danh sách thuế mà không cần lọc
			List<TaxHistory> taxList = taxRepo.findAll(pv);

			model.addAttribute("taxList", taxList);
			model.addAttribute("pv", pv);

			return Views.TAX_SHOWTAXPAGE;

		} catch (Exception e) {
			System.err.println("Error in showtax: " + e.getMessage());
			return "redirect:/admin/tax/showtax";
		}
	}

	@GetMapping("/clear-filter")
	@ResponseBody
	public ResponseEntity<?> clearFilter(HttpSession session) {
		session.removeAttribute("taxFilterStart");
		session.removeAttribute("taxFilterEnd");
		return ResponseEntity.ok().build();
	}

	@PostMapping("/add")
	@ResponseBody
	public ResponseEntity<?> addTax(@RequestBody TaxHistory tax, HttpServletRequest request) {
		try {
			if (tax.getPeriodEnd().isBefore(tax.getPeriodStart())) {
				return ResponseEntity.badRequest().body("End date cannot be before start date");
			}
			
			Employee emp = (Employee) request.getSession().getAttribute("loggedInEmployee");
			tax.setPaymentStatus("Pending");
			tax.setCreatedAt(LocalDateTime.now());

			tax.setCreatedBy(emp.getId());

			taxRepo.save(tax);

			return ResponseEntity.ok("Tax added successfully");

		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error adding tax: " + e.getMessage());
		}
	}

	@GetMapping("/calculate-revenue")
	@ResponseBody
	public ResponseEntity<?> calculateRevenue(@RequestParam String startDate, @RequestParam String endDate) {
		try {
			LocalDate start = LocalDate.parse(startDate);
			LocalDate end = LocalDate.parse(endDate);

			if (end.isBefore(start)) {
				return ResponseEntity.badRequest().body("End date cannot be before start date");
			}

			// Calculate revenue
			Double revenue = taxRepo.calculateRevenue(start, end);
			return ResponseEntity.ok(revenue);

		} catch (DateTimeParseException e) {
			return ResponseEntity.badRequest().body("Invalid date format");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error calculating revenue: " + e.getMessage());
		}
	}

	@GetMapping("/{id}")
	@ResponseBody
	public ResponseEntity<TaxHistory> getTaxById(@PathVariable int id) {
		TaxHistory tax = taxRepo.findById(id);
		if (tax != null) {
			return ResponseEntity.ok(tax);
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping("/{id}/pay")
	@ResponseBody
	public ResponseEntity<?> markAsPaid(@PathVariable int id) {
		try {
			TaxHistory tax = taxRepo.findById(id);
			if (tax == null) {
				return ResponseEntity.notFound().build();
			}

			if (!"Pending".equals(tax.getPaymentStatus())) {
				return ResponseEntity.badRequest().body("This tax is already processed");
			}

			tax.setPaymentStatus("Paid");
			tax.setPaymentDate(LocalDate.now());
			taxRepo.updateStatus(tax);

			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error updating tax: " + e.getMessage());
		}
	}

	@PostMapping("/{id}/cancel")
	@ResponseBody
	public ResponseEntity<?> cancelTax(@PathVariable int id) {
		try {
			TaxHistory tax = taxRepo.findById(id);
			if (tax == null) {
				return ResponseEntity.notFound().build();
			}

			if (!"Pending".equals(tax.getPaymentStatus())) {
				return ResponseEntity.badRequest().body("This tax is already processed");
			}

			tax.setPaymentStatus("Cancelled");
			taxRepo.updateStatus(tax);

			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error cancelling tax: " + e.getMessage());
		}
	}
}
