package com.dayee.controller;

import java.util.Date;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dayee.model.SmsCode;
import com.dayee.model.User;
import com.dayee.service.UserService;
import com.dayee.utils.C123SendSmsClient;
import com.dayee.utils.DateUtil;
import com.dayee.utils.MD5Util;
import com.dayee.utils.QueryUtil;
import com.dayee.utils.StringUtils;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController{

	@Resource
	UserService userService ;
	
	@RequestMapping("login/index")
	public String loginIndex(Model model) throws Exception{
	    
		int userId = 0;
		Cookie cookie = getCookie("userId");
		if(cookie!=null&&StringUtils.isNotEmpty(cookie.getValue())){
			userId = Integer.valueOf(cookie.getValue());
		}
		
		if(userId!=0){
			User user = userService.queryById(userId);
			model.addAttribute("user",user);
			model.addAttribute("rememberMe","");
		}
		return "login";
	}

	@RequestMapping("login")
	public String login(User user,Integer rememberMe,String code,Model model) throws Exception{
		
	    if(user.getPhone()==null) {
	        model.addAttribute("message","用户不存在");
	        return"forward:/user/login/index";
	    }
	    
		String password = user.getPassword();
		user.setPassword(null);
		User u = userService.queryOne(QueryUtil.createQuery().eq("phone", user.getPhone()).eq("name", user.getName()));
		
		if(u==null){
			model.addAttribute("message","用户不存在");
		} else if(!(u.getPassword().equals(password)||u.getPassword().equals(MD5Util.toMd5(password)))){
			model.addAttribute("message","密码错误");
		}else if(StringUtils.isEmpty(code)) {
		    model.addAttribute("message","验证码为空");
		}else{
		       
		       SmsCode smsCode = userService.queryOne(QueryUtil.createQuery().eq("code", code).gt("validityPeriod", new Date().getTime()),SmsCode.class);
		    
		       if(smsCode==null) {
		           model.addAttribute("message","验证码错误");
		       }else {
		           setUserCookie(u,rememberMe);
		           if(u.getIsXiaoJianRen()!=null){
		               return"redirect:/xiaojianren/review/list";
		           }
		           return"redirect:/system/index";
		       }
		}
		
		return"forward:/user/login/index";
	}
	
	@RequestMapping("login/getSmsCode")
	@ResponseBody
    public HashMap<String, Object> getSmsCode(String phone) throws Exception{
	    if(StringUtils.isEmpty(phone)) {
	        return returnResult("-1","手机不能为空");
	    }else {
            User u = userService.queryOne(QueryUtil.createQuery().eq("phone", phone));
            if(u==null) {
                return returnResult("-1","手机号不存在!请联系管理员注册.");
            }
            SmsCode smsCode = userService.queryOne(QueryUtil.createQuery().eq("phone", phone).gt("validityPeriod", new Date().getTime()),SmsCode.class);
            if(smsCode!=null
               &&DateUtil.nowTimestampDifferenceBySecond(smsCode.getSendTimestamp())<120) {
                return returnResult("-1","喝杯咖啡稍等片刻!");
            }else {
                if(smsCode==null) {
                    smsCode = new SmsCode();
                }
                int code = (int) ((Math.random()*8999)+1000);
                
                C123SendSmsClient.send(phone,"验证码为： "+code+",请在页面完成输入完成验证。如非本人操作请忽略。");
                
                smsCode.setCode(code);
                smsCode.setPhone(phone);
                smsCode.setSendTimestamp(new Date().getTime());
                smsCode.setValidityPeriod(DateUtil.addNowTimestampByMinute(SmsCode.VALIDITY_PERIOD_MINUTE));
                if(smsCode.getId()==null)userService.addObject(smsCode);
                else userService.updateObjectById(smsCode);
                return returnSuccessResult();
            }
        }
	}
	
	@RequestMapping("logot")
	public String logot(Model model) throws Exception{
		 removeUser();
		 return"redirect:/user/login/index";
	}
	
	@RequestMapping("updatePassword")
	@ResponseBody
	public HashMap<String, Object> updatePassword(String oldPassword,String newPassword) throws Exception{
		
		User user = getUser();
		oldPassword = MD5Util.toMd5(oldPassword);
		if(!user.getPassword().equals(oldPassword)){
			return returnResult("-1","密码不正确");
		}else {
			newPassword = MD5Util.toMd5(newPassword);
			user.setPassword(newPassword);
			userService.updateObjectById(user);
			setUserCookie(user, null);
			return returnResultGoTip("0","密码修改成功,需要重新登录","重新登录","user/logot");
		}
	}
}