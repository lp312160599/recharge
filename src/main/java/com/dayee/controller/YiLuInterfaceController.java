package com.dayee.controller;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/yiLu/api")
public class YiLuInterfaceController extends BaseController{

	@RequestMapping("queryCompanyInfo")
	@ResponseBody
	public HashMap<String,Object> queryBalance(){
		HashMap<String,Object> result = returnResult("0","");
		result.put("rechage",companySystem.getRechargeMoney()); 
		result.put("validityPeriod",companySystem.getValidityPeriod());
		return result;
	}
}
