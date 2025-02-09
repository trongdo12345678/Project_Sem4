package com.customer.controller;

import java.security.SecureRandom;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.customer.repository.AccountRepository;
import com.customer.repository.CartRepository;
import com.customer.repository.CheckoutRepository;
import com.customer.repository.CouponRepository;
import com.customer.repository.DistanceService;
import com.customer.repository.GHNService;
import com.customer.repository.MomoService;
import com.customer.repository.ShoppingpageRepository;
import com.models.Brand;
import com.models.Category_Product;
import com.models.Coupon;
import com.models.Customer;
import com.models.Order;
import com.models.PageView;
import com.models.Shopping_cart;
import com.models.Warehouse;
import com.models.ghn.District;
import com.models.ghn.Province;
import com.models.ghn.Ward;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/v2/checkout")
public class RestCheckoutController {

    @Autowired
    private CartRepository cartRepository;
    
    @Autowired 
    private AccountRepository customerRepository;
    
    @Autowired
    private CouponRepository couponRepository;
  

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

    @GetMapping("/info/{customerId}")
    public ResponseEntity<?> getCheckoutInfo(@PathVariable int customerId) {
        try {
            
            List<Shopping_cart> cartItems = cartRepository.findAllCartsByCustomerId(customerId);
            
            
            double subtotal = cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

            
            Customer customer = customerRepository.finbyid(customerId);
            
            // Tạo response object
            Map<String, Object> response = new HashMap<>();
            response.put("cartItems", cartItems);
            response.put("customer", customer);
            response.put("subtotal", String.format("%.2f", subtotal));
            response.put("discount", "0.00");
            response.put("shipping", "0.00"); 
            response.put("total", String.format("%.2f", subtotal));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/provinces")
    @ResponseBody
    public ResponseEntity<?> getProvinces() {
        try {
            List<Province> provinces = ghnService.getProvinces();
            
            provinces.forEach(province -> {
                province.setProvinceName(removeDiacritics(province.getProvinceName()));
            });
            return ResponseEntity.ok(provinces);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching provinces: " + e.getMessage());
        }
    }

    @GetMapping("/districts")
    @ResponseBody
    public ResponseEntity<?> getDistricts(@RequestParam Integer provinceId) {
        try {
            List<District> districts = ghnService.getDistricts(provinceId);
            
            districts.forEach(district -> {
                district.setDistrictName(removeDiacritics(district.getDistrictName()));
            });
            return ResponseEntity.ok(districts);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching districts: " + e.getMessage());
        }
    }

    @GetMapping("/wards")
    @ResponseBody
    public ResponseEntity<?> getWards(@RequestParam Integer districtId) {
        try {
            List<Ward> wards = ghnService.getWards(districtId);
            
            wards.forEach(ward -> {
                ward.setWardName(removeDiacritics(ward.getWardName()));
            });
            return ResponseEntity.ok(wards);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching wards: " + e.getMessage());
        }
    }

    private String removeDiacritics(String text) {
        String temp = Normalizer.normalize(text, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = pattern.matcher(temp).replaceAll("");
        return temp.replaceAll("đ", "d").replaceAll("Đ", "D");
    }
    @GetMapping("/nearest")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> nearest(@RequestParam("address") String address,
			@RequestParam("districtId") int toDistrictId,@RequestParam("customerId") int customerId, HttpServletRequest request) {
		try {

			Warehouse nearestWarehouse = distanceService.findNearestWarehouse(address);
			List<Shopping_cart> listc = cartRepository
					.findAllCartsByCustomerId(customerId);
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
    @PostMapping("/gocheckout")
    @ResponseBody
    public ResponseEntity<?> checkoutAPI(@RequestBody Map<String, Object> checkoutData) {
        try {
            Order order = new Order();
            
            
            int customerId = (Integer) checkoutData.get("customerId");
            order.setCustomer_id(customerId);
            order.setCus_Name(checkoutData.get("customerName").toString());
            order.setPhone(checkoutData.get("phone").toString());
            order.setStatus("Waiting for confirmation");
            
            
            order.setPayment_id((Integer) checkoutData.get("paymentMethod"));
            order.setPay_status(checkoutData.get("payStatus").toString());
            
            
            order.setProvince_Id((Integer) checkoutData.get("provinceId"));
            order.setDistrict_Id((Integer) checkoutData.get("districtId"));
            order.setWard_Id(checkoutData.get("wardId").toString());
            order.setWareHouse_Id((Integer) checkoutData.get("warehouseId"));
            
            
            String fullAddress = checkoutData.get("fullAddress").toString();
            order.setAddress(fullAddress);
            
            
            order.setShippingFee(Double.parseDouble(checkoutData.get("shippingFee").toString()));
            order.setTotalAmount(Double.parseDouble(checkoutData.get("totalCartValue").toString()));
            order.setDiscount(Double.parseDouble(checkoutData.get("discount").toString()));
            order.setDate(LocalDate.now());
            order.setOrderID(generateOrderId());
            
            String transactionId = checkoutData.get("transactionId") != null ? 
                    checkoutData.get("transactionId").toString() : null;
                if (transactionId != null) {
                    order.setTransactionId(transactionId);
                }
            
            String couponId = checkoutData.get("couponId") != null ? 
                checkoutData.get("couponId").toString() : "";
            order.setCoupon_id(couponId.isEmpty() ? 0 : Integer.parseInt(couponId));
            
            
            String notes = checkoutData.get("notes") != null ? 
                checkoutData.get("notes").toString() : "";
            order.setNotes(notes);
            
            List<Shopping_cart> cartItems = cartRepository.findAllCartsByCustomerId(customerId);
            
            
            boolean success = repco.Checkout(order, cartItems);
            
            if (success) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Order placed successfully",
                    "orderId", order.getOrderID()
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Failed to place order"
                ));
            }
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Error: " + e.getMessage()
            ));
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
}
