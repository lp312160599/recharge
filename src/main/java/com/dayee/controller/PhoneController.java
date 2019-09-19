package com.dayee.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dayee.model.CallConsumption;
import com.dayee.model.MonthlyDeductionLog;
import com.dayee.service.MonthlyDeductionLogService;
import com.dayee.service.PhoneService;
import com.dayee.utils.QueryUtil;
import com.dayee.vo.Orders;
import com.dayee.vo.Page;
import com.dayee.vo.QueryParameter;

@Controller
@RequestMapping("/phone")
public class PhoneController extends BaseController{

	@Resource
	private PhoneService phoneService;
	@Resource
	private MonthlyDeductionLogService monthlyDeductionLogService;
	
	@RequestMapping("{companyId}/consumption/list")
	public String queryConsumption(@PathVariable("companyId")Integer companyId,CallConsumption callConsumption,QueryParameter queryParameter,Orders orders,Page page,Model model) throws Exception{
		
		if(callConsumption==null) callConsumption= new CallConsumption();
		queryParameter = getQueryParameter(queryParameter);
		QueryUtil queryUtil = QueryUtil.createQuery().eq("money",callConsumption.getMoney())
				.eq("duration", callConsumption.getDuration())
				.eq("status", 0)
				.eq("unit",callConsumption.getUnit())
				.eq("sysCompanyId",callConsumption.getSysCompanyId())
				.like("accountNumber",callConsumption.getAccountNumber())
				.like("caller",callConsumption.getCaller())
				.like("orgName",callConsumption.getOrgName())
				.like("called",callConsumption.getCalled())
				.like("number",callConsumption.getNumber())
				.betweenDate("startTime",queryParameter.getStartDate(),queryParameter.getEndDate());
		
		List<CallConsumption> callConsumptionList = phoneService.query(queryUtil.get(),getPage(page), CallConsumption.class, orders);
		model.addAttribute("callConsumptionList", callConsumptionList);
		model.addAttribute("consumption", callConsumption);
		model.addAttribute("companyId", companyId);
		addModel(model, page, queryParameter, orders);
		return "phone/consumption";
	}
	
	    @RequestMapping("{companyId}/monthlyDeduction/list")
	    public String monthlyDeduction(@PathVariable("companyId")Integer companyId,Page page,Model model) throws Exception{
	        Orders order = new Orders();
	        order.setMonthlyDeductionOrder("id_desc");
	        List<MonthlyDeductionLog> logList =   monthlyDeductionLogService.query(QueryUtil.createQuery().eq("companySystemId", companyId).get(),getPage(page),MonthlyDeductionLog.class, order);
	        model.addAttribute("logList", logList);
	        model.addAttribute("page", page);
	        return "phone/monthlyDeduction";
	   }
	
}