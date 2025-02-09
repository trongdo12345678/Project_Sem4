package com.interceptor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.customer.repository.AccountRepository;
import com.customer.repository.CartRepository;
import com.customer.repository.ShoppingpageRepository;
import com.models.Customer;
import com.models.Shopping_cart;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component // Thêm annotation này
public class HeaderInterceptor implements HandlerInterceptor {

	@Autowired
	private ShoppingpageRepository repca;
	@Autowired
	private AccountRepository repacc;
	@Autowired
	CartRepository repcart;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		request.setAttribute("categorys", repca.findAllCate());
		request.setAttribute("brands", repca.findAllBrand());
		if (request.getSession().getAttribute("logined") != null) {
			int idlogined = (int) request.getSession().getAttribute("logined");
			request.setAttribute("logined", idlogined);
			Customer cus = repacc.finbyid(idlogined);
			var name = cus.getFirst_name() + " " + cus.getLast_name();
			request.setAttribute("loginedname", name);
			List<Shopping_cart> listc = repcart.findAllCartsByCustomerId(idlogined);
			request.setAttribute("Cquantity", listc.size());
		} else {
			request.setAttribute("logined", null);
		}
		request.setAttribute("currentURI", request.getRequestURI());

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (modelAndView != null) {
			modelAndView.addObject("categorys", request.getAttribute("categorys"));
			modelAndView.addObject("logined", request.getAttribute("logined"));
			modelAndView.addObject("loginedname", request.getAttribute("loginedname"));
			modelAndView.addObject("Cquantity", request.getAttribute("Cquantity"));
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}
