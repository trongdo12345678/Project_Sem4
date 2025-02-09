package com.admin.controller;

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

import com.customer.repository.CouponRepository;
import com.models.Coupon;
import com.models.Employee;
import com.models.PageView;
import com.utils.Views;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin/discount")
public class DiscountController {
	@Autowired
	private CouponRepository repcp;

	@GetMapping("/showList")
	public String showDiscountList(Model model,
			@RequestParam(name = "cp", required = false, defaultValue = "1") int cp) {
		try {
			PageView pv = new PageView();
			pv.setPage_current(cp);
			pv.setPage_size(8);

			List<Coupon> coupons = repcp.getAll(pv);
			model.addAttribute("coupons", coupons);
			model.addAttribute("pv", pv);

			return Views.DISCOUNT_LIST;

		} catch (Exception e) {
			return Views.DISCOUNT_LIST;
		}
	}

	@PostMapping("/add")
	@ResponseBody
	public ResponseEntity<?> addDiscount(@RequestBody Coupon coupon, HttpServletRequest request) {
		try {

			Employee emp = (Employee) request.getSession().getAttribute("loggedInEmployee");
			coupon.setEmployee_Id(emp.getId());
			boolean result = repcp.insert(coupon);
			if (result) {
				return ResponseEntity.ok().body(Map.of("message", "Discount added successfully"));
			} else {
				return ResponseEntity.badRequest().body(Map.of("message", "Failed to add discount"));
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
		}
	}

	@GetMapping("/validatediscount")
	public ResponseEntity<Coupon> validateCoupon(@RequestParam("code") String code) {

		Coupon coupon = repcp.findCouponByCode(code);

		if (coupon == null) {
			return ResponseEntity.ok().body(null);
		}

		return ResponseEntity.ok(coupon);
	}

	@GetMapping("/validatediscountid")
	@ResponseBody
	public Coupon validateDiscount(@RequestParam String code, @RequestParam(required = false) Integer id) {
		return repcp.findCouponByCode(code, id);
	}

	@GetMapping("/check-usage/{id}")
	@ResponseBody
	public ResponseEntity<?> checkDiscountUsage(@PathVariable int id) {
		try {
			boolean isUsed = repcp.isDiscountUsedInOrders(id);
			return ResponseEntity.ok().body(Map.of("isUsed", isUsed));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
		}
	}

	@GetMapping("/{id}")
	@ResponseBody
	public ResponseEntity<?> getDiscountById(@PathVariable int id) {
		try {
			Coupon coupon = repcp.findById(id);
			if (coupon == null) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(coupon);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
		}
	}

	@PostMapping("/update")
	@ResponseBody
	public ResponseEntity<?> updateDiscount(@RequestBody Coupon coupon, HttpServletRequest request) {
		try {
			

			Employee emp = (Employee) request.getSession().getAttribute("loggedInEmployee");
			
			coupon.setEmployee_Id(emp.getId());
			boolean result = repcp.updateDiscount(coupon);
			if (result) {
				return ResponseEntity.ok().body(Map.of("message", "Discount updated successfully"));
			} else {
				return ResponseEntity.badRequest().body(Map.of("message", "Failed to update discount"));
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
		}
	}
}