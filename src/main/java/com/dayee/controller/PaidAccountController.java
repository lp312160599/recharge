package com.dayee.controller;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dayee.model.CompanySystem;
import com.dayee.model.PaidAccount;
import com.dayee.service.PaidAccountService;
import com.dayee.utils.QueryUtil;
import com.dayee.vo.Orders;
import com.dayee.vo.Page;
import com.dayee.vo.QueryParameter;


@Controller
@RequestMapping("/paidAccount")
public class PaidAccountController extends BaseController{

    @Resource
    private PaidAccountService paidAccountService;
    
    @RequestMapping("{companySystemId}/minute/list")
    public String queryPaidAccountMinute(@PathVariable("companySystemId")Integer companySystemId,PaidAccount paidAccount,QueryParameter queryParameter,Orders orders,Page page,Model model) throws Exception{
        
        if(paidAccount==null) paidAccount= new PaidAccount();
        queryParameter = getQueryParameter(queryParameter);
        QueryUtil queryUtil = QueryUtil.createQuery().like("name",paidAccount.getName())
                .like("createUser",paidAccount.getCreateUser())
                .eq("companySystemId", companySystemId)
                .betweenDate("lastOptionTime",queryParameter.getStartTime(),queryParameter.getEndTime())
                .betweenDate("createTime",queryParameter.getStartDate(),queryParameter.getEndDate());
        
        if(paidAccount.getState()!=null
           &&!paidAccount.getState().equals("-1")){
            queryUtil.eq("state", paidAccount.getState());
        }

        List<PaidAccount> paidAccountList = paidAccountService.query(queryUtil.get(),getPage(page), PaidAccount.class, orders);
        CompanySystem c =  paidAccountService.queryById(companySystemId, CompanySystem.class);
        model.addAttribute("paidAccountList", paidAccountList);
        model.addAttribute("paidAccount", paidAccount);
        model.addAttribute("companyId", companySystemId);
        model.addAttribute("systemId",c.getSystemId() );
        model.addAttribute("sys",c );
        addModel(model, page, queryParameter, orders);
        return "company/paidAccountMinute";
    }
    
    @RequestMapping("{companySystemId}/money/list")
    public String queryPaidAccountMoney(@PathVariable("companySystemId")Integer companySystemId,PaidAccount paidAccount,QueryParameter queryParameter,Orders orders,Page page,Model model) throws Exception{
        
        if(paidAccount==null) paidAccount= new PaidAccount();
        queryParameter = getQueryParameter(queryParameter);
        QueryUtil queryUtil = QueryUtil.createQuery().like("name",paidAccount.getName())
                .like("createUser",paidAccount.getCreateUser())
                .eq("companySystemId", companySystemId)
                .betweenDate("lastOptionTime",queryParameter.getStartTime(),queryParameter.getEndTime())
                .betweenDate("createTime",queryParameter.getStartDate(),queryParameter.getEndDate());
        
        if(paidAccount.getState()!=null
                &&!paidAccount.getState().equals("-1")){
            queryUtil.eq("state", paidAccount.getState());
        }
        
        List<PaidAccount> paidAccountList = paidAccountService.query(queryUtil.get(),getPage(page), PaidAccount.class, orders);
        model.addAttribute("paidAccountList", paidAccountList);
        model.addAttribute("paidAccount", paidAccount);
        model.addAttribute("companyId", companySystemId);
        addModel(model, page, queryParameter, orders);
        return "company/paidAccountMoney";
    }
    
    @RequestMapping("add")
    @ResponseBody
    public HashMap<String, Object> addPaidAccount(PaidAccount paidAccount) throws Exception{
            paidAccountService.createNewPidAccount(paidAccount.getName(), paidAccount.getState(), paidAccount.getCompanySystemId());
            return returnSuccessResult();
    }
    
    @RequestMapping("updateState")
    @ResponseBody
    public HashMap<String,Object> updateState(String state,String paidAccountIds){
            String[]  ids = paidAccountIds.split(",");
            paidAccountService.updateState(state, ids);
            return returnSuccessResult();
    }
    
    @RequestMapping("distribution")
    @ResponseBody
    public HashMap<String, Object> distributionMinute(Integer minute,Integer fileSize,Integer companySystemId,Integer paidAccountId,String type,String description) throws Exception{
            paidAccountService.distributionMinute(minute,fileSize, companySystemId, paidAccountId, type, description);
            return returnSuccessResult();
    }
    
    @RequestMapping("distribution/money")
    @ResponseBody
    public HashMap<String, Object> distributionMinute(Double money,Integer companySystemId,Integer paidAccountId,String type,String description) throws Exception{
            paidAccountService.distributionMoney(money, companySystemId, paidAccountId, type, description);
            return returnSuccessResult();
    }
}
