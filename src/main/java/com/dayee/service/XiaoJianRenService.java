package com.dayee.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dayee.dao.XiaoJianRenDao;
import com.dayee.exception.XiaoJianRenException;
import com.dayee.model.Company;
import com.dayee.model.CompanySystem;
import com.dayee.model.InterfaceRequestData;
import com.dayee.model.PaidAccount;
import com.dayee.model.PaidAccountDetail;
import com.dayee.model.PrePaid;
import com.dayee.model.Systems;
import com.dayee.model.User;
import com.dayee.model.XiaoJianRenConsumptionLog;
import com.dayee.model.XiaoJianRenOptLog;
import com.dayee.utils.DateUtil;
import com.dayee.utils.EncryptUtils;
import com.dayee.utils.NumberUtil;
import com.dayee.utils.QueryUtil;
import com.dayee.utils.SendRequest;
import com.dayee.utils.SignatureUtils;
import com.dayee.utils.StringUtils;

@Service
public class XiaoJianRenService extends BaseService<XiaoJianRenConsumptionLog>{

	@Resource
	protected XiaoJianRenDao xiaoJianRenDao;
	@Resource
	protected PaidAccountService paidAccountService;
    @Resource
    private CompanySystemService companySystemService;
    @Resource
    private MoneyChangeLogService moneyChangeLogService;
	
	/*预扣款*/
	@Transactional(rollbackFor=Exception.class)
	public PrePaid prePaid(Double money,Integer paidAccountId,CompanySystem companySystem,Integer xiaoJianRenLogId) throws Exception{
		 log.info("开始进行预扣款业务处理====<=共享账号=>"+paidAccountId+"==>扣款金额===>"+money+",扣款企业信息==>"+getColumnValues(companySystem)+",xiaoJianRenLogId==>"+xiaoJianRenLogId);
		 
		 if(paidAccountId==null){
		     throw new XiaoJianRenException("共享账号ID不能为空");
		 }else if(money==null||money<=0){
			 throw new XiaoJianRenException("锁定金额异常");
		 }
		
	    PaidAccount paidAccount =  paidAccountService.queryById(paidAccountId);
	    if(paidAccount==null){
	        throw new XiaoJianRenException("共享账号不存在");
	    }else if(paidAccount.getState().equals(PaidAccount.INVALID)){
	        throw new XiaoJianRenException("无效的共享账号");
	    }else if(money>paidAccount.getRemainingMoney()) {
	        throw new XiaoJianRenException("金额不足");
	    }
		
		
	    PaidAccountDetail  paidAccountDetail  =  paidAccountService.changeMoney(companySystem,paidAccount, money, "扣款", PaidAccountService.TYPE_CONSUMPTION);
	    paidAccountDetail.setXiaoJianRenLogId(xiaoJianRenLogId);
	    updateObjectById(paidAccountDetail);
	
		updateObjectById(companySystem);
		noticeMoney(companySystem, paidAccount.getRemainingMoney(),paidAccount.getId());
		return null;
	}
	
	
	/*预扣款*/
	@Transactional(rollbackFor=Exception.class)
	public void refund(Integer logId,Integer paidAccountId,CompanySystem companySystem) throws Exception{
		 log.info("开始进行退款业务处理====<===>操作ID===>"+logId+",扣款企业信息==>"+getColumnValues(companySystem));
		if(logId==null){
			 throw new XiaoJianRenException("ID 不能为空!");
		}else if(paidAccountId==null){
            throw new XiaoJianRenException("共享账号ID不能为空");
        }
		PrePaid prePaid  =  queryById(logId,PrePaid.class);
		if(prePaid==null){
		    throw new XiaoJianRenException("logId 不能为存在!");
		}else if (prePaid.getType().equals(PrePaid.STATUS_UNLOCKING)){
		    throw new XiaoJianRenException("请不要重复提交!!");
		}
		log.info("查询到的日志信息===>"+getColumnValues(prePaid));
		
		PaidAccount paidAccount =  paidAccountService.queryById(paidAccountId);
	    if(paidAccount==null){
	            throw new XiaoJianRenException("共享账号不存在");
	    }
		
		paidAccountService.changeMoney(companySystem,paidAccount, prePaid.getMoney(), "解锁预扣额度", PaidAccountService.TYPE_RECHARGE);
		companySystemService.updateBalance(companySystem, prePaid.getMoney(), CompanySystemService.TYPE_RECHARGE_REFUND);
		prePaid.setId(null);
		prePaid.setMoney(NumberUtil.multiply(prePaid.getMoney(),-1));
		prePaid.setAddDate(new Date());
		prePaid.setType(PrePaid.STATUS_UNLOCKING);
		updateObjectById(companySystem);
		noticeMoney(companySystem,  paidAccount.getRemainingMoney(),paidAccount.getId());
		addObject(prePaid);
		
	}
	
	public void noticeMoney(CompanySystem companySystem,double balance,Integer paidAccountId) throws Exception{
		
		HashMap<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("corpSecret",companySystem.getSecretKey());
		parameter.put("corpCode",EncryptUtils.decode(companySystem.getSecretKey()).split("_")[0]);
		parameter.put("balance",NumberUtil.multiply(balance, 100));
		parameter.put("paidAccountId",paidAccountId);
		InterfaceRequestData data = new InterfaceRequestData(companySystem.getXiaoJianRenCallBackUrl()+"/api/sync/corp/balance", InterfaceRequestData.METHOD_POST, "", JSONArray.toJSONString(parameter), InterfaceRequestData.PARAMETER_TYPE_PARAMETER, ".+?state.+?", new Date());
	    addObject(data);
	}
	
	/*申请悬赏金*/
	@Transactional(rollbackFor=Exception.class)
	public void applyIssueMoney(XiaoJianRenConsumptionLog xiaoJianRenConsumptionLog,CompanySystem companySystem) throws Exception{
		 log.info("开始进行悬赏金申请业务操作=====>日志信息==>"+getColumnValues(xiaoJianRenConsumptionLog)+",===>企业信息==>"+getColumnValues(companySystem));
		 if(StringUtils.isEmpty(xiaoJianRenConsumptionLog.getRecommender())){
			 throw new XiaoJianRenException("recommender 不能为空!");
		 }else if(StringUtils.isEmpty(xiaoJianRenConsumptionLog.getRecommenderPhone())){
			 throw new XiaoJianRenException("recommenderPhone 不能为空!");
		 }else if(StringUtils.isEmpty(xiaoJianRenConsumptionLog.getRecommendPostName())){
			 throw new XiaoJianRenException("recommendPostName 不能为空!");
		 }else if(StringUtils.isEmpty(xiaoJianRenConsumptionLog.getOrgName())){
			 throw new XiaoJianRenException("orgName 不能为空!");
		 }else if(xiaoJianRenConsumptionLog.getOrgId()==null){
             throw new XiaoJianRenException("OrgId 不能为空!");
         }else if(StringUtils.isEmpty(xiaoJianRenConsumptionLog.getCandidate())){
			 throw new XiaoJianRenException("candidate 不能为空!");
		 }else if(StringUtils.isEmpty(xiaoJianRenConsumptionLog.getCandidatePhone())){
			 throw new XiaoJianRenException("candidatePhone 不能为空!");
		 }else if(StringUtils.isEmpty(xiaoJianRenConsumptionLog.getRecommendMode())){
			 throw new XiaoJianRenException("recommendMode 不能为空!");
		 }else if(xiaoJianRenConsumptionLog.getRecommendTime()==null){
			 throw new XiaoJianRenException("recommendTime 不能为空!");
		 }else if(xiaoJianRenConsumptionLog.getEntryTime()==null){
			 throw new XiaoJianRenException("entryTime 不能为空!");
		 }else if(xiaoJianRenConsumptionLog.getBonusId()==null){
			 throw new XiaoJianRenException("bonusId 不能为空!");
		 }else if(xiaoJianRenConsumptionLog.getReward()==null){
			 throw new XiaoJianRenException("悬赏金额 不能为空!");
		 }else if(xiaoJianRenConsumptionLog.getPaidAccountId()==null){
		     throw new XiaoJianRenException("共享账号不能为空!");
		 }else{
		     
		     PaidAccount  paidAccount  = paidAccountService.queryById(xiaoJianRenConsumptionLog.getPaidAccountId());
		     if(paidAccount==null){
		         throw new XiaoJianRenException("共享账号不存在!");
		     }
		     
			 XiaoJianRenConsumptionLog old =  queryOne(QueryUtil.createQuery().eq("bonusId",xiaoJianRenConsumptionLog.getBonusId()).get());
			 if(old!=null){
				 throw new XiaoJianRenException("不能重复申请悬赏金!");
			 }
			 xiaoJianRenConsumptionLog.setApplyTime(new Date());
			 xiaoJianRenConsumptionLog.setStatus(XiaoJianRenConsumptionLog.REVIEW_STATUS_UNAUDITED);
			 xiaoJianRenConsumptionLog.setCompanyId(companySystem.getId());
			 xiaoJianRenConsumptionLog.setCompanyName(companySystem.getCompanyName());
			 xiaoJianRenConsumptionLog.setPaidAccountName(paidAccount.getName());
			 addObject(xiaoJianRenConsumptionLog);
			 
			 companySystem.setApplyReward(NumberUtil.addTogether(companySystem.getApplyReward(),xiaoJianRenConsumptionLog.getReward()));
			 updateObjectById(companySystem);
			 
			 String url  = companySystem.getCallBackUrl();
			 Company company = queryById(companySystem.getCompanyId(),Company.class);
			 InterfaceRequestData interfaceRequestData = new InterfaceRequestData(url+"/mailResponse/"+company.getCode()+"/xjrCallback", InterfaceRequestData.METHOD_POST, "", JSONArray.toJSONString(xiaoJianRenConsumptionLog), InterfaceRequestData.PARAMETER_TYPE_BODY, "0", new Date());
			 addObject(interfaceRequestData);
		 }
	}
	
	/*hr审核通过*/
	@Transactional(rollbackFor=Exception.class)
	public synchronized void hrReview(int id,String hrName,CompanySystem companySystem) throws Exception{
		 log.info("开始进行HR审核业务操作=====>日志ID==>"+id+",===>HR姓名==>"+hrName+"企业信息===>"+getColumnValues(companySystem));
		 XiaoJianRenConsumptionLog xiaoJianRenConsumptionLog =  queryById(id);
		 if(xiaoJianRenConsumptionLog==null){
			 throw new XiaoJianRenException("id 不存在!");
		 }else if(StringUtils.isEmpty(hrName)){
			 throw new XiaoJianRenException("审核的HR不能为空!");
		 }else if(!xiaoJianRenConsumptionLog.getStatus().equals(XiaoJianRenConsumptionLog.REVIEW_STATUS_UNAUDITED)){
			 throw new XiaoJianRenException("无法进行审核!!");
		 }
		 
		 PaidAccount paidAccount = queryById(xiaoJianRenConsumptionLog.getPaidAccountId(),PaidAccount.class);
		 if(paidAccount.getRemainingMoney()<xiaoJianRenConsumptionLog.getReward()) {
		     throw new XiaoJianRenException("您企业充值的赏金额度已使用完毕，请先充值再使用!");
		 }
		 
		 
		 paidAccount.setNotIssuedMoney(NumberUtil.addTogether(paidAccount.getNotIssuedMoney(), xiaoJianRenConsumptionLog.getReward()));
		 updateObjectById(paidAccount);
		 
		 xiaoJianRenConsumptionLog.setStatus(XiaoJianRenConsumptionLog.REVIEW_STATUS_AUDITED);
		 xiaoJianRenConsumptionLog.setReviewTime(new Date());
		 xiaoJianRenConsumptionLog.setReviewer(hrName);
		 updateObjectById(xiaoJianRenConsumptionLog);
		
		 companySystem.setApplyReward( NumberUtil.subtraction(companySystem.getApplyReward(),xiaoJianRenConsumptionLog.getReward()));
		 updateObjectById(companySystem);
		 
		//扣款
         prePaid(xiaoJianRenConsumptionLog.getReward(),xiaoJianRenConsumptionLog.getPaidAccountId() , companySystem,xiaoJianRenConsumptionLog.getId());
		 
	}
	
	@Transactional(rollbackFor=Exception.class)
	public synchronized void issueRewards(User user,String id) throws Exception{
	    log.info("开始进行通知小荐人发放悬赏金=====>日志ID==>" + id);
        if (StringUtils.isEmpty(id)) {
            throw new XiaoJianRenException("id 不存在!");
        }
        String ids[] = id.split(",");
        Integer idArray[] = new Integer[ids.length];
        for (int i = 0; i < ids.length; i++) {
            idArray[i] = Integer.valueOf(ids[i]);
        }
        issueRewards(user,idArray);
	}
	
	@Transactional(rollbackFor=Exception.class)
	public synchronized void cancelIssueRewardstring(User user,String id) throws Exception{
        log.info("撤销已发放的小荐人悬赏金=====>日志ID==>" + id);
        if (StringUtils.isEmpty(id)) {
            throw new XiaoJianRenException("id 不存在!");
        }
        String ids[] = id.split(",");
        Integer idArray[] = new Integer[ids.length];
        for (int i = 0; i < ids.length; i++) {
            idArray[i] = Integer.valueOf(ids[i]);
        }
        
        List<XiaoJianRenConsumptionLog> consumptionLogList = query(QueryUtil.createQuery().in("id",idArray));
        List<String> recommenderList = new ArrayList<String>();
        for(XiaoJianRenConsumptionLog consumptionLog: consumptionLogList) {
              if(consumptionLog.getIssueDate().getTime()>new Date().getTime()) {
                  consumptionLog.setStatus(XiaoJianRenConsumptionLog.REVIEW_STATUS_AUDITED);
                  consumptionLog.setStatusNumber(0);
                  consumptionLog.setIssueDate(null);
                  updateObjectById(consumptionLog);
                  addOptLog(user, XiaoJianRenOptLog.CANCEL_ISSUE_REWARD, consumptionLog);
              }else {
                  recommenderList.add(consumptionLog.getRecommender());
              }
        }
        
        if(recommenderList.size()!=0) {
            String str = "如下推荐人:"+StringUtils.stitching(",", recommenderList);
            str+=",已过撤销时间,无法撤销!";
            throw new XiaoJianRenException(str);
        }
    }
	
	@Transactional(rollbackFor=Exception.class)
	public synchronized void issueRewards(User user,Integer ids[]) throws Exception{
        List<XiaoJianRenConsumptionLog> list =  query(QueryUtil.createQuery().in("id", ids));
        issueRewards(user,list);
	}
	
	@Transactional(rollbackFor=Exception.class)
	public synchronized void issueRewards(User user,List<XiaoJianRenConsumptionLog> xiaoJianRenList) throws Exception{
		
		for (int i = 0; i < xiaoJianRenList.size(); i++) {
			XiaoJianRenConsumptionLog xiaoJianRenConsumptionLog =xiaoJianRenList.get(i) ;
			
			if(xiaoJianRenConsumptionLog==null) {
			    throw new XiaoJianRenException("id 不存在!");
			}
			
			if(xiaoJianRenConsumptionLog.getStatus().equals(XiaoJianRenConsumptionLog.REVIEW_STATUS_AUDITED)) {
			    xiaoJianRenConsumptionLog.setStatus(XiaoJianRenConsumptionLog.REVIEW_STATUS_CONFIRM_SEND_CANCEL);
			    xiaoJianRenConsumptionLog.setIssueDate(DateUtil.afterHour(new Date(), 1));
			    updateObjectById(xiaoJianRenConsumptionLog);
			    addOptLog(user, XiaoJianRenOptLog.ISSUE_REWARD, xiaoJianRenConsumptionLog);
			    continue;
			}
			
			if (!xiaoJianRenConsumptionLog.getStatus().equals(
					XiaoJianRenConsumptionLog.REVIEW_STATUS_CONFIRM_SEND_CANCEL)) {
				log.error("id为:==>" + xiaoJianRenConsumptionLog.getId()
						+ "无法发放悬赏金,因为他的状态为==>"
						+ xiaoJianRenConsumptionLog.getStatus());
				continue;
			}
			CompanySystem companySystem = queryById(
					xiaoJianRenConsumptionLog.getCompanyId(),
					CompanySystem.class);

			String token = getToken(companySystem.getXiaoJianRenCallBackUrl());
			if(token==null) {
			    throw new XiaoJianRenException("发放失败==>小荐人token获取失败=>");
			}
			TreeMap<String, String> parameter = new TreeMap<String, String>();
			parameter.put("amount", String.valueOf(NumberUtil.multiplyToInt(
					xiaoJianRenConsumptionLog.getReward(), 100)));
			parameter.put("applyResult", "0");
			parameter.put("token", token);
			

			log.info("通知小荐人发送悬赏金===>"+parameter.toString());
			String interfaceUrl = null;
			if(xiaoJianRenConsumptionLog.getAuditNotWithApply()==null) {
			    parameter.put("bonusId", xiaoJianRenConsumptionLog.getBonusId());
			    interfaceUrl = "/api/cz/bonus";
			}else {
			    parameter.put("bounid", xiaoJianRenConsumptionLog.getBonusId());
	            parameter.put("status", "0");
			    interfaceUrl = "/api/reward/auditPass";
			}
			
			parameter.put("signatrue", SignatureUtils.getSignature(parameter));
			
			String body = SendRequest
                    .sendPost(
                              companySystem.getXiaoJianRenCallBackUrl()
                              + interfaceUrl,
                              new HashMap<String, String>(), parameter, "UTF-8")
                    .getBody();
            log.info("通知小荐人发送悬赏金===>返回结果==>"+body);
            JSONObject jSONObject = JSONArray.parseObject(body);
            String state = jSONObject.getString("state");
            if (!state.equals("200")) {
                throw new XiaoJianRenException( jSONObject.getString("msg")==null?jSONObject.getString("result"):jSONObject.getString("msg"));
            }
			
            PaidAccount paidAccount = queryById(xiaoJianRenConsumptionLog.getPaidAccountId(),PaidAccount.class);
            
            paidAccount.setNotIssuedMoney(NumberUtil.subtraction(paidAccount.getNotIssuedMoney(), xiaoJianRenConsumptionLog.getReward()));
            
            paidAccount.setIssuedMoney(NumberUtil.addTogether(paidAccount.getIssuedMoney(), xiaoJianRenConsumptionLog.getReward()));
            updateObjectById(paidAccount);
            
			companySystem.setXiaoJianRenIssuedMoney(NumberUtil.addTogether(companySystem.getXiaoJianRenIssuedMoney(), xiaoJianRenConsumptionLog.getReward()));
			updateObjectById(companySystem);
			
			xiaoJianRenConsumptionLog.setSendTime(new Date());
			xiaoJianRenConsumptionLog
					.setStatus(XiaoJianRenConsumptionLog.REVIEW_STATUS_CONFIRM_SEND);
			updateObjectById(xiaoJianRenConsumptionLog);

			Systems systems = queryById(CompanySystem.TYPE_XIAO_JIAN_REN, Systems.class);
			
			systems.setIssueMoney(NumberUtil.addTogether(systems.getIssueMoney(), xiaoJianRenConsumptionLog.getReward()));
			updateObjectById(systems);
			
			parameter = new TreeMap<String, String>();
			parameter.put("id", xiaoJianRenConsumptionLog.getId().toString());
			parameter.put("status", xiaoJianRenConsumptionLog.getStatus());
			parameter.put("sendTime",DateUtil.dateToString(xiaoJianRenConsumptionLog.getSendTime(),DateUtil.YYYY_MM_DD_HH_MM_SS));

			String url = companySystem.getCallBackUrl();
			Company company = queryById(companySystem.getCompanyId(),
					Company.class);
			InterfaceRequestData interfaceRequestData = new InterfaceRequestData(
					url + "/mailResponse/" + company.getCode()
							+ "/xjrUpdateLogStatus",
					InterfaceRequestData.METHOD_POST, "",
					JSONArray.toJSONString(parameter),
					InterfaceRequestData.PARAMETER_TYPE_PARAMETER, "0",new Date());
			addObject(interfaceRequestData);
		}
	}
	
	@Transactional(rollbackFor=Exception.class)
	public synchronized void receiveReward(String bonusId,CompanySystem companySystem,User user) throws Exception {
	    XiaoJianRenConsumptionLog xiaoJianRenConsumptionLog =  queryOne(QueryUtil.createQuery().eq("bonusId",bonusId).get());
        if(xiaoJianRenConsumptionLog==null){
            throw new XiaoJianRenException("悬赏金记录不存在!");
        }else if(!xiaoJianRenConsumptionLog.getStatus().equals(XiaoJianRenConsumptionLog.REVIEW_STATUS_CONFIRM_SEND)) {
            throw new XiaoJianRenException("悬赏金记录状态不是已发放,不能更改领取状态!!");
        }
        xiaoJianRenConsumptionLog.setStatus(XiaoJianRenConsumptionLog.REVIEW_STATUS_CONFIRM_SEND_RECEIVED);
        PaidAccount paidAccount = queryById(xiaoJianRenConsumptionLog.getPaidAccountId(),PaidAccount.class);
        Double receivedMoney = paidAccount.getReceivedMoney();
        paidAccount.setReceivedMoney(NumberUtil.addTogether(receivedMoney, xiaoJianRenConsumptionLog.getReward()));
        moneyChangeLogService.addLog(user, receivedMoney, paidAccount.getReceivedMoney(), MoneyChangeLogService.PAID_ACCOUNT_RECEIVED_MONEY, companySystem.getId());
	    updateObjectById(xiaoJianRenConsumptionLog);
	    updateObjectById(paidAccount);
	}
	
	public String getToken(String url) throws ClientProtocolException, IOException {
	    Map<String,String> params =  new HashMap<>();
	    params.put("appSecret","xjrOauthSecret");
	    params.put("appKey","1");
	    String body = SendRequest.sendPost(url+"/api/getToken", new HashMap<String,String>(), params, "UTF-8").getBody();
	    if(body.contains("state")) {
	        JSONObject result = JSONArray.parseObject(body);
	        String state = result.getString("state");
	        if(state.equals("200")) {
	            return result.getString("data");
	        }
	    }
	    return null;
	}
	
	@Transactional(rollbackFor=Exception.class)
	public synchronized void rechargeMerchantMoney(Integer companySystemId,double money,User user,String desc) throws Exception {
	    CompanySystem companySystem =  queryById(companySystemId, CompanySystem.class);
	    double oldMoney = companySystem.getMerchantRechargeMoney();
	    companySystem.setMerchantRechargeMoney(NumberUtil.addTogether(oldMoney, money));
	    
	    updateObjectById(companySystem);
	    moneyChangeLogService.addLog(user, oldMoney, companySystem.getMerchantRechargeMoney(), MoneyChangeLogService.COMPANY_SERVICE_MERCHANT_RECHARGE_MONEY,companySystem.getId(),desc);
	    updateMerchantMoney(companySystem, money, user);
	}
	
	public synchronized void updateMerchantMoney(CompanySystem companySystem,double money,User user) throws Exception {
	    double oldMoney = companySystem.getMerchantBalance();
	    companySystem.setMerchantBalance(NumberUtil.addTogether(oldMoney, money));
	    
	    updateObjectById(companySystem);
	    moneyChangeLogService.addLog(user, oldMoney, companySystem.getMerchantBalance(), MoneyChangeLogService.COMPANY_SERVICE_MERCHANT_BALANCE,companySystem.getId());
	}
	
    public synchronized void updateMerchantMoney(Integer companySystemId,double money,User user) throws Exception {
        CompanySystem companySystem =  queryById(companySystemId, CompanySystem.class);
        updateMerchantMoney(companySystem, money, user);
    }
    
    public void addOptLog(User user,String type,XiaoJianRenConsumptionLog consumptionLog) throws Exception {
        XiaoJianRenOptLog log = new XiaoJianRenOptLog();
        log.setCompanySystemId(consumptionLog.getCompanyId());
        log.setXiaoJianRenConsumptionLogId(consumptionLog.getId());
        log.setIp(user.getIp());
        log.setOptTime(new Date());
        log.setType(type);
        log.setUserName(user.getName());
        log.setUserId(user.getId());
        log.setInfo("用户"+user.getName()+"于"+DateUtil.dateToString(log.getOptTime())+type+consumptionLog.getRecommender()+"的赏金,金额"+consumptionLog.getReward()+"元,联系方式:"+user.getPhone());
        addObject(log);
    }
	
	public static void main(String[] args) throws Exception {
	   
        
        /*TreeMap<String, String> parameter = new TreeMap<String, String>();
        parameter.put("bounid", "11");
        parameter.put("status", "0");
        parameter.put("amount", "11");
        parameter.put("applyResult", "0");
        parameter.put("token", "111");
        parameter.put("signatrue", SignatureUtils.getSignature(parameter));
        
        
        String body = SendRequest
                .sendPost("http://180.168.28.98:8095/littleRecommender/api/reward/auditPass",
                          new HashMap<String, String>(), parameter, "UTF-8")
                .getBody();*/
        
        System.out.println(DateUtil.dateToString(new Date(),DateUtil.YYYY_MM_DD_HH_MM_SS));
    }
	
}