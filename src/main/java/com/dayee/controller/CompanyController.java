package com.dayee.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dayee.model.Company;
import com.dayee.model.CompanySystem;
import com.dayee.model.RechargeAudit;
import com.dayee.model.Systems;
import com.dayee.service.CompanyService;
import com.dayee.service.RechargeAuditService;
import com.dayee.utils.QueryUtil;
import com.dayee.vo.CompanySystemParameter;
import com.dayee.vo.Orders;
import com.dayee.vo.Page;
import com.dayee.vo.QueryParameter;

@Controller
@RequestMapping("/company")
public class CompanyController extends BaseController{

	@Resource
	private CompanyService companyService;
	@Resource
	private RechargeAuditService rechargeAuditService;
	
	@RequestMapping("list")
	public String queryCompany(QueryParameter queryParameter,Orders orders,Page page,Model model) throws Exception{
		
		List<Company> companyList = companyService.queryCompany(getQueryParameter(queryParameter),getPage(page),orders);
		model.addAttribute("companyList", companyList);
		model.addAttribute("system", companyService.query(Systems.class));
		addModel(model, page, queryParameter, orders);
		return "company/companyManager";
	}
	
	@RequestMapping("{systemId}/list")
	public String queryCallConsumption(@PathVariable("systemId") Integer systemId,QueryParameter queryParameter,Orders orders,Page page,Model model) throws Exception{
		
		queryParameter  = getQueryParameter(queryParameter);
		QueryUtil queryUtil = QueryUtil.createQuery().like("companyName",queryParameter.getCompanyName())
		                       .like("createUserName", queryParameter.getCreateUserName())
		                       .eq("name",queryParameter.getName())
		                       .eq("systemId",systemId)
		                       .betweenDate("createTime", queryParameter.getStartTime(), queryParameter.getEndTime())
		                       .betweenDate("spaceEndTime", queryParameter.getStartSpaceEndTime(), queryParameter.getEndSpaceEndTimeTime());
		
	    List<CompanySystem> companySystemList = companyService.query(queryUtil.get(),getPage(page), CompanySystem.class, orders);
	    model.addAttribute("companySystemList", companySystemList);
	    model.addAttribute("systemId", systemId);
	    List<RechargeAudit> rechargeAuditList = rechargeAuditService.queryCommonlyUsed();
	    StringBuffer rechargeAuditHtml = new StringBuffer();
	    rechargeAuditHtml.append("[");
	    for(RechargeAudit rechargeAudit:rechargeAuditList) {
	        if(systemId!=3||!"yysh@dayee.com".equals(rechargeAudit.getAuditorEmail())) {
	            String s  = rechargeAudit.getAuditor()+":"+rechargeAudit.getAuditorEmail();
	            rechargeAuditHtml.append("{name:'").append(s).append("',value:'").append(s).append("'},");
	        }
	    }
	    rechargeAuditHtml.append("]");
	    model.addAttribute("words", rechargeAuditHtml);
	    addModel(model, page, queryParameter, orders);
		return "company/details";
	}
	
	@RequestMapping("delete/{id}")
	@ResponseBody
	public HashMap<String, Object> delete(@PathVariable  int id) throws Exception{
	  companyService.deleteCompany(id);
	  return returnResult("0",null);
   }
	
	@RequestMapping("update/{id}")
	public String updateCompanyView1( @PathVariable("id")int id ,Model model) throws Exception{
		
		return updateCompanyView2(id, model);
	}
	
	@RequestMapping("update/2")
	public String updateCompanyView2(Integer id,Model model) throws Exception{
		String token = createToken();
		List<Systems> systemList =  companyService.query(Systems.class);
		model.addAttribute("token", token);
		model.addAttribute("id", id);
		model.addAttribute("systemList",systemList);
		if(id!=null){
			 QueryUtil queryUtil = QueryUtil.createQuery().eq("companyId", id);
			 List<CompanySystem> sysList = companyService.query(queryUtil.get(),CompanySystem.class);
			 
			 P:for (int i = 0; i < systemList.size(); i++) {
				  for (int j = 0; j < sysList.size(); j++) {
					 if(systemList.get(i).getId().equals(sysList.get(j).getSystemId())){
						 continue P;
					 }
				  }
				  CompanySystem c = new CompanySystem();
				  c.setName(systemList.get(i).getName());
				  c.setSystemId(systemList.get(i).getId());
				  sysList.add(c);
		     }
			 model.addAttribute("company", companyService.queryById(id));
			 model.addAttribute("sysList", sysList);
		}else{
			List<CompanySystem> sysList = new ArrayList<CompanySystem>();
			for (int i = 0; i < systemList.size(); i++) {
				CompanySystem c = new CompanySystem();
				c.setName(systemList.get(i).getName());
				c.setSystemId(systemList.get(i).getId());
				sysList.add(c);
			}
			model.addAttribute("sysList", sysList);
		}
		return "company/addCompany_2";
	}
	
	@RequestMapping("update")
	public String addCompany(Company company,String token,CompanySystemParameter para,Model model) throws Exception{
		if(!isExitsToken(token)){
			addErrorMessage("请不要重复提交", model);
			return "tip/error";
		}
		 
		removeToken(token);
		model.addAttribute("id", company.getId());
		model.addAttribute("name", company.getName());
		companyService.updateCompany(company, getUser(), para.getCompanySystemList());
	    return "company/addCompany_3";
   }
}