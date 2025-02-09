package com.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.customer.repository.ShoppingpageRepository;
import com.models.PageView;
import com.utils.Views;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("shoppingpage")
public class ShoppingpageController {

	@Autowired
	private ShoppingpageRepository rep;

	@GetMapping("")
	public String showpage(Model model, @RequestParam(name = "cp", required = false, defaultValue = "1") int cp,
			HttpServletRequest request) {

		PageView pv = new PageView();
		pv.setPage_current(cp);
		pv.setPage_size(12);

		// Retrieve selected filters from session or initialize them
		int[] idCategories = (int[]) request.getSession().getAttribute("selectedCategories");
		int[] idBrands = (int[]) request.getSession().getAttribute("selectedBrands");
		String stringsearch = (String) request.getSession().getAttribute("gosppws");
		;

		if (idCategories == null) {
			idCategories = new int[0];
		}
		if (idBrands == null) {
			idBrands = new int[0];
		}
		if (stringsearch == null) {
			stringsearch = "";
			model.addAttribute("stringsearch", null);
		} else {
			model.addAttribute("stringsearch", stringsearch);
		}

		String[] statuses = { "NewRelease", "Active", "OutOfStock" };

		model.addAttribute("pronewar", rep.findAllpagingapi(pv, stringsearch, idCategories, idBrands, statuses));

		model.addAttribute("pv", pv);
		model.addAttribute("selectedBrands", idBrands);
		model.addAttribute("selectedCategories", idCategories);
		return Views.CUS_SHOPPINGPAGE;
	}

	@PostMapping("/filter")
	public String applyFilters(@RequestParam(name = "brands", required = false) int[] brandIds,
			@RequestParam(name = "categories", required = false) int[] categoryIds, HttpServletRequest request, // To
																												// store
																												// filters
																												// in
																												// the
																												// session
			Model model) {

		// Store the selected filters in the session
		request.getSession().setAttribute("selectedBrands", brandIds);
		request.getSession().setAttribute("selectedCategories", categoryIds);

		return "redirect:/shoppingpage"; // Redirect to the shopping page or another page
	}

	@GetMapping("/setcate/{id}")
	public String setcate(@PathVariable("id") int id, HttpServletRequest request, // To store filters in the session
			Model model) {
		int[] idCategories = new int[] { id };
		request.getSession().setAttribute("selectedBrands", null);
		request.getSession().setAttribute("selectedCategories", idCategories);

		return "redirect:/shoppingpage"; // Redirect to the shopping page or another page
	}

	@GetMapping("/setbrand/{id}")
	public String setbrand(@PathVariable("id") int id, HttpServletRequest request, // To store filters in the session
			Model model) {
		int[] idCategories = new int[] { id };
		request.getSession().setAttribute("selectedCategories", null);
		request.getSession().setAttribute("selectedBrands", idCategories);

		return "redirect:/shoppingpage"; // Redirect to the shopping page or another page
	}

	@GetMapping("/reset")
	public String reset(HttpServletRequest request, // To store filters in the session
			Model model) {

		// Store the selected filters in the session
		request.getSession().setAttribute("selectedBrands", null);
		request.getSession().setAttribute("selectedCategories", null);
		request.getSession().setAttribute("gosppws", null);

		return "redirect:/shoppingpage"; // Redirect to the shopping page or another page
	}

}
