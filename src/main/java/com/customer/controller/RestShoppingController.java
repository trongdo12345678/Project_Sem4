package com.customer.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.customer.repository.ShoppingpageRepository;
import com.models.Brand;
import com.models.Category_Product;
import com.models.PageView;


@RestController
@RequestMapping("/api/v2/shopping")
public class RestShoppingController {
	@Autowired
	private ShoppingpageRepository rep;
	
	@GetMapping("/takebac") 
    public ResponseEntity<Map<String, Object>> getHomeData() {
        try {
            Map<String, Object> response = new HashMap<>();
            
            List<Category_Product> categories = rep.findAllCate();
            List<Brand> brands = rep.findAllBrand();
            
            response.put("categories", categories);
            response.put("brands", brands);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
	@GetMapping("/listpro")
    public ResponseEntity<?> getProducts(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "30") int pageSize
    ) {
        try {
        	int[] idCategories = {};
    		int[] idBrands = {};
    		
    		PageView pv = new PageView();
    		pv.setPage_current(page);
    		pv.setPage_size(pageSize);
    		String[] statuses = { "NewRelease", "Active", "OutOfStock" };

            Map<String, Object> response = new HashMap<>();
            response.put("products", rep.findAllpagingapi(pv, "", idCategories, idBrands, statuses));
 
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
	
	
}
