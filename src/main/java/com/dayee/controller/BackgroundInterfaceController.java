package com.dayee.controller;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dayee.model.BackgroundConsumptionLog;
import com.dayee.model.CompanySystem;
import com.dayee.service.BackgroundInterfaceService;
import com.dayee.utils.QueryUtil;

@Controller
@RequestMapping("interface/background")
public class BackgroundInterfaceController extends BaseController{

	@Resource
	private BackgroundInterfaceService backgroundInterfaceService;
	
	@RequestMapping("query/companyInfo/{secretKey}")
	@ResponseBody
	public HashMap<String,Object> queryCompanyInfo(@PathVariable("secretKey")String secretKey) throws Exception {

        QueryUtil q = QueryUtil.createQuery().eq("secretKey", secretKey);
        List<CompanySystem> list = backgroundInterfaceService.query(q.get(), CompanySystem.class);
        if (list.size() != 0) {
            CompanySystem companySystem = list.get(0);
            HashMap<String, Object> map = returnResult("0", "");
            map.put("backgroundSimple", String.valueOf(companySystem.getBackgroundSimple()));
            map.put("backgroundEducation", String.valueOf(companySystem.getBackgroundEducation()));
            map.put("backgroundPractical", String.valueOf(companySystem.getBackgroundPractical()));
            map.put("backgroundBusinessConflict", String.valueOf(companySystem.getBackgroundBusinessConflict()));
            map.put("backgroundBachelorScience", String.valueOf(companySystem.getBackgroundBachelorScience()));
            map.put("backgroundStandard", String.valueOf(companySystem.getBackgroundStandard()));
            map.put("backgroundEarlyEducation", String.valueOf(companySystem.getBackgroundEarlyEducation()));
            return map;
        } else
            return returnResult("-1", "不存在");
	}
	
	@RequestMapping("callBack/{secretKey}")
	@ResponseBody
	public HashMap<String,Object> callBack(@PathVariable("secretKey")String secretKey
			              ,@RequestBody BackgroundConsumptionLog backgroundConsumptionLog ) throws Exception {

        QueryUtil q = QueryUtil.createQuery().eq("secretKey", secretKey);
        List<CompanySystem> list = backgroundInterfaceService.query(q.get(), CompanySystem.class);
        if (list.size() != 0) {
            CompanySystem companySystem = list.get(0);
            backgroundConsumptionLog.setSysCompanyId(companySystem.getId());
            backgroundInterfaceService.addConsumptionLog(backgroundConsumptionLog, companySystem);
            return returnResult("0", "");
        } else
            return returnResult("-1", "不存在");
		
	}
	
	
}
