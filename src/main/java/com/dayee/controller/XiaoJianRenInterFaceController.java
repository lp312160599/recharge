package com.dayee.controller;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dayee.model.PaidAccount;
import com.dayee.model.XiaoJianRenConsumptionLog;
import com.dayee.service.PaidAccountService;
import com.dayee.service.XiaoJianRenService;
import com.dayee.utils.NumberUtil;
import com.dayee.utils.QueryUtil;

@Controller
@RequestMapping("/xjr/api")
public class XiaoJianRenInterFaceController extends BaseController{

	@Resource
	private XiaoJianRenService xiaoJianRenService;
	@Resource
	private PaidAccountService paidAccountService;
	
	
	@RequestMapping("applyReward")
	@ResponseBody
	public HashMap<String,Object> applyReward(XiaoJianRenConsumptionLog xiaoJianRenConsumptionLog,int rewardAmount) throws Exception{
			double money = NumberUtil.division(rewardAmount, 100);
			xiaoJianRenConsumptionLog.setReward(money);
			xiaoJianRenService.applyIssueMoney(xiaoJianRenConsumptionLog, companySystem);
			return returnResult("0","");
	}
	
	@RequestMapping("hrReview/{id}")
	@ResponseBody
	public HashMap<String,Object> hrReview(@PathVariable(name="id")int id,String hrName) throws Exception{
			xiaoJianRenService.hrReview(id,hrName, companySystem);
			return returnResult("0","");
	}
	
	@RequestMapping("receiveReward")
    @ResponseBody
    public HashMap<String,Object> receiveReward(String bonusId) throws Exception{
            xiaoJianRenService.receiveReward(bonusId, companySystem,getUser());
            return returnResult("0","");
    }
	
	@RequestMapping("queryBalance")
	@ResponseBody
	public HashMap<String,Object> queryBalance(Integer paidAccountId) throws Exception{
		HashMap<String,Object> result = returnResult("0","");
		PaidAccount paidAccount = paidAccountService.queryById(paidAccountId);
		result.put("balance",paidAccount.getRemainingMoney());
		return result;
	}
	
	@RequestMapping("paidAccountList")
    @ResponseBody
    public HashMap<String,Object> getPaidAccountList() throws Exception{
            List<PaidAccount>  list = paidAccountService.query(QueryUtil.createQuery()
                                                               .eq("companySystemId",companySystem.getId())
                                                               .eq("state","有效"));
            HashMap<String,Object> map = returnSuccessResult();
            map.put("data", list);
            return map;

    }
}