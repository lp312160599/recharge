package com.dayee.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.dayee.model.CompanySystem;
import com.dayee.model.User;
import com.dayee.service.CompanyService;
import com.dayee.utils.SimpleDateFormatNew;
import com.dayee.vo.Orders;
import com.dayee.vo.Page;
import com.dayee.vo.QueryParameter;

public class BaseController {

	@Autowired
	protected HttpServletRequest request;

	@Autowired
	protected HttpServletResponse response;
	
	@Resource
	protected CompanyService companyService;
	
	
	protected CompanySystem companySystem;
	
	protected Logger log = Logger.getLogger(getClass());


	public HttpSession getSession(){
		return request.getSession();
	}
	
	public String createToken(){
		String token = UUID.randomUUID().toString();
		getSession().setAttribute("token",token);
		return token;
	}
	
	public void removeToken(String token){
		getSession().removeAttribute("token");
	}
	
	public boolean isExitsToken(String token){
		Object o = getSession().getAttribute("token");
		if(o==null||!o.equals(token)) return false;
		else return true;
	}
	
	public void addErrorMessage(String message,Model model){
		model.addAttribute("message",message);
	}
	
	public void setUserCookie(User user,Integer rememberMe){
	    user.setIp(request.getRemoteAddr());
		request.getSession().setAttribute("user",user);
		if(rememberMe!=null){
			Cookie c = new Cookie("userId",String.valueOf(user.getId()));
			c.setPath("/");
			response.addCookie(c);
		}else{
			Cookie c = getCookie("userId");
			if(c!=null){
				c.setPath("/");
				c.setMaxAge(0);
				c.setValue(null);
				response.addCookie(c);
			}
		}
	}
	
	public void removeUser(){
		request.getSession().setAttribute("user",null);
	}
	
	public Cookie getCookie(String name){
		Cookie cookie[] = request.getCookies();
		if(cookie==null) return null;
		for(Cookie c:cookie){
			if(c.getName().equals("userId")){
				
				return c;
			}
		}
		return null;
	}
	
	public User getUser(){
		User user = (User) request.getSession().getAttribute("user");
		return user;
	}
	
	public Page getPage(Page page){
		if(page==null) page = new Page();
		return page;
	}
	

	public Model addModel(Model model,Page page,QueryParameter queryParameter,Orders orders){
		model.addAttribute("page",page);
		model.addAttribute("queryParameter", queryParameter);
		model.addAttribute("orders", orders);
		return model;
	}
	
	public QueryParameter getQueryParameter(QueryParameter queryParameter){
		if(queryParameter==null) return new QueryParameter();
		else return queryParameter;
	}
	
	public HashMap<String,Object> returnResult(String error,String msg){
		HashMap<String,Object> map = new HashMap<String, Object>();
		map.put("error", error);
		map.put("msg", msg);
		return map;
	}
	
	public HashMap<String,Object> returnSuccessResult(){
        return returnResult("0","");
    }
	
	public HashMap<String,Object> returnResultGoTip(String error,String msg,String buttonText,String url){
		HashMap<String,Object> map = new HashMap<String, Object>();
		String uuid = UUID.randomUUID().toString();
		map.put("error", error);
		map.put("msg", msg);
		map.put("uuid", uuid);
		getSession().setAttribute(uuid+"_msg",msg);
		getSession().setAttribute(uuid+"_buttonText",buttonText);
		getSession().setAttribute(uuid+"_url",url);
		return map;
	}
	
	@InitBinder
	public void InitBinder(WebDataBinder dataBinder) {  
		SimpleDateFormatNew dateFormat = new SimpleDateFormatNew();  
	    dateFormat.setLenient(false);  
	    dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));  
	}

	public void setCompanySystem(CompanySystem companySystem) {
		this.companySystem = companySystem;
	}
}
