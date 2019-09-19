package com.dayee.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.dayee.dao.RechargeAuditDao;
import com.dayee.exception.RechargeAuditException;
import com.dayee.exception.RechargeException;
import com.dayee.model.Company;
import com.dayee.model.CompanySystem;
import com.dayee.model.InterfaceRequestData;
import com.dayee.model.Recharge;
import com.dayee.model.RechargeAudit;
import com.dayee.model.Systems;
import com.dayee.model.User;
import com.dayee.utils.NumberUtil;
import com.dayee.utils.QueryUtil;
import com.dayee.utils.StringUtils;
import com.dayee.vo.RechargeFreezeInfo;

@Component
public class RechargeService extends BaseService<Recharge>{

	@Resource
	private XiaoJianRenService xiaoJianRenService;
	@Resource
	private CompanySystemService companySystemService;
	@Resource
	private RechargeAuditDao rechargeAuditDao;
	
	@Transactional(rollbackFor=Exception.class)
	public void addRecharge(Integer companyId,Integer systemId, Recharge recharge,List<String> names,List<String> emails,User user) throws Exception{
		log.info("企业充值===>companyId:"+companyId+"==>systemId"+systemId+"===recharge"+getColumnValues(recharge)+"===>user==>"+getColumnValues(user));
		CompanySystem companySystem = queryById(systemId,CompanySystem.class);
		
		if(recharge.getMoney()==null){
		    recharge.setMoney(0d);
		}
		
		if(recharge.getType().equals(Recharge.TYPE_RECHARGE_REFUND)){
		    RechargeFreezeInfo  rechargeFreezeInfo =  rechargeAuditDao.sumRechargeUnderReview(companySystem.getId());
		    int remainingFileSize = companySystem.getRemainingFileSize()-(rechargeFreezeInfo.getFileSize()*1024*1024);
		   
		    if(recharge.getFileSize()!=null
		       &&(recharge.getFileSize()*1024*1024)>remainingFileSize){
		        throw new RechargeException("超出可退空间!!");
		    }
		    if(recharge.getMinute()!=null){
		        Integer second = recharge.getMinute()*60;
		        int remainingSecond =companySystem.getRemainingTime()-(rechargeFreezeInfo.getMinute()*60);
		        if(second>remainingSecond){
		            throw new RechargeException("超出可退时间!!");
		        }
		        recharge.setMinute(-recharge.getMinute());
		    }
		    
		    if(recharge.getSpaceEndTime()!=null
		       &&(companySystem.getSpaceEndTime()==null||recharge.getSpaceEndTime().getTime()>companySystem.getSpaceEndTime().getTime())){
		        throw new RechargeException("不可大于已设结束时间!!");
		    }
	
            if(recharge.getFileSize()!=null){
                    recharge.setFileSize(-recharge.getFileSize());
           }
		    recharge.setMoney(-recharge.getMoney());
		}else if(companySystem.getSystemId().equals(CompanySystem.TYPE_DAYEE_VIDEO)
		        &&companySystem.getSpaceEndTime()==null){
		    recharge.setSpaceEndTime(new Date());
		}
		
		recharge.setCompanyId(companyId);
		recharge.setSystem(companySystem.getName());
		recharge.setRechargeUser(user.getName());
		recharge.setSystemId(systemId);
		
		addObject(recharge);
		addRechargeAudit(recharge, names, emails);
	}
	
	public void addRechargeAudit(Recharge recharge,List<String> names,List<String> emails) throws Exception{
		recharge.setState("审核中");
		for (int i = 0; i < names.size(); i++) {
			log.info("企业充值===>增加审核人==name:"+names.get(i)+"===>emails"+emails.get(i));
			RechargeAudit rechargeAudit = new RechargeAudit();
			rechargeAudit.setAuditor(names.get(i));
			rechargeAudit.setAuditorEmail(emails.get(i));
			rechargeAudit.setState(RechargeAudit.NO_SEND);
			rechargeAudit.setRechargeId(recharge.getId());
			names.set(i, "<span title=\""+recharge.getState()+"\">"+names.get(i)+"<span>");
			addObject(rechargeAudit);
		}
		recharge.setAuditor(StringUtils.stitching(",", names));
		updateObjectById(recharge);
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void repeatRecharge(Integer rechargeId,List<String> names,List<String> emails) throws Exception{
		log.info("重新发起审核===>rechargeId:"+rechargeId);
		QueryUtil queryUtil = QueryUtil.createQuery().eq("rechargeId", rechargeId);
		Recharge recharge = queryById(rechargeId,Recharge.class);
        List<RechargeAudit> rechargeAuditList = query(queryUtil.get(),RechargeAudit.class);
        deleteObjects(rechargeAuditList, RechargeAudit.class); 
        addRechargeAudit(recharge, names, emails);
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void examineAudit(Integer rechargeId,List<String> names,List<String> emails) throws Exception{
		log.info("催审核===>rechargeId:"+rechargeId);
		Recharge recharge = queryById(rechargeId,Recharge.class);
		
		QueryUtil queryUtil = QueryUtil.createQuery().notIn("state", new String[]{RechargeAudit.OK,RechargeAudit.FAIL})
                                                     .eq("rechargeId", rechargeId);
		
        List<RechargeAudit> rechargeAuditList = query(queryUtil.get(),RechargeAudit.class);
		 
        deleteObjects(rechargeAuditList, RechargeAudit.class); 
		
        addRechargeAudit(recharge, names, emails);
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void auditRecharge(Integer rechargeId,Integer rechargeAuditId,Integer state) throws Exception{
		log.info("验证充值审核==>rechargeId:"+rechargeId+"===>rechargeAuditId:"+rechargeAuditId+"===state:"+state);
		RechargeAudit rechargeAudit = queryById(rechargeAuditId,RechargeAudit.class);
		
		if(rechargeAudit==null)throw new RechargeAuditException("不存在的审核!!");
		
		if(rechargeAudit.getState().equals(RechargeAudit.OK)
		 ||rechargeAudit.getState().equals(RechargeAudit.FAIL)){
			log.info("已审核,请不要重复审核");
			throw new RechargeAuditException("已审核,请不要重复审核");
		}
		
		if(state==1)rechargeAudit.setState(RechargeAudit.OK);
		else if(state==2)rechargeAudit.setState(RechargeAudit.FAIL);
		updateObjectById(rechargeAudit);
		
		Recharge recharge = queryById(rechargeId,Recharge.class);
		
		if(recharge.getState().equals("已拒绝")){
			throw new RechargeAuditException("审核已拒绝,无法继续审核");
		}
		
		if(recharge.getState().equals("审核中")){
			 if(rechargeAudit.getState().equals(RechargeAudit.FAIL)){
				 recharge.setState("已拒绝");
			 }else if(rechargeAudit.getState().equals(RechargeAudit.OK)){
				 QueryUtil queryUtil = QueryUtil.createQuery().in("state", new String[]{RechargeAudit.NO_SEND,RechargeAudit.NO_AUDIT})
                         .eq("rechargeId", rechargeId);
                 List<RechargeAudit> rechargeAuditList = query(queryUtil.get(),RechargeAudit.class);

                 if(rechargeAuditList.size()==0){
                	 
                	 CompanySystem companySystem = queryById(recharge.getSystemId(),CompanySystem.class);
                	 
                	 companySystem.setRechargeMoney(NumberUtil.addTogether(companySystem.getRechargeMoney(),recharge.getMoney()));
                	 companySystemService.updateBalance(companySystem, recharge.getMoney(),recharge.getType());
                	 recharge.setValidityPeriod(companySystem.getValidityPeriod());
                	 recharge.setState("已通过");
    				 updateObjectById(recharge);
    				 
    				 Systems system = queryById(companySystem.getSystemId(),Systems.class);
    				 system.setRecharge( NumberUtil.addTogether(system.getRecharge(), recharge.getMoney()));
    				
    				 
    				 if(companySystem.getSystemId().equals(CompanySystem.TYPE_YILU)){
    					 Recharge r = new Recharge();
    					 r.setMoney(recharge.getMoney());
    					 r.setCreateTime(recharge.getCreateTime());
    					 r.setRechargeUser(recharge.getRechargeUser());
    					 r.setValidityPeriod(recharge.getValidityPeriod());
    					 Company company = queryById(companySystem.getCompanyId(),Company.class);
    					 InterfaceRequestData in = new InterfaceRequestData(companySystem.getCallBackUrl()+"/mailResponse/"+company.getCode()+"/yiLuRechageCallback", InterfaceRequestData.METHOD_POST,"", JSONArray.toJSONString(r), InterfaceRequestData.PARAMETER_TYPE_BODY, "0", new Date());
    					 addObject(in);
    				 } else if(companySystem.getSystemId().equals(CompanySystem.TYPE_DAYEE_VIDEO)){
    				     int second =  0;
    				     if(recharge.getMinute()!=null){
    				         second = recharge.getMinute()*60;
    				     }
    				     
    				     if(recharge.getFileSize()!=null){
    				         Integer fileSize = companySystem.getFileSize();
    				         Integer rechargeFileSize = recharge.getFileSize()*1024*1024;
    				         companySystem.setFileSize(fileSize+rechargeFileSize);
    				         companySystem.setRemainingFileSize(companySystem.getRemainingFileSize()+rechargeFileSize);
    				     }
    				     
    				     if(recharge.getSpaceEndTime()!=null){
    				         companySystem.setSpaceEndTime(recharge.getSpaceEndTime());
    				     }
    				     
    				     companySystem.setRechargeTime(companySystem.getRechargeTime()+second);
    				     companySystem.setRemainingTime(companySystem.getRemainingTime()+second);
    				     companySystem.setBalanceTime(companySystem.getBalanceTime()+second);
    				     system.setRechargeTime(system.getRechargeTime()+second);
    				     updateObjectById(companySystem);
                     }
    				 updateObjectById(system);
                 }
			 }
			 recharge.setAuditor(updateRechargeAuditor(rechargeId));
			 updateObjectById(recharge);
		}
	}

	public String updateRechargeAuditor(Integer rechargeId) throws Exception{
		
		QueryUtil  q = QueryUtil.createQuery().eq("rechargeId",rechargeId);
		List<RechargeAudit> rechargeAuditList = query(q.get(), RechargeAudit.class);
		String names = "";
		for (int i = 0; i < rechargeAuditList.size(); i++) {
			RechargeAudit rechargeAudit = rechargeAuditList.get(i);
			names += "<span title=\"审核"+rechargeAudit.getState()+"\" class=\"c_01\">"+rechargeAudit.getAuditor()+"<span>,";
		}
		names = names.substring(0,names.length()-1);
		return names;
	}
	
}
