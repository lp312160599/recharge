package com.dayee.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dayee.model.MoneyChangeLog;
import com.dayee.model.XiaoJianRenConsumptionLog;
import com.dayee.model.XiaoJianRenOptLog;
import com.dayee.service.MoneyChangeLogService;
import com.dayee.service.XiaoJianRenService;
import com.dayee.utils.DateUtil;
import com.dayee.utils.QueryUtil;
import com.dayee.utils.StringUtils;
import com.dayee.vo.Orders;
import com.dayee.vo.Page;
import com.dayee.vo.QueryParameter;

@Controller
@RequestMapping("/xiaojianren")
public class XiaoJianRenController extends BaseController{

	@Resource
	private XiaoJianRenService xiaoJianRenService;
	@Resource
	private MoneyChangeLogService moneyChangeLogService;
	
	@RequestMapping("{companySystemId}/consumption/list")
	public String queryConsumption(@PathVariable("companySystemId")Integer companySystemId,XiaoJianRenConsumptionLog consumption,QueryParameter queryParameter,Orders orders,Page page,Model model) throws Exception{
		
		if(consumption==null) consumption= new XiaoJianRenConsumptionLog();
		queryParameter = getQueryParameter(queryParameter);
		QueryUtil queryUtil = QueryUtil.createQuery()
				.eq("status", XiaoJianRenConsumptionLog.REVIEW_STATUS_CONFIRM_SEND)
				.eq("companyId", companySystemId)
				.like("recommender",consumption.getRecommender())
				.like("candidate",consumption.getCandidate())
				.like("recommendPostName",consumption.getRecommendPostName())
				.like("candidateStatus",consumption.getCandidateStatus())
				.like("orgName",consumption.getOrgName())
				.betweenDate("sendTime",queryParameter.getStartDate(),queryParameter.getEndDate());
		
		List<XiaoJianRenConsumptionLog> consumptionList = xiaoJianRenService.query(queryUtil.get(),getPage(page), XiaoJianRenConsumptionLog.class, orders);
		model.addAttribute("consumptionList", consumptionList);
		model.addAttribute("consumption", consumption);
		model.addAttribute("companyId", companySystemId);
		addModel(model, page, queryParameter, orders);
		return "xiaojianren/consumption";
	}
	
	@RequestMapping("{companySystemId}/merchantRechargeDetails/list")
	public String queryMerchantRechargeDetails(@PathVariable("companySystemId")Integer companySystemId,QueryParameter queryParameter,Page page,Model model) throws Exception{
	    
	  
	    QueryUtil queryUtil = QueryUtil.createQuery()
	            .eq("changeColumnName", MoneyChangeLogService.COMPANY_SERVICE_MERCHANT_RECHARGE_MONEY)
	            .eq("companySystemId", companySystemId);

	    Orders orders = new Orders();
	    orders.setTimeOrder("addDate_desc");
	    
	    List<MoneyChangeLog> consumptionList = moneyChangeLogService.query(queryUtil.get(),getPage(page), orders);
	    model.addAttribute("consumptionList", consumptionList);
	    model.addAttribute("companySystemId", companySystemId);
	    addModel(model, page,queryParameter, orders);
	    return "xiaojianren/merchantRechargeDetails";
	}
	
	@RequestMapping("optLogDetails/list")
	public String queryOptLogDetails(QueryParameter queryParameter,XiaoJianRenOptLog log,Page page,Model model) throws Exception{
	    
	    if(log==null) log= new XiaoJianRenOptLog();
        queryParameter = getQueryParameter(queryParameter);
        
	    QueryUtil queryUtil = QueryUtil.createQuery()
	            .eq("type", log.getType())
	            .like("ip", log.getIp())
	            .like("userName", log.getUserName())
	            .betweenDate("optTime",queryParameter.getStartDate(),queryParameter.getEndDate());
	    
	    Orders orders = new Orders();
	    orders.setTimeOrder("optTime_desc");
	    
	    List<XiaoJianRenOptLog> optLogDetailsList = xiaoJianRenService.query(queryUtil.get(),getPage(page), XiaoJianRenOptLog.class,orders);
	    model.addAttribute("optLogDetailsList", optLogDetailsList);
	    model.addAttribute("log", log);
	    addModel(model, page,queryParameter, orders);
	    return "xiaojianren/log";
	}
	
	@RequestMapping("review/list")
	public String queryReview(XiaoJianRenConsumptionLog consumption,QueryParameter queryParameter,Page page,Model model) throws Exception{
		
		if(consumption==null) consumption= new XiaoJianRenConsumptionLog();
		queryParameter = getQueryParameter(queryParameter);
		QueryUtil queryUtil = QueryUtil.createQuery()
				.like("recommender",consumption.getRecommender())
				.like("candidate",consumption.getCandidate())
				.like("recommendPostName",consumption.getRecommendPostName())
				.like("candidateStatus",consumption.getCandidateStatus())
				.like("companyName",consumption.getCompanyName())
				.betweenDate("sendTime",queryParameter.getStartDate(),queryParameter.getEndDate());
		
 
		if(StringUtils.isNotEmpty(consumption.getStatus())){
			queryUtil.eq("status",consumption.getStatus() );
			
			if(consumption.getStatus().equals(XiaoJianRenConsumptionLog.REVIEW_STATUS_CONFIRM_SEND_CANCEL)) {
			    queryUtil.betweenDate("issueDate",DateUtil.dateToString(new Date()),null);
			}
		}
		
		Orders orders  = new Orders();
		orders.setStatusOrder("statusNumber_asc");
		orders.setTimeOrder("applyTime_desc");
	
		List<XiaoJianRenConsumptionLog> consumptionList = xiaoJianRenService.query(queryUtil.get(),getPage(page), XiaoJianRenConsumptionLog.class, orders);
		model.addAttribute("consumptionList", consumptionList);
		model.addAttribute("consumption", consumption);
		addModel(model, page, queryParameter, orders);
		return "xiaojianren/review";
	}
	
	@RequestMapping("issueRewards")
	@ResponseBody
	public HashMap<String,Object> issueReward (String id) throws Exception{
			xiaoJianRenService.issueRewards(getUser(),id);
			return returnResult("0","");
	}
	
	@RequestMapping("cancelIssueReward")
    @ResponseBody
    public HashMap<String,Object> cancelIssueReward (String id) throws Exception{
            xiaoJianRenService.cancelIssueRewardstring(getUser(),id);
            return returnResult("0","");
    }
	
	@RequestMapping("merchantRecharge")
    @ResponseBody
	public HashMap<String,Object> merchantRecharge(int companySystemId,double money,String desc) throws Exception{
	    xiaoJianRenService.rechargeMerchantMoney(companySystemId, money, getUser(),desc);
	    return returnResult("0","");
	}
	
}
