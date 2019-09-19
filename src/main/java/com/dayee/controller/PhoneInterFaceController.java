package com.dayee.controller;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.dayee.model.CompanySystem;
import com.dayee.service.PhoneInterFaceServie;
import com.dayee.utils.QueryUtil;
import com.dayee.vo.KuaiYongYunResult;

@Controller
@RequestMapping("interface/phone")
public class PhoneInterFaceController extends BaseController{

	@Resource
	PhoneInterFaceServie phoneInterFaceServie;
	
	@RequestMapping("callPhone/{secretKey}")
	@ResponseBody
	public HashMap<String,Object> callPhone(@PathVariable("secretKey")String secretKey,String accountName,String caller,String called,String orgName,String systemType) throws Exception {

        log.info("调用拨打电话接口============>" + secretKey
                 + ",accountName==>"
                 + accountName
                 + ",caller==>"
                 + caller
                 + ",orgName=>"
                 + orgName);
        QueryUtil q = QueryUtil.createQuery().eq("secretKey", secretKey);
        List<CompanySystem> list = phoneInterFaceServie.query(q.get(), CompanySystem.class);
        log.info("企业信息===>" + JSONArray.toJSONString(list));
        if (list.size() != 0) {
            CompanySystem companySystem = list.get(0);
            if (companySystem.getBalance() <= 5) {
                return returnResult("-1", "失败,余额不足!");
            }
            String session = null;
            if (companySystem.getSystemId() == 1) {
                session = phoneInterFaceServie.callPhoneByKuaiYongYun(companySystem, orgName, accountName, caller,
                                                                      called, systemType);
            } else if (companySystem.getSystemId() == 5) {
                session = phoneInterFaceServie.callPhoneByRongYing(companySystem, orgName, accountName, caller, called,
                                                                   systemType);
            }
            HashMap<String, Object> returnResult = returnResult("0", "");
            returnResult.put("data", session);
            return returnResult;
        } else
            return returnResult("-1", "不存在");
	}
	
	@RequestMapping("query/companyInfo/{secretKey}")
	@ResponseBody
	public HashMap<String,Object> queryCompanyInfo(@PathVariable("secretKey")String secretKey) throws Exception {

        QueryUtil q = QueryUtil.createQuery().eq("secretKey", secretKey);
        List<CompanySystem> list = phoneInterFaceServie.query(q.get(), CompanySystem.class);
        if (list.size() != 0) {
            CompanySystem companySystem = list.get(0);
            HashMap<String, Object> map = returnResult("0", "");
            map.put("rechargeMoney", String.valueOf(companySystem.getRechargeMoney()));
            map.put("unitPrice", String.valueOf(companySystem.getUnit()));
            return map;
        } else
            return returnResult("-1", "不存在");
	}
	
	@RequestMapping("callBack")
	@ResponseBody
	public String callBack(@RequestBody KuaiYongYunResult kuaiYongYunResult ) throws Exception {
			log.info("电话回调进入====>");
			phoneInterFaceServie.callBack(kuaiYongYunResult.getBindUUId(), kuaiYongYunResult.getCalleeAcceptTime(), kuaiYongYunResult.getEndCallTime(), kuaiYongYunResult.getDuration(), kuaiYongYunResult.getRecordfile());
			return  "success";
	}
}