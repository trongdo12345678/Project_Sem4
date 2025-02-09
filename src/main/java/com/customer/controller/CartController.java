package com.customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.customer.repository.CartRepository;
import com.customer.repository.CouponRepository;
import com.models.Coupon;
import com.models.Shopping_cart;
import com.utils.Views;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("cart")
public class CartController {
	@Autowired
	private CartRepository rep;
	@Autowired
	private CouponRepository repcp;

	@GetMapping("/showcart")
	public String showpage(Model model, HttpServletRequest request, HttpServletResponse response) {
		// Yêu cầu không lưu cache
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		request.getSession().setAttribute("appliedcouponid", null);
		request.getSession().setAttribute("cameFromCart", true);
		List<Shopping_cart> listc = rep.findAllCartsByCustomerId((int) request.getSession().getAttribute("logined"));
		double totalCartValue = 0.00;
		for (Shopping_cart cartItem : listc) {
			totalCartValue += cartItem.getPrice() * cartItem.getQuantity();
		}
		model.addAttribute("totalCartValue", totalCartValue);
		model.addAttribute("carts", listc);
		return Views.CUS_CARTPAGE;
	}

	@PostMapping("/addtocart")
	@ResponseBody
	public String addToCart(@RequestParam int id, HttpServletRequest request) {
		if (request.getSession().getAttribute("logined") == null) {
			return "{\"status\": \"false\"}";
		}

		Shopping_cart cart = new Shopping_cart();
		cart.setCustomer_id((int) request.getSession().getAttribute("logined"));
		cart.setProduct_id(id);
		cart.setQuantity(1);

		boolean success = rep.addToCartOrUpdate(cart);

		if (success) {
			return "{\"status\": \"success\"}";
		} else {
			return "{\"status\": \"failed\"}";
		}
	}

	@PostMapping("/pluscart")
	@ResponseBody
	public String pluscart(@RequestParam int id, HttpServletRequest request) {

		boolean success = rep.plusProductQuantityInCart(id);

		if (success) {
			return "{\"status\": \"success\"}";
		} else {
			return "{\"status\": \"failed\"}";
		}
	}

	@PostMapping("/minuscart")
	@ResponseBody
	public String minuscart(@RequestParam int id, HttpServletRequest request) {

		boolean success = rep.minusProductQuantityInCart(id);

		if (success) {
			return "{\"status\": \"success\"}";
		} else {
			return "{\"status\": \"failed\"}";
		}
	}

	@PostMapping("/deletecartbyid")
	@ResponseBody
	public String deletecartbyid(@RequestParam int id, HttpServletRequest request) {

		boolean success = rep.deleteCartById(id);

		if (success) {
			return "{\"status\": \"success\"}";
		} else {
			return "{\"status\": \"failed\"}";
		}
	}

	@PostMapping("/addtocartwq")
	@ResponseBody
	public String addtocartwq(@RequestParam("id") int id, @RequestParam("quantity") int quantity,
			HttpServletRequest request) {
		if (request.getSession().getAttribute("logined") == null) {
			return "{\"status\": \"false\"}";
		}

		Shopping_cart cart = new Shopping_cart();
		cart.setCustomer_id((int) request.getSession().getAttribute("logined"));
		cart.setProduct_id(id);
		cart.setQuantity(quantity);

		boolean success = rep.addToCartOrUpdate(cart);

		if (success) {
			return "{\"status\": \"success\"}";
		} else {
			return "{\"status\": \"failed\"}";
		}
	}

	@PostMapping("/size")
	@ResponseBody
	public String size(HttpServletRequest request) {

		List<Shopping_cart> listc = rep.findAllCartsByCustomerId((int) request.getSession().getAttribute("logined"));
		int size = listc.size();

		return "{\"status\": " + size + "}";
	}

	@GetMapping("/validatediscount")
	public ResponseEntity<Coupon> validateCoupon(@RequestParam("code") String code) {
		
		Coupon coupon = repcp.findCouponByCode(code);

		
		if (coupon == null) {
			return ResponseEntity.ok().body(null);
		}

		
		return ResponseEntity.ok(coupon);
	}

}
