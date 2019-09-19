package com.dayee.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.dayee.dao.PhoneConsumptionDao;
import com.dayee.exception.CreditsException;
import com.dayee.exception.DayeeVideoConsumptionLogException;
import com.dayee.exception.ExceptionCode;
import com.dayee.exception.FileSizeException;
import com.dayee.model.Company;
import com.dayee.model.CompanySystem;
import com.dayee.model.CreditsLog;
import com.dayee.model.DayeeVideoConsumptionLog;
import com.dayee.model.InterfaceRequestData;
import com.dayee.model.PaidAccount;
import com.dayee.model.Systems;
import com.dayee.utils.DateUtil;
import com.dayee.utils.NumberUtil;
import com.dayee.utils.QueryUtil;
import com.dayee.utils.StringUtils;

@Service
public class CompanySystemService extends BaseService<CompanySystem> {

    public static String TYPE_RECHARGE = "充值";
    public static String TYPE_RECHARGE_REFUND = "退款";
    public static String TYPE_CONSUMPTION = "消费";
    
    @Resource
    private PhoneConsumptionDao phoneConsumptionDao;
    @Resource
    private PaidAccountService paidAccountService;
    @Resource
    private MoneyChangeLogService moneyChangeLogService;
    
    @Transactional(rollbackFor=Exception.class)
    public synchronized void updateBalance(CompanySystem companySystem,double money,String type) throws Exception{
        
         log.info("更新企业余额信息====>companySystem"+getColumnValues(companySystem)+",money==>"+money);
         
         double oldBalance = companySystem.getBalance();
        
         
         companySystem.setBalance(NumberUtil.addTogether(companySystem.getBalance(), money));
         
         Systems system = queryById(companySystem.getSystemId(),Systems.class);
         
         system.setBalance(NumberUtil.addTogether(system.getBalance(), money));
         
         if(CompanySystem.TYPE_KUAI_YONG_YUN==companySystem.getSystemId()
            ||CompanySystem.TYPE_RONG_YING==companySystem.getSystemId()){
             
             int duration = (int) (companySystem.getRechargeMoney()/companySystem.getUnit());
             companySystem.setDuration(duration);
             
             int remainingDuration = (int) (companySystem.getBalance()/companySystem.getUnit());
             companySystem.setRemainingDuration(remainingDuration);
             
             system.setCallMoney(phoneConsumptionDao.sumCallMoney(companySystem.getId()));
             
             if(type.equals(TYPE_CONSUMPTION)){
                 log.info("计算通话总额额===>");
                 
                 companySystem.setCountMoney(NumberUtil.addTogether((Math.abs(money)),companySystem.getCountMoney()));
                 log.info("计算通话总额===>"+companySystem.getCountMoney());
             }
                 
         }else if(CompanySystem.TYPE_YILU==companySystem.getSystemId()){
            double balance = companySystem.getBalance();
            int validityPeriod = (int) (balance/companySystem.getPrice());
            if(validityPeriod>0){
                double price = (companySystem.getPrice()*validityPeriod);
                balance = balance - price;
                Date d = null;
                if(companySystem.getValidityPeriod()==null) d = new Date();
                else d= companySystem.getValidityPeriod();
                d = DateUtil.afterYear(d, validityPeriod);
                companySystem.setValidityPeriod(d);
                system.setBalance(system.getBalance()-price);
            }
            companySystem.setBalance(balance);
         }
         
         log.info("更新企业余额信息====>system==>"+getColumnValues(system));
         
         updateObjectById(system);
         updateObjectById(companySystem);
         
         moneyChangeLogService.addLog(null, oldBalance, companySystem.getBalance(), MoneyChangeLogService.COMPANY_SERVICE_BALANCE,companySystem.getId());
         
        }
    
    @Transactional(rollbackFor=Exception.class)
    public void restoreFileSize(CompanySystem companySystem, String externalKey, Integer paidAccountId, String type)
            throws Exception {

        if (StringUtils.isEmpty("externalKey")) {
            throw new FileSizeException(ExceptionCode.ERROR_10009, "无效的externalKey");
        }

        if (paidAccountId == null) {
            throw new FileSizeException(ExceptionCode.ERROR_10002, "无效的共享账号ID");
        }

        PaidAccount paidAccount = queryById(paidAccountId, PaidAccount.class);

        if (paidAccount == null || paidAccount.getState().equals(PaidAccount.INVALID)) {
            throw new FileSizeException(ExceptionCode.ERROR_10003, "无效的共享账号");
        }
        DayeeVideoConsumptionLog log = queryOne(QueryUtil.createQuery().eq("externalKey",externalKey ),DayeeVideoConsumptionLog.class);
        
        if(log==null){
            throw new FileSizeException(ExceptionCode.ERROR_10009, "无效的externalKey");
        }else if(log.getFileSize()==null){
            throw new FileSizeException(ExceptionCode.ERROR_10010, "该流水无空间消耗!");
        }
        
        int f = (int)(log.getFileSize()*1024);
        paidAccountService.changeFileSize(companySystem, paidAccount, f, "恢复空间", type);
        paidAccountService.savePidAccountConsumptionLog(companySystem, paidAccount, 0, f, "恢复空间", type);
    }
    
    @Transactional(rollbackFor=Exception.class)
    public void deductingCredits(CompanySystem companySystem,Integer credits,Integer paidAccountId,String type) throws Exception{
             if(credits==null||credits<0){
                 throw new CreditsException(ExceptionCode.ERROR_10001,"无效的信用额度");
             }
             
             if(paidAccountId==null){
                 throw new CreditsException(ExceptionCode.ERROR_10002,"无效的共享账号ID");
             }
             
             PaidAccount paidAccount = queryById(paidAccountId, PaidAccount.class);
             
             if(paidAccount==null
                ||paidAccount.getState().equals(PaidAccount.INVALID)){
                 throw new CreditsException(ExceptionCode.ERROR_10003,"无效的共享账号");
             }
             
             if(type.equals(CreditsLog.TYPE_RESTORE)){
                 credits = -credits;
             }
             
             Integer creditsSecond = paidAccount.getCredits();
             Integer remainingCredits = creditsSecond-paidAccount.getUseCredits();
             
             if(credits>remainingCredits){
                 throw new CreditsException(ExceptionCode.ERROR_10004,"信用额度不足,扣除失败");
             }else{
                 paidAccount.setUseCredits(paidAccount.getUseCredits()+credits);
                 updateObjectById(paidAccount);
                 CreditsLog log = new CreditsLog();
                 log.setCompanySystemId(companySystem.getId());
                 log.setCreateTime(new Date());
                 log.setCredits(credits);
                 log.setRemainingCredits(remainingCredits-credits);
                 log.setType(type);
                 log.setPaidAccountId(paidAccountId);
                 addObject(log);
             }
    }
    
    @Transactional(rollbackFor=Exception.class)
    public void recordingVideoWater(CompanySystem companySystem,DayeeVideoConsumptionLog dayeeVideoConsumptionLog) throws Exception{
        
        List<DayeeVideoConsumptionLog> logList = query(QueryUtil.createQuery().eq("number", dayeeVideoConsumptionLog.getNumber()), DayeeVideoConsumptionLog.class);
        
        if(logList.size()!=0){
            throw new DayeeVideoConsumptionLogException(ExceptionCode.ERROR_10005,"该流水已经存在,请不要重复提交!");
        }
        
        if(dayeeVideoConsumptionLog.getPaidAccountId()==null){
            throw new DayeeVideoConsumptionLogException(ExceptionCode.ERROR_10002,"无效的共享账号ID");
        }
        
        PaidAccount paidAccount = queryById(dayeeVideoConsumptionLog.getPaidAccountId(), PaidAccount.class);
        
        if(paidAccount==null){
                 throw new DayeeVideoConsumptionLogException(ExceptionCode.ERROR_10003,"无效的共享账号");
        }
        
        Integer creditsSecond = paidAccount.getCredits();
        Integer  earlyWarningSeconds= dayeeVideoConsumptionLog.getCreditLimitSeconds()-dayeeVideoConsumptionLog.getActualSeconds();
        Integer remainingCredits = creditsSecond-paidAccount.getUseCredits()+dayeeVideoConsumptionLog.getCreditLimitSeconds();
        
        log.info("开始扣除信用额度!!==>");
        
        paidAccount.setUseCredits(paidAccount.getUseCredits()-earlyWarningSeconds);
        int fileSize = (int)(dayeeVideoConsumptionLog.getFileSize()*1024);
        paidAccountService.changeMinute(companySystem,paidAccount, dayeeVideoConsumptionLog.getActualSeconds(),fileSize, "消费", TYPE_CONSUMPTION);
        log.info("更新共享账号!!==>");
        updateObjectById(paidAccount);
        log.info("储存信用额度日志!!==>");
        CreditsLog log = new CreditsLog();
        log.setCompanySystemId(companySystem.getId());
        log.setCreateTime(new Date());
        log.setCredits(earlyWarningSeconds);
        log.setRemainingCredits(remainingCredits);
        log.setType(CreditsLog.TYPE_RESTORE);
        log.setPaidAccountId(dayeeVideoConsumptionLog.getPaidAccountId());
        addObject(log);
        
        this.log.info("添加流水信息!!==>");
        dayeeVideoConsumptionLog.setCompanySystemId(companySystem.getId());
        dayeeVideoConsumptionLog.setShareCostId(paidAccount.getId());
        dayeeVideoConsumptionLog.setPaidAccountName(paidAccount.getName());
        dayeeVideoConsumptionLog.setCreateTime(new Date());
        addObject(dayeeVideoConsumptionLog);
        
        this.log.info("添加回调信息!!==>");
        Company company = queryById(companySystem.getCompanyId(),Company.class);
        InterfaceRequestData data = new InterfaceRequestData(companySystem.getCallBackUrlUnderline()+"/mailResponse/"+company.getCode()+"/dayeeVideoConsumptionBack", InterfaceRequestData.METHOD_POST, "", JSONArray.toJSONString(dayeeVideoConsumptionLog), InterfaceRequestData.PARAMETER_TYPE_BODY, "0",new Date());
        addObject(data);
        this.log.info("结束!!==>");
    }
    
    public Integer creditsMinuteToSecond(Integer credits){
        if(credits<60){
            return 1;
        }else {
            int minute = credits/60;
            minute += (credits%60!=0?1:0);
            return minute;
        }
    }
}