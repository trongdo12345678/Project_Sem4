package com.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.admin.repository.CategoryRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component // Thêm annotation này
public class CartInterceptor implements HandlerInterceptor {

	@Autowired
	private CategoryRepository repca;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String ajaxHeader = request.getHeader("X-Requested-With");
		boolean isAjaxRequest = "XMLHttpRequest".equals(ajaxHeader);

		if (isAjaxRequest) {

			return true;

		} else {

			if (request.getSession().getAttribute("logined") != null) {

				request.setAttribute("logined", request.getSession().getAttribute("logined"));
				return true;
			} else {

				response.sendRedirect(request.getContextPath() + "/account/signin");
				return false;
			}
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}
