package com.dayee.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dayee.model.BackgroundConsumptionLog;
import com.dayee.service.BackgroundInterfaceService;
import com.dayee.utils.QueryUtil;
import com.dayee.vo.Orders;
import com.dayee.vo.Page;
import com.dayee.vo.QueryParameter;

@Controller
@RequestMapping("/background")
public class BackgroundController extends BaseController{

	@Resource
	private BackgroundInterfaceService backgroundInterfaceService;
	
	@RequestMapping("{companyId}/consumption/list")
	public String queryConsumption(@PathVariable("companyId")Integer companyId,BackgroundConsumptionLog consumption,QueryParameter queryParameter,Orders orders,Page page,Model model) throws Exception{
		
		if(consumption==null) consumption= new BackgroundConsumptionLog();
		queryParameter = getQueryParameter(queryParameter);
		QueryUtil queryUtil = QueryUtil.createQuery()
				.like("number",consumption.getNumber())
				.like("name",consumption.getName())
				.like("postName",consumption.getPostName())
				.betweenDate("sendTime",queryParameter.getStartDate(),queryParameter.getEndDate());
		
		List<BackgroundConsumptionLog> consumptionList = backgroundInterfaceService.query(queryUtil.get(),getPage(page), BackgroundConsumptionLog.class, orders);
		model.addAttribute("consumptionList", consumptionList);
		model.addAttribute("consumption", consumption);
		model.addAttribute("companyId", companyId);
		addModel(model, page, queryParameter, orders);
		return "background/consumption";
	}
	
}
