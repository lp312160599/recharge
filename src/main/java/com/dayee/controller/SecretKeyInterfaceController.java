package com.dayee.controller;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dayee.model.CompanySystem;
import com.dayee.model.SecurityCode;
import com.dayee.service.SecretKeyService;
import com.dayee.utils.QueryUtil;

@Controller
@RequestMapping("interface/secretKey")
public class SecretKeyInterfaceController extends BaseController{

	@Resource
	private SecretKeyService secretKeyService;
	
	@RequestMapping("query/securityCode/{secretKey}")
	@ResponseBody
	public HashMap<String,Object> querySecurityCode(@PathVariable("secretKey")String secretKey) throws Exception {

        QueryUtil q = QueryUtil.createQuery().eq("secretKey", secretKey);
        List<CompanySystem> list = secretKeyService.query(q.get(), CompanySystem.class);
        if (list.size() != 0) {
            CompanySystem companySystem = list.get(0);
            q = QueryUtil.createQuery().eq("companySystemId", companySystem.getId()).eq("state", "未使用");
            List<SecurityCode> securityCodeList = secretKeyService.query(q.get(), SecurityCode.class);
            HashMap<String, Object> map = returnResult("0", "");
            map.put("securityCodeList", securityCodeList);
            map.put("error", "0");
            return map;
        } else
            return returnResult("-1", "不存在");
	}
	
	@RequestMapping("set/securityCode/{secretKey}")
	@ResponseBody
	public HashMap<String,Object> setSecurityCode(@PathVariable("secretKey")String secretKey,@RequestBody List<String> codes) throws Exception {

        QueryUtil q = QueryUtil.createQuery().eq("secretKey", secretKey);
        List<CompanySystem> list = secretKeyService.query(q.get(), CompanySystem.class);
        if (list.size() != 0) {
            for (CompanySystem companySystem : list) {
                secretKeyService.setsetSecurityCode(secretKey, codes, companySystem);
            }
            return returnResult("0", "");
        } else
            return returnResult("-1", "不存在");
	}
}