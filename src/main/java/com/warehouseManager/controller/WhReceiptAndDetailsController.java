package com.warehouseManager.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.models.Conversion;
import com.models.PageView;
import com.models.Warehouse_receipt;
import com.models.Warehouse_receipt_detail;
import com.utils.Views;
import com.warehouseManager.repository.WhReceiptAndDetailsRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("warehouseManager/warehouseReceipt")
public class WhReceiptAndDetailsController {
		@Autowired
		private WhReceiptAndDetailsRepository repwd;

		//show phiếu nhập kho
		@GetMapping("showWhReceipt")
		public String showWhReceipt(Model model, @RequestParam(name = "cp", required = false, defaultValue = "1") int cp, HttpSession session) {
		    try {
		        PageView pv = new PageView();
		        pv.setPage_current(cp);
		        pv.setPage_size(10);
		        int employeeId = repwd.getEmployeeIdFromSession(session);
		        List<Warehouse_receipt> whr = repwd.findAll(pv, employeeId);
		        model.addAttribute("whrs", whr);
		        model.addAttribute("pv", pv);
		        return Views.SHOW_WAREHOUSE_RECEIPT;
		    } catch (Exception e) {
		        e.printStackTrace();
		        return "error";
		    }
		}

		//show chi tiết phiếu nhập kho
		@GetMapping("/showWhReceiptDetail")
		public String showWhReceiptDetail(
		        @RequestParam("id") String WhrId,
		        @RequestParam("id") String WhrdId,
		        @RequestParam(value = "page", defaultValue = "1") int page,
		        @RequestParam(value = "size", defaultValue = "2") int size,
		        Model model) {
		    try {
		        int idwhr = Integer.parseInt(WhrId);
		        Warehouse_receipt whr = repwd.findId(idwhr);

		        PageView pageView = new PageView();
		        pageView.setPage_current(page);
		        pageView.setPage_size(size);

		        int idwhrs = Integer.parseInt(WhrdId);
		        List<Warehouse_receipt_detail> details = repwd.findDetailsByReceiptId(idwhrs, pageView);

		        model.addAttribute("details", details);
		        model.addAttribute("whr", whr);
		        model.addAttribute("products", repwd.findAllPro());
		        model.addAttribute("pageView", pageView);

		        return Views.SHOW_WAREHOUSE_RECEIPT_DETAILS;
		    } catch (Exception e) {
		        e.printStackTrace();
		        return "error";
		    }
		}


		// thêm phiếu nhập kho và chi tiết
		@GetMapping("showAddWhReceipt")
		public String showAddWhReceipt(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		    try {
		        int employeeId = repwd.getEmployeeIdFromSession(session);
		        Integer warehouseId = (Integer) session.getAttribute("warehouseId"); 
		        if (warehouseId == null) {
		            warehouseId = repwd.getWarehouseIdByEmployeeId(employeeId);
		            if (warehouseId != null) {
		                session.setAttribute("warehouseId", warehouseId);
		            } else {
		                redirectAttributes.addFlashAttribute("error", "You have not been assigned to manage any warehouse yet!");
		                return "redirect:showWhReceipt";
		            }
		        }
		       
		        model.addAttribute("whId", warehouseId);
		        model.addAttribute("products", repwd.findAllPro());
		        model.addAttribute("employeeId", employeeId);

		        return Views.ADD_WAREHOUSE_RECEIPT;
		    } catch (Exception e) {
		        e.printStackTrace();
		        return "error";
		    }
		}

		@PostMapping("/addWhReceipt")
		public String addWhReceipt(
		        @RequestParam("wh_id") int wh_id,
		        @RequestParam("status") String status,
		        @RequestParam("shippingFee") double shippingFee,
		        @RequestParam(value = "otherFee", defaultValue = "0") double otherFee,
		        @RequestParam("employeeId") int employeeId,
		        @RequestParam List<Integer> quantity,
		        @RequestParam List<Double> wh_price,
		        @RequestParam List<Integer> product_id,
		        @RequestParam List<Integer> conversionId,
		        @RequestParam List<String> statusDetails,
		        Model model, RedirectAttributes redirectAttributes) {

		    Warehouse_receipt receipt = new Warehouse_receipt();
		    
		    receipt.setName(repwd.generateReceiptName());
		    receipt.setWh_id(wh_id);
		    receipt.setStatus(status);
		    receipt.setShipping_fee(shippingFee);
		    receipt.setOther_fee(otherFee);
		    receipt.setEmployee_id(employeeId);
		    receipt.setDate(LocalDateTime.now());
		    
		    List<Warehouse_receipt_detail> details = new ArrayList<>();
		    for (int i = 0; i < quantity.size(); i++) {
		        Warehouse_receipt_detail detail = new Warehouse_receipt_detail();
		        detail.setQuantity(quantity.get(i));
		        detail.setWh_price(wh_price.get(i));
		        detail.setProduct_id(product_id.get(i));
		        detail.setConversion_id(conversionId.get(i));
		        detail.setStatus(statusDetails.get(i));
		        details.add(detail);
		    }

		    // Lấy ID của hóa đơn nhập kho
		    int generatedReceiptId = repwd.addRequestOrderWithDetails(receipt, details);

		    if (generatedReceiptId > 0) {
		        redirectAttributes.addFlashAttribute("newReceiptId", generatedReceiptId);
		        redirectAttributes.addFlashAttribute("message", "✔ Imported goods successfully!");
		    } else {
		        model.addAttribute("message", "Adding warehouse receipt failed.");
		    }
		    
		    return "redirect:showWhReceipt";
		}

		
		//randum tên phiếu
		 @GetMapping("/generateReceiptName")
	    @ResponseBody
	    public String generateReceiptName() {
	        String receiptName = repwd.generateReceiptName();
	        return receiptName;
	    }
		
		 // lấy đơn vị theo product
		@GetMapping("/getConversions")
		@ResponseBody
		public List<Conversion> getConversions(@RequestParam("id") int productId) {
		    try {
		        return repwd.getAllConversions(productId);
		    } catch (Exception e) {
		        e.printStackTrace();
		        return Collections.emptyList();
		    }
		}

}