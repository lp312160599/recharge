package com.dayee.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HttpSession  httpSession  = request.getSession();
		Object o = httpSession.getAttribute("user");
		if(o==null){
			 request.getRequestDispatcher("/user/login/index").forward(request, response);  
			 return false;
		}else{
			return true;
		}
	}
}