package com.dayee.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.dayee.model.MoneyChangeLog;
import com.dayee.model.User;

@Service
public class MoneyChangeLogService extends BaseService<MoneyChangeLog> {
    
    public static String COMPANY_SERVICE_BALANCE="companyServiceBalance";
    
    public static String COMPANY_SERVICE_MERCHANT_RECHARGE_MONEY="companyServiceMerchantRechargeMoney";
    
    public static String COMPANY_SERVICE_MERCHANT_BALANCE="companyServiceMerchantBalance";
    
    public static String PAID_ACCOUNT_RECEIVED_MONEY="paidAccountReceivedMoney";

    public void addLog(User user,double oldMoney,double newMoney,String changeColumnName,Integer companySystemId) throws Exception {
        addLog(user, oldMoney, newMoney, changeColumnName, companySystemId,"");
    }
    
    public void addLog(User user,double oldMoney,double newMoney,String changeColumnName,Integer companySystemId,String remarke) throws Exception {
        MoneyChangeLog log = new MoneyChangeLog();
        log.setAddDate(new  Date());
        if(user!=null) {
            log.setOptUserId(user.getId());
            log.setOptUserName(user.getName());
        }
        log.setNewMoney(newMoney);
        log.setOldMoney(oldMoney);
        log.setChangeColumnName(changeColumnName);
        log.setCompanySystemId(companySystemId);
        log.setRemarke(remarke);
        addObject(log);
    }
}