package com.admin.controller;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.repository.ConversionRepository;
import com.models.Conversion;
import com.utils.Views;


@Controller
@RequestMapping("admin")
public class ConversionController {
	
	@Autowired
	private ConversionRepository con;
    
    @GetMapping("/addConversion")
    public String addConversionForm(Model model) {
		model.addAttribute("units",con.findAllUnit());
        model.addAttribute("conversion", new Conversion());
        return Views.ADD_CONVERSION; 
    }

    @GetMapping("/conversion")
    @ResponseBody
    public List<Map<String, Object>> findAllConversions(@RequestParam("productId") int productId) {
        return con.findAllConversions(productId);
    }

    @PostMapping("/addConversion")
    @ResponseBody
    public ResponseEntity<?> addUnit(
            @RequestParam("product_id") int product_id,
            @RequestParam("from_unit_name") int from_unit_name,
            @RequestParam("to_unit_name") int to_unit_name,
            @RequestParam("conversion_rate") int conversion_rate) {
        try {
            Conversion conversion = new Conversion();
            conversion.setProduct_id(product_id);
            conversion.setFrom_unit_id(from_unit_name);
            conversion.setTo_unit_id(to_unit_name);
            conversion.setConversion_rate(conversion_rate);

            con.addConversion(conversion);

            String fromUnitName = con.getUnitNameById(from_unit_name);
            String toUnitName = con.getUnitNameById(to_unit_name);
            if (fromUnitName == null || toUnitName == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Invalid unit ID(s) provided"));
            }

            Map<String, Object> response = new HashMap<>();
            response.put("from_unit_name", fromUnitName);
            response.put("to_unit_name", toUnitName);
            response.put("conversion_rate", conversion_rate);
            System.out.println("Response sent to client: " + response); 

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to add conversion", "details", e.getMessage()));
        }
    }
    
    @DeleteMapping("/conversion/delete")
    @ResponseBody
    public ResponseEntity<String> deleteConversion(@RequestBody Map<String, Object> requestData) {
        try {
        	int conversionId = Integer.parseInt(requestData.get("id").toString());
            int productId = Integer.parseInt(requestData.get("product_id").toString());

            int rowsAffected = con.deleteConversion(conversionId, productId);

            if (rowsAffected == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Conversion not found or does not belong to the specified product.");
            }

            return ResponseEntity.ok("Conversion deleted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting conversion: " + e.getMessage());
        }
    }


    
    @GetMapping("/editConversion")
    public String editConversion(@RequestParam("id") int conversionId, Model model) {
		model.addAttribute("units",con.findAllUnit());
        Conversion conversion = con.findById(conversionId);
        model.addAttribute("conversion", conversion);
        return Views.UPDATE_CONVERSION; 
    }

    @PostMapping("/updateConversion")
    public String updateConversion(Conversion conversion) {
    	con.updateConversion(conversion);
        return "redirect:/admin/conversions";
    }
}