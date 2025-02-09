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

import com.admin.repository.CategoryRepository;
import com.admin.repository.ProductRepository;
import com.models.Category_Product;
import com.models.PageView;
import com.models.Product;
import com.utils.Views;

@Controller
@RequestMapping("admin/category")
public class CategoryController {

	@Autowired
	private CategoryRepository repca;
	
	@Autowired
	private ProductRepository reppro;
	// show danh mục
	@GetMapping("/showCategory")
	public String showCategory(Model model, @RequestParam(name = "cp", required = false, defaultValue = "1") int cp) {
	    try {
	        PageView pv = new PageView();
	        pv.setPage_current(cp);
	        pv.setPage_size(5);
	        List<Category_Product> categories = repca.findAll(pv);
	        for (Category_Product category : categories) {
	            category.setRelatedCount(repca.countByCategoryId(category.getId()));
	        }
	        model.addAttribute("categories", categories);
	        model.addAttribute("pv", pv);
	        return Views.CATEGORY_SHOWCATEGORY;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "redirect:/error";
	    }
	}
	@GetMapping("/showProductFromCategory")
	public String showProductFromCategory(@RequestParam("id") int categoryId, Model model,
	                                       @RequestParam(name = "cp", required = false, defaultValue = "1") int cp) {
	    PageView pv = new PageView();
	    pv.setPage_current(cp);
	    pv.setPage_size(10);
	    List<Product> products = repca.findByCategoryId(categoryId, pv);
	    Map<Integer, Boolean> productReferences = new HashMap<>();
	    for (Product product : products) {
	        boolean isReferenced = reppro.isProductReferenced(product.getId());
	        productReferences.put(product.getId(), isReferenced);
	    }

	    model.addAttribute("products", products);
	    model.addAttribute("productReferences", productReferences);
	    model.addAttribute("pv", pv);
	    model.addAttribute("categoryId", categoryId);

	    return "/admin/category/showProductFromCategory";
	}


	// thêm danh mục 
	@GetMapping("showAddCategory")
	public String showAddCategory(Model model) {
	    try {
	        Category_Product ca  = new Category_Product();
	        model.addAttribute("new_item", ca);
	        return Views.CATEGORY_SHOWADDCATEGORY;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "redirect:/error";
	    }
	}
	@PostMapping("/addCategory")
	public String addCategory(@RequestParam String name, RedirectAttributes redirectAttributes) {
	    try {
	        Category_Product category = new Category_Product();
	        category.setName(name);
	        repca.saveCate(category);
	        redirectAttributes.addFlashAttribute("newCategoryId", category.getId());
	        redirectAttributes.addFlashAttribute("message", "✔ Category added successfully!");
	        return "redirect:/admin/category/showCategory";
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("error", "Error adding category: " + e.getMessage());
	        return "redirect:/admin/category/showCategory";
	    }
	}

	
	//xóa danh mục
	@GetMapping("/deleteCa")
	public String deleteCa(@RequestParam String id, @RequestParam int cp, RedirectAttributes redirectAttributes) {
	    try {
	        int idb = Integer.parseInt(id);
	        repca.deleteCa(idb);
	        PageView pv = new PageView();
	        int totalCount = repca.countCategories();
	        if ((cp - 1) * pv.getPage_size() >= totalCount) {
	            cp = cp > 1 ? cp - 1 : 1;
	        }
	        redirectAttributes.addFlashAttribute("message", "✔ Category deleted successfully!");
	        return "redirect:/admin/category/showCategory?cp=" + cp;
	    } catch (NumberFormatException e) {
	        e.printStackTrace();
	        return "redirect:/error";
	    }
	}

	
	// sửa danh mục
	@GetMapping("showUpdateCategory")
	public String showUpdateCategory(Model model, @RequestParam String id) {
	    try {
	        int idp = Integer.parseInt(id);
	        model.addAttribute("up_item", repca.findId(idp));
	        return Views.CATEGORY_SHOWUPDATECATEGORY;
	    } catch (NumberFormatException e) {
	        e.printStackTrace();
	        return "redirect:/error";
	    }
	}
	@PostMapping("updateCa")
	public String updateCa(@RequestParam("id") int id,
							@RequestParam("name") String name, RedirectAttributes redirectAttributes) {
		try {
			Category_Product ca  = new Category_Product();
			ca.setName(name);
			ca.setId(id);
			repca.updateCa(ca);
	        redirectAttributes.addFlashAttribute("message", "✔ Category updated successfully!");
	        return "redirect:/admin/category/showCategory";
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("error", "Error updating category: " + e.getMessage());
	        return "redirect:/admin/category/showCategory";
	    }
	}
}
