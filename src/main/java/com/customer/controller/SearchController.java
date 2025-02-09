package com.customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.customer.repository.ShoppingpageRepository;
import com.models.PageView;
import com.models.Product;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("search")
public class SearchController {

	@Autowired
	ShoppingpageRepository rep;

	@GetMapping("/product")
	@ResponseBody
	public List<Product> searchproduct(@RequestParam String stringsearch) {
		PageView pv = new PageView();
		pv.setPage_current(1);
		pv.setPage_size(12);

		int[] idCategories = new int[0];
		int[] idBrands = new int[0];

		String[] statuses = { "NewRelease", "Active", "OutOfStock" };

		List<Product> listp = rep.findAllpaging(pv, stringsearch, idCategories, idBrands, statuses);

		return listp;
	}

	@GetMapping("/gosppws")
	public String gosppws(@RequestParam String search, HttpServletRequest request) {
		request.getSession().setAttribute("gosppws", search);
		return "redirect:/shoppingpage";
	}

}
