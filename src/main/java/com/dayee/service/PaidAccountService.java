package com.dayee.service;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dayee.dao.PaidAccountDao;
import com.dayee.exception.PaidAccountExcption;
import com.dayee.model.CompanySystem;
import com.dayee.model.CreditsLog;
import com.dayee.model.PaidAccount;
import com.dayee.model.PaidAccountDetail;
import com.dayee.utils.NumberUtil;
import com.dayee.vo.DurationSummary;

@Service
public class PaidAccountService extends BaseService<PaidAccount>{


    public static String TYPE_RECHARGE = "充值";
    public static String TYPE_RECHARGE_REFUND = "退款";
    public static String TYPE_CONSUMPTION = "消费";
    
    @Resource
    private PaidAccountDao paidAccountDao;
    @Resource
    private XiaoJianRenService xiaoJianRenService;
    @Resource
    private CompanySystemService companySystemService;
    
    public DurationSummary queryDurationSummary(CompanySystem companySystem){
        DurationSummary durationSummary = new DurationSummary();
        durationSummary.setCountTime(companySystem.getRechargeTime());
        durationSummary.setUseTime(companySystem.getUseTime());
        durationSummary.setRemainingTime(paidAccountDao.getAllPaidAccountRemainingTime(companySystem.getId()));
        return durationSummary;
    }
    
    public void createNewPidAccount(String name,String state,int companySystemId) throws Exception{
        PaidAccount account = new PaidAccount();
        account.setName(name);
        account.setState(state);
        account.setCreateTime(new Date());
        account.setCompanySystemId(companySystemId);
        account.setCreateUser("admin");
        addObject(account);
    }
    
    public void updateState(String state,String ids[]){
        paidAccountDao.updateState(state, ids);
    }
    
    @Transactional(rollbackFor=Exception.class)
    public void distributionMinute(Integer minute,Integer fileSize,Integer companySystemId,Integer paidAccountId,String type,String desc) throws Exception{
        
          if(minute==null){
              minute = 0;
          }
          
          if(fileSize==null){
              fileSize = 0;
          }
        
          int second = minute*60;
          fileSize = fileSize*1024*1024; 
          PaidAccount paidAccount = queryById(paidAccountId);
          CompanySystem companySystem =  queryById(companySystemId,CompanySystem.class);
          if(type.equals(CompanySystemService.TYPE_RECHARGE_REFUND)){
              if(paidAccount.getRemainingSecond()<second){
                  throw new PaidAccountExcption("可剩余分钟数不足!");
              }
              
              if(paidAccount.getRemainingFileSize()<fileSize){
                  throw new PaidAccountExcption("超出可退掉储存空间!");
              }
              
          }else if(type.equals(CompanySystemService.TYPE_RECHARGE)){
              if(companySystem.getRemainingTime()<second){
                  throw new PaidAccountExcption("总账号可剩余分钟数不足!");
              }
              
              if(companySystem.getRemainingFileSize()<fileSize){
                  throw new PaidAccountExcption("超出可分配储存空间!");
              }
              
          }
          changeMinute(companySystem, paidAccount, second,fileSize, desc, type);
          
          if(second!=0){
              int creditsMultiple = companySystem.getCreditsMultiple();
              int credits = paidAccount.getRemainingSecond()*creditsMultiple;
              paidAccount.setCredits(credits);
              paidAccount.setUseCredits(0);
              updateObjectById(paidAccount);
              
          }
    }
    
    public void changeMinute(CompanySystem companySystem,int accountId,int second,int fileSize,String desc,String type) throws Exception{
        changeMinute(companySystem,queryById(accountId), second,fileSize, desc,type);
    }
    
    public void changeFileSize(CompanySystem companySystem,PaidAccount pidAccount,int fileSize,String desc,String type) throws Exception{
        log.info("改变共享账号工具流水===>companySystem==>"+getColumnValues(companySystem)+"===>pidAccount"+getColumnValues(pidAccount)
                 +"====<fileSize>"+fileSize+"===>type"+type);
        log.info("fileSize==>更新前==>"+pidAccount.getRemainingFileSize()+",companySystemFileSize===>"+companySystem.getUseFileSize());
        if(type.equals(TYPE_CONSUMPTION)){
            pidAccount.setRemainingFileSize(pidAccount.getRemainingFileSize()-fileSize);
            pidAccount.setUseFileSize(pidAccount.getUseFileSize()+fileSize);
            companySystem.setUseFileSize(companySystem.getUseFileSize()+fileSize);
        }else if(type.equals(TYPE_RECHARGE_REFUND)){
            pidAccount.setRemainingFileSize(pidAccount.getRemainingFileSize()-fileSize);
            pidAccount.setFileSize(pidAccount.getFileSize()-fileSize);
            companySystem.setRemainingFileSize(companySystem.getRemainingFileSize()+fileSize);
        }else if(type.equals(TYPE_RECHARGE)){
            pidAccount.setRemainingFileSize(pidAccount.getRemainingFileSize()+fileSize);
            pidAccount.setFileSize(pidAccount.getFileSize()+fileSize);
            companySystem.setRemainingFileSize(companySystem.getRemainingFileSize()-fileSize);
        }else if(type.equals(CreditsLog.TYPE_RESTORE)){
            pidAccount.setRemainingFileSize(pidAccount.getRemainingFileSize()+fileSize);
            pidAccount.setUseFileSize(pidAccount.getUseFileSize()-fileSize);
            companySystem.setUseFileSize(companySystem.getUseFileSize()-fileSize);
        }else{
            throw new PaidAccountExcption(type+"==>状态错误!!");
        }
        log.info("fileSize==>更新完毕==>"+pidAccount.getRemainingFileSize()+",companySystemFileSize===>"+companySystem.getUseFileSize());
    }
    
    @Transactional(rollbackFor=Exception.class)
    public void changeMinute(CompanySystem companySystem,PaidAccount pidAccount,int second,int fileSize,String desc,String type) throws Exception{
            
               log.info("改变共享账号分钟流水===>companySystem==>"+getColumnValues(companySystem)+"===>pidAccount"+getColumnValues(pidAccount)
                        +"====<second>"+second+"===>type"+type);
               if(type.equals(TYPE_CONSUMPTION)){
                   pidAccount.setRemainingSecond(pidAccount.getRemainingSecond()-second);
                   pidAccount.setUseTime(pidAccount.getUseTime()+second);
                   companySystem.setUseTime(companySystem.getUseTime()+second);
               }else if(type.equals(TYPE_RECHARGE_REFUND)){
                   pidAccount.setRemainingSecond(pidAccount.getRemainingSecond()-second);
                   pidAccount.setCountSecond(pidAccount.getCountSecond()-second);
                   companySystem.setRemainingTime(companySystem.getRemainingTime()+second);
               }else if(type.equals(TYPE_RECHARGE)){
                   pidAccount.setRemainingSecond(pidAccount.getRemainingSecond()+second);
                   pidAccount.setCountSecond(pidAccount.getCountSecond()+second);
                   companySystem.setRemainingTime(companySystem.getRemainingTime()-second);
               }else{
                   throw new PaidAccountExcption(type+"==>状态错误!!");
               }
        
               changeFileSize(companySystem, pidAccount, fileSize, desc, type);
               savePidAccountConsumptionLog(companySystem, pidAccount, second, fileSize, desc, type);
    }
    
    public void savePidAccountConsumptionLog(CompanySystem companySystem,PaidAccount pidAccount,int second,int fileSize,String desc,String type) throws Exception{
        pidAccount.setLastOptionTime(new Date());
        pidAccount.setDescription(desc);
        
        PaidAccountDetail detail = new PaidAccountDetail();
        detail.setCompanySystemId(pidAccount.getCompanySystemId());
        detail.setCreateTime(new Date());
        detail.setSecond(second);
        detail.setFileSize(fileSize);
        detail.setPaidAccountId(pidAccount.getId());
        detail.setType(type);
        
        updateObjectById(pidAccount);
        addObject(detail);
        updateObjectById(companySystem);
    }
    
    
    public void distributionMoney(Double money,Integer companySystemId,Integer paidAccountId,String type,String desc) throws Exception{
        
        PaidAccount paidAccount = queryById(paidAccountId);
        CompanySystem companySystem =  queryById(companySystemId,CompanySystem.class);
        if(type.equals(CompanySystemService.TYPE_RECHARGE_REFUND)){
            if(paidAccount.getRemainingMoney()<money){
                throw new PaidAccountExcption("余额不足!");
            }
        }else if(type.equals(CompanySystemService.TYPE_RECHARGE)){
            if(companySystem.getBalance()<money){
                throw new PaidAccountExcption("总账号余额不足!");
            }
        }
        changeMoney(companySystem, paidAccount, money, desc, type);
  }
  
  public void changeMoney(CompanySystem companySystem,int accountId,Double money,String desc,String type) throws Exception{
      changeMoney(companySystem,queryById(accountId), money, desc,type);
  }
  
  @Transactional(rollbackFor=Exception.class)
  public PaidAccountDetail changeMoney(CompanySystem companySystem,PaidAccount paidAccount,Double money,String desc,String type) throws Exception{
          
             if(type.equals(TYPE_CONSUMPTION)){
                 
                 paidAccount.setRemainingMoney(NumberUtil.subtraction(paidAccount.getRemainingMoney(), money));
                 paidAccount.setUseMoney(NumberUtil.addTogether(paidAccount.getUseMoney(), money));
                 companySystem.setUseMoney(NumberUtil.addTogether(companySystem.getUseMoney(), money));
                 
             }else if(type.equals(TYPE_RECHARGE_REFUND)){
                 paidAccount.setRemainingMoney(NumberUtil.subtraction(paidAccount.getRemainingMoney(), money));
                 paidAccount.setCountMoney(NumberUtil.subtraction(paidAccount.getCountMoney(), money));
                 companySystemService.updateBalance(companySystem, money, CompanySystemService.TYPE_RECHARGE_REFUND);
             }else if(type.equals(TYPE_RECHARGE)){
                 paidAccount.setRemainingMoney(NumberUtil.addTogether(paidAccount.getRemainingMoney(), money));
                 paidAccount.setCountMoney(NumberUtil.addTogether(paidAccount.getCountMoney(), money));
                 companySystemService.updateBalance(companySystem, -money, CompanySystemService.TYPE_RECHARGE);
             }else{
                 throw new PaidAccountExcption(type+"==>状态错误!!");
             }
      
             paidAccount.setLastOptionTime(new Date());
             paidAccount.setDescription(desc);
             
             PaidAccountDetail detail = new PaidAccountDetail();
             detail.setCompanySystemId(paidAccount.getCompanySystemId());
             detail.setCreateTime(new Date());
             detail.setMoney(money);
             detail.setPaidAccountId(paidAccount.getId());
             detail.setType(type);
             
             updateObjectById(paidAccount);
             addObject(detail);
             updateObjectById(companySystem);
             
             if(companySystem.getSystemId().equals(CompanySystem.TYPE_XIAO_JIAN_REN)){
                 xiaoJianRenService.noticeMoney(companySystem, paidAccount.getRemainingMoney(),paidAccount.getId());
             }
             return detail;
  }
}