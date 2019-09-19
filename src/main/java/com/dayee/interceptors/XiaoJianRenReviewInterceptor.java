package com.dayee.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.dayee.model.User;

public class XiaoJianRenReviewInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		User user = (User) request.getSession().getAttribute("user");
		if(user.getIsXiaoJianRen()!=null){
			request.getRequestDispatcher("/xiaojianren/review/list").forward(request, response);  
			return false;
		}else{
			return true;
		}
	}
}