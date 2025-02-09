package com.customer.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.models.PageView;
import com.models.Shopping_cart;


@RestController
@RequestMapping("/api/v2/cart")
public class RestCartController {
	@Autowired
    CartRepository rep;
	
    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCartsByCustomerId(@PathVariable int customerId) {
        try {
            List<Shopping_cart> carts = rep.findAllCartsByCustomerId(customerId);
            if (carts.isEmpty()) {
                return ResponseEntity.ok(new ArrayList<>());
            }
            return ResponseEntity.ok(carts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
    @PostMapping("/addtocart")
    public ResponseEntity<?> addToCart(
            @RequestParam("customerId") int customerId,
            @RequestParam("productId") int productId) {
        try {
            Shopping_cart cart = new Shopping_cart();
            cart.setCustomer_id(customerId);
            cart.setProduct_id(productId);
            cart.setQuantity(1);

            boolean success = rep.addToCartOrUpdate(cart);

            if (success) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Product added to cart successfully",
                    "data", Map.of(
                        "customerId", customerId,
                        "productId", productId
                    )
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Failed to add product to cart",
                    "error", "Database operation failed"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "An error occurred while adding product to cart",
                "error", e.getMessage()
            ));
        }
    }
    @PostMapping("/updatequantity")
    public ResponseEntity<?> updateQuantity(
            @RequestParam("cartId") int cartId,
            @RequestParam("action") String action) {
        
        boolean success;
        if (action.equals("plus")) {
            success = rep.plusProductQuantityInCart(cartId);
        } else if (action.equals("minus")) {
            success = rep.minusProductQuantityInCart(cartId);
        } else {
            return ResponseEntity.badRequest().body(Map.of("status", "invalid_action"));
        }

        if (success) {
            return ResponseEntity.ok(Map.of("status", "success"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("status", "failed"));
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteCartItem(@RequestParam("cartId") int cartId) {
        boolean success = rep.deleteCartById(cartId);

        if (success) {
            return ResponseEntity.ok(Map.of("status", "success"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("status", "failed"));
        }
    }
}
