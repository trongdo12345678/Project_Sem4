package com.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.admin.repository.BrandRepository;
import com.admin.repository.ProductRepository;
import com.models.Brand;
import com.models.PageView;
import com.models.Product;
import com.utils.Views;

@Controller
@RequestMapping("admin/brand")
public class BrandController {
	@Autowired
	private BrandRepository repbr;
	
	@Autowired
	private ProductRepository reppro;
	// thêm thương hiẹu
	@GetMapping("/showBrand")
	public String showBrand(Model model, @RequestParam(name = "cp", required = false, defaultValue = "1") int cp) {
		try {
			PageView pv = new PageView();
			pv.setPage_current(cp);
			pv.setPage_size(5);
			List<Brand> brands = repbr.findAll(pv);
			for (Brand brand : brands) {
				int relatedCount = repbr.countByBrandId(brand.getId());
				brand.setRelatedCount(relatedCount);
			}
			model.addAttribute("brands", brands);
			model.addAttribute("pv", pv);
			return Views.BRAND_SHOWBRAND;
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/error";
		}
	}
	
	
	@GetMapping("/showProductFromBrand")
	public String showProductFromBrand(@RequestParam("id") int brandId, Model model,
	                                   @RequestParam(name = "cp", required = false, defaultValue = "1") int cp) {
	    PageView pv = new PageView();
	    pv.setPage_current(cp);
	    pv.setPage_size(10);
	    List<Product> products = repbr.findByBrandId(brandId, pv);
	    Map<Integer, Boolean> productReferences = new HashMap<>();
	    for (Product product : products) {
	        boolean isReferenced = reppro.isProductReferenced(product.getId());
	        productReferences.put(product.getId(), isReferenced);
	    }

	    model.addAttribute("products", products);
	    model.addAttribute("productReferences", productReferences);
	    model.addAttribute("pv", pv);
	    model.addAttribute("brandId", brandId);

	    return "/admin/brand/showProductFromBrand";
	}

	// thêm thương hiệu
	@GetMapping("showAddBrand")
	public String showAddBrand(Model model) {
		try {
			Brand br = new Brand();
			model.addAttribute("new_item", br);
			return Views.BRAND_SHOWADDBRAND;
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/error";
		}
	}

	@PostMapping("addBrand")
	public String addBrand(@RequestParam String name, RedirectAttributes redirectAttributes) {
		try {
			Brand br = new Brand();
			br.setName(name);
			repbr.saveBr(br);
			redirectAttributes.addFlashAttribute("newBrandId", br.getId());
			redirectAttributes.addFlashAttribute("message", "✔ Brand added successfully!");
			return "redirect:/admin/brand/showBrand";
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", "Error adding brand: " + e.getMessage());
			return "redirect:/admin/brand/showBrand";
		}
	}

	// xóa thương hiệu
	@GetMapping("/deleteBr")
	public String deleteBr(@RequestParam String id, @RequestParam int cp, RedirectAttributes redirectAttributes) {
		try {
			int idb = Integer.parseInt(id);
			repbr.deleteBr(idb);
			PageView pv = new PageView();
			int totalCount = repbr.countBrand();
			int totalPage = (int) Math.ceil((double) totalCount / pv.getPage_size());
			if ((cp - 1) * pv.getPage_size() >= totalCount) {
				cp = cp > 1 ? cp - 1 : 1;
			}
			redirectAttributes.addFlashAttribute("message", "✔ Brand deleted successfully!");
			return "redirect:/admin/brand/showBrand?cp=" + cp;
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/error";
		}
	}

	// sửa lại thương hiệu
	@GetMapping("showUpdateBrand")
	public String showUpdateBrand(Model model, @RequestParam String id) {
		try {
			int idp = Integer.parseInt(id);
			model.addAttribute("up_item", repbr.findId(idp));
			return Views.BRAND_SHOWUPDATEBRAND;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return "redirect:/error";
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/error";
		}
	}

	@PostMapping("updateBr")
	public String updateBr(@RequestParam("id") int id, @RequestParam("name") String name,
			RedirectAttributes redirectAttributes) {
		try {
			Brand br = new Brand();
			br.setName(name);
			br.setId(id);
			repbr.updateBr(br);
			redirectAttributes.addFlashAttribute("message", "✔ Brand updated successfully!");
			return "redirect:/admin/brand/showBrand";
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", "Error updated brand: " + e.getMessage());
			return "redirect:/admin/brand/showBrand";
		}
	}
}
