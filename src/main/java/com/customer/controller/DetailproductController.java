package com.customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.customer.repository.CommentRepository;
import com.customer.repository.DetailproductRepository;
import com.customer.repository.ShoppingpageRepository;
import com.models.Comment;
import com.models.PageView;
import com.models.Product;
import com.utils.Views;

@Controller
@RequestMapping("detailproduct")
public class DetailproductController {
	@Autowired
	private DetailproductRepository rep;

	@Autowired
	private ShoppingpageRepository repsp;

	@Autowired
	CommentRepository commentRepository;

	@GetMapping("/product")
	public String showpage(Model model, @RequestParam String id) {
		Product pro = rep.findId(Integer.parseInt(id));
		model.addAttribute("list_spe", rep.findprospeId(Integer.parseInt(id)));
	    model.addAttribute("productImages", rep.findProductImagesByProductId(Integer.parseInt(id)));
		model.addAttribute("product", pro);
		String[] statuses = { "NewRelease", "Active", "OutOfStock" };
		model.addAttribute("list_procate",
				repsp.findAllnopaging(new PageView(), "", new int[] { pro.getCate_id() }, new int[] {}, statuses));
		model.addAttribute("list_probrands",
				repsp.findAllnopaging(new PageView(), "", new int[] {}, new int[] { pro.getBrand_id() }, statuses));
		model.addAttribute("list_proother",
				repsp.findAllnopaging(new PageView(), "", new int[] {}, new int[] {}, statuses));
		List<Comment> comments = commentRepository.getCommentsByProductId(Integer.parseInt(id));
		
		model.addAttribute("comments", comments);
		return Views.CUS_DETAILPROPAGE;
	}
}
