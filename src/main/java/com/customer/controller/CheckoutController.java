package com.customer.controller;

import java.security.SecureRandom;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.customer.repository.AccountRepository;
import com.customer.repository.CartRepository;
import com.customer.repository.CheckoutRepository;
import com.customer.repository.CouponRepository;
import com.customer.repository.DistanceService;
import com.customer.repository.GHNService;
import com.customer.repository.MomoService;
import com.models.Coupon;
import com.models.Customer;
import com.models.Order;
import com.models.Shopping_cart;
import com.models.Warehouse;
import com.utils.Views;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("checkout")
public class CheckoutController {
	@Autowired
	CartRepository repcart;

	@Autowired
	AccountRepository repacc;

	@Autowired
	CheckoutRepository repco;

	@Autowired
	CouponRepository repcp;

	@Autowired
	private MomoService momoService;

	@Autowired
	private GHNService ghnService;

	@Autowired
	private DistanceService distanceService;

	@GetMapping("/showcheckout")
	public String showpage(Model model, HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		Boolean cameFromCart = (Boolean) request.getSession().getAttribute("cameFromCart");
		if (cameFromCart == null || !cameFromCart) {
			request.getSession().removeAttribute("cameFromCart");
			return "redirect:/cart/showcart";
		}
		int customerId = (int) request.getSession().getAttribute("logined");
		List<Shopping_cart> listc = repcart.findAllCartsByCustomerId(customerId);
		double totalCartValue = listc.stream().mapToDouble(cartItem -> cartItem.getPrice() * cartItem.getQuantity())
				.sum();

		model.addAttribute("totalCartorigin", String.format("%.2f", totalCartValue));
		Double discount = 0.00;
		if (request.getSession().getAttribute("appliedcouponid") != null) {
			Coupon cp = repcp.findCouponByid((int) request.getSession().getAttribute("appliedcouponid"));
			discount = repcp.calculateTotalCartValueWithCoupon(totalCartValue, cp);
			model.addAttribute("couponId", (int) request.getSession().getAttribute("appliedcouponid"));
		} else {
			model.addAttribute("couponId", null);
		}
		totalCartValue = totalCartValue - discount;

		model.addAttribute("discount", String.format("%.2f", discount));
		model.addAttribute("totalCartValue", String.format("%.2f", totalCartValue));
		model.addAttribute("carts", listc);

		Customer cus = repacc.finbyid(customerId);
		model.addAttribute("cusinfo", cus);

		model.addAttribute("listco", repco.takeall());

		return Views.CUS_CUSCHECKOUTPAGE;
	}

	@GetMapping("/savecoupon")
	public String savecoupon(@RequestParam(value = "coupon", required = false) int couponid,
			HttpServletRequest request) {
		request.getSession().setAttribute("appliedcouponid", couponid);

		return "redirect:/checkout/showcheckout";
	}

	@PostMapping("/gocheckout")
	public String gocheckout(@ModelAttribute Customer cusinfo, @RequestParam String payment,
			@RequestParam String phoneco, @RequestParam(required = false) String couponId,
			@RequestParam(required = false) String discount, @RequestParam(required = false) String totalCartValue,
			@RequestParam(required = false) String notes, @RequestParam String province, @RequestParam String district,
			@RequestParam String ward, @RequestParam(required = false) String shippingFee,
			@RequestParam(required = false) String warehouseId, Model model, HttpServletRequest request,
			HttpServletResponse response) {

		String[] provinceData = province.split("\\|");
		String[] districtData = district.split("\\|");
		String[] wardData = ward.split("\\|");
		int provinceId = Integer.parseInt(provinceData[0]);
		String provinceName = provinceData[1];
		int districtId = Integer.parseInt(districtData[0]);
		String districtName = districtData[1];
		String wardId = wardData[0];
		String wardName = wardData[1];
		String fullAddress = String.format("%s, %s, %s, %s", removeAccent(cusinfo.getAddress()), removeAccent(wardName),
				removeAccent(districtName), removeAccent(provinceName));

		Order or = new Order();
		or.setDiscount(Double.parseDouble(discount));
		or.setShippingFee(Double.parseDouble(shippingFee));
		or.setCustomer_id(cusinfo.getId());
		or.setCus_Name(cusinfo.getFirst_name() + " " + cusinfo.getLast_name());
		or.setStatus("Waiting for confirmation");
		or.setPhone(phoneco);
		or.setProvince_Id(provinceId);
		or.setDistrict_Id(districtId);
		or.setWard_Id(wardId);
		or.setWareHouse_Id(Integer.parseInt(warehouseId));
		or.setAddress(fullAddress);
		or.setEmployee_id(0);
		or.setPayment_id(Integer.parseInt(payment));
		or.setNotes(notes);
		or.setDate(LocalDate.now());
		or.setTotalAmount(Double.parseDouble(totalCartValue));
		or.setCoupon_id(couponId != "" ? Integer.parseInt(couponId) : 0);
		or.setOrderID(generateOrderId());

		List<Shopping_cart> listc = repcart
				.findAllCartsByCustomerId((int) request.getSession().getAttribute("logined"));

		if (or.getPayment_id() == 1) {
			or.setPay_status("Not pay yet");

			if (repco.Checkout(or, listc)) {
				request.getSession().removeAttribute("cameFromCart");
			}
		} else if (or.getPayment_id() == 2) {
			request.getSession().setAttribute("ordercheckout", or);
			momoService.processPayment(or, response);
			return null;
		}
		return "redirect:/order/showorder";
	}

	private String removeAccent(String s) {
		String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(temp).replaceAll("").replace('đ', 'd').replace('Đ', 'D');
	}

	private String generateOrderId() {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder orderId = new StringBuilder(16);
		SecureRandom random = new SecureRandom();
		for (int i = 0; i < 16; i++) {
			int index = random.nextInt(characters.length());
			orderId.append(characters.charAt(index));
		}
		return orderId.toString();
	}

	@GetMapping("/payment-result")
	public String paymentResult(@RequestParam Map<String, String> params, HttpSession session,
			HttpServletRequest request

	) {
		try {
			String resultCode = params.get("resultCode");
			String transId = params.get("transId");

			if ("0".equals(resultCode)) {

				session.removeAttribute("cameFromCart");
				Order or = (Order) request.getSession().getAttribute("ordercheckout");
				List<Shopping_cart> listc = repcart
						.findAllCartsByCustomerId((int) request.getSession().getAttribute("logined"));
				or.setPay_status("paid");
				or.setTransactionId(transId);
				if (repco.Checkout(or, listc)) {
					request.getSession().removeAttribute("cameFromCart");
				}
				return "redirect:/order/showorder";

			} else {

				return "redirect:/checkout/showcheckout";
			}

		} catch (Exception e) {

			session.setAttribute("paymentError", "Có lỗi xảy ra: " + e.getMessage());
			return "redirect:/checkout/showcheckout";
		}
	}

	@PostMapping("/payment-notify")
	@ResponseBody
	public ResponseEntity<?> ipnNotify(@RequestBody Map<String, String> body) {
		try {
			String resultCode = body.get("resultCode");
			String orderId = body.get("orderId");
			if ("0".equals(resultCode)) {
			} else {

			}
			return ResponseEntity.ok().body(Map.of("message", "Success"));

		} catch (Exception e) {

			return ResponseEntity.badRequest().body(Map.of("message", "Failed"));
		}
	}

	@GetMapping("/api/provinces")
	@ResponseBody
	public ResponseEntity<?> getProvinces() {
		try {
			return ResponseEntity.ok(ghnService.getProvinces());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error fetching provinces: " + e.getMessage());
		}
	}

	@GetMapping("/api/districts")
	@ResponseBody
	public ResponseEntity<?> getDistricts(@RequestParam Integer provinceId) {
		try {
			return ResponseEntity.ok(ghnService.getDistricts(provinceId));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error fetching districts: " + e.getMessage());
		}
	}

	@GetMapping("/api/wards")
	@ResponseBody
	public ResponseEntity<?> getWards(@RequestParam Integer districtId) {
		try {
			return ResponseEntity.ok(ghnService.getWards(districtId));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error fetching wards: " + e.getMessage());
		}
	}

	@GetMapping("/api/nearest")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> nearest(@RequestParam("address") String address,
			@RequestParam("districtId") int toDistrictId, HttpServletRequest request) {
		try {

			Warehouse nearestWarehouse = distanceService.findNearestWarehouse(address);
			List<Shopping_cart> listc = repcart
					.findAllCartsByCustomerId((int) request.getSession().getAttribute("logined"));
			if (nearestWarehouse != null) {
				
				double shippingFee = ghnService.calculateShippingFee(nearestWarehouse.getDistrict_Id(), toDistrictId,
						listc);

				
				Map<String, Object> response = new HashMap<>();
				response.put("id", nearestWarehouse.getId());
				response.put("name", nearestWarehouse.getName());
				response.put("address", nearestWarehouse.getAddress());
				response.put("lat", nearestWarehouse.getLat());
				response.put("lng", nearestWarehouse.getLng());
				response.put("districtId", nearestWarehouse.getDistrict_Id());
				response.put("shippingFee", shippingFee);

				return ResponseEntity.ok(response);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

}
