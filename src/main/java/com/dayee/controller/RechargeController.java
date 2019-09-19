package com.dayee.controller;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dayee.model.CompanySystem;
import com.dayee.model.Recharge;
import com.dayee.model.RechargeAudit;
import com.dayee.service.RechargeService;
import com.dayee.utils.QueryUtil;
import com.dayee.vo.Orders;
import com.dayee.vo.Page;
import com.dayee.vo.QueryParameter;

@Controller
@RequestMapping("/recharge")
public class RechargeController extends BaseController{

	@Resource
	private RechargeService rechargeService;
	
	@RequestMapping("{companyId}/{systemId}/list")
	public String queryRecharge(@PathVariable("companyId")Integer companyId
			                   ,@PathVariable("systemId")Integer systemId,
			                    Recharge recharge,QueryParameter queryParameter,
			                    Orders orders,Page page,Model model) throws Exception{
		
		if(recharge==null) recharge= new Recharge();
		queryParameter = getQueryParameter(queryParameter);
		QueryUtil queryUtil = QueryUtil.createQuery().eq("money",recharge.getMoney())
		                       .eq("type", recharge.getType())
		                       .eq("state",recharge.getState())
		                       .eq("companyId",companyId)
		                       .eq("systemId",systemId)
		                       .like("description",recharge.getDescription())
		                       .like("auditor",recharge.getAuditor())
		                       .like("rechargeUser",recharge.getRechargeUser())
		                       .nowDate("createTime",queryParameter.getStartTime());
		
		orders = new Orders();
		orders.setTimeOrder("createTime_desc");
		
	    List<Recharge> rechargeList = rechargeService.query(queryUtil.get(),getPage(page), Recharge.class, orders);
	    CompanySystem c = rechargeService.queryById(systemId, CompanySystem.class);
	    model.addAttribute("rechargeList", rechargeList);
	    model.addAttribute("recharge", recharge);
	    model.addAttribute("companyId", companyId);
	    model.addAttribute("systemId", systemId);
	    model.addAttribute("sId", c.getSystemId());
	    addModel(model, page, queryParameter, orders);
		return "company/recharge";
	}
	
	@RequestMapping("{rechargeId}/rechargeAudit/list/no")
	@ResponseBody
	public List<RechargeAudit> queryRechargeAuditByNoOk(@PathVariable("rechargeId")Integer rechargeId,Model model) throws Exception{
		
		QueryUtil queryUtil = QueryUtil.createQuery().eq("rechargeId",rechargeId)
				                                     .notIn("state",new String[]{RechargeAudit.OK,RechargeAudit.FAIL});
		
		List<RechargeAudit> rechargeList = rechargeService.query(queryUtil.get(),RechargeAudit.class);
		
		return rechargeList;
	}
	
	@RequestMapping("{rechargeId}/rechargeAudit/list")
	@ResponseBody
	public List<RechargeAudit> queryRechargeAudit(@PathVariable("rechargeId")Integer rechargeId,Model model) throws Exception{
		
		QueryUtil queryUtil = QueryUtil.createQuery().eq("rechargeId",rechargeId);
		
		List<RechargeAudit> rechargeList = rechargeService.query(queryUtil.get(),RechargeAudit.class);
		
		return rechargeList;
	}
	
	@RequestMapping("{companyId}/{systemId}/add")
	@ResponseBody
    public HashMap<String,Object> addRecharge(@PathVariable("companyId")Integer companyId,@PathVariable("systemId")Integer systemId, Recharge recharge,@RequestParam("name") List<String> names,@RequestParam("email") List<String> emails) throws Exception{
	        rechargeService.addRecharge(companyId, systemId, recharge, names, emails, getUser());
	        return   returnResult("0", "");
    }
	
	
	@RequestMapping("{rechargeId}/{rechargeAuditId}/audit/{state}")
	public String auditRecharge(@PathVariable("rechargeId")Integer rechargeId,@PathVariable("rechargeAuditId")Integer rechargeAuditId,@PathVariable("state")Integer state,Model model) throws Exception{
			rechargeService.auditRecharge(rechargeId, rechargeAuditId, state);
			addErrorMessage("审核成功", model);
			return "tip/success";

	}
	
	@RequestMapping("{rechargeId}/audit/examine")
	@ResponseBody
	public HashMap<String,Object> examineAudit(@PathVariable("rechargeId")Integer rechargeId,@RequestParam("name") List<String> names,@RequestParam("email") List<String> emails,Model model) throws Exception{
			rechargeService.examineAudit(rechargeId, names, emails);
			return returnResult("0", "");
	}
	
	@RequestMapping("{rechargeId}/audit/repeat")
	@ResponseBody
	public HashMap<String,Object> repeatRecharge(@PathVariable("rechargeId")Integer rechargeId,@RequestParam("name") List<String> names,@RequestParam("email") List<String> emails,Model model) throws Exception{
			rechargeService.repeatRecharge(rechargeId, names, emails);
			return returnResult("0", "");
	}
}
