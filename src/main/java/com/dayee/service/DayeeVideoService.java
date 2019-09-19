package com.dayee.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dayee.dao.DayeeVideoDao;
import com.dayee.model.Company;
import com.dayee.model.CompanySystem;
import com.dayee.model.DayeeVideoConsumptionLog;
import com.dayee.model.PaidAccount;
import com.dayee.model.Systems;
import com.dayee.model.User;
import com.dayee.utils.DateUtil;
import com.dayee.utils.QueryUtil;
import com.dayee.vo.CorpInfo;

@Service
public class DayeeVideoService extends BaseService<DayeeVideoConsumptionLog>{

    @Resource
    private CompanyService companyService;
    @Resource
    private PaidAccountService paidAccountService;
    @Resource
    private DayeeVideoDao dayeeVideoDao;
    
    @Transactional(rollbackFor=Exception.class)
    public void corpSynchronize(List<CorpInfo> corpInfo) throws Exception{
        
        User user = queryOne(QueryUtil.createQuery().eq("name","admin").get(),User.class);
        Systems system = queryById(6,Systems.class);
        Date spaceEndTime = DateUtil.stringToDate("2019-04-30", DateUtil.YYYY_MM_DD);
        int second = 1200;
        int fileSize = 153600;
        
        List<CompanySystem> companySystemList = new ArrayList<CompanySystem>();
        
        CompanySystem companySystem = new CompanySystem();
        companySystem.setSystemId(6);
        companySystem.setCreditsMultiple(3);
        companySystem.setDayeeVideoType(3);
        companySystem.setPaidAccountWarning(30);
        companySystem.setWarning(30);
        
        companySystem.setFileSize(fileSize);
        companySystem.setRemainingFileSize(fileSize);
        
        companySystem.setRechargeTime(second);
        companySystem.setRemainingTime(second);
        companySystem.setBalanceTime(second);
        
        companySystem.setSpaceEndTime(spaceEndTime);
        
        companySystemList.add(companySystem);
        
        for(CorpInfo info :corpInfo){
            
            Company c = queryOne(QueryUtil.createQuery().eq("code",info.getCorpCode() ).get(), Company.class);
            if(c != null) continue;
            
            Company company = new Company();
            company.setCode(info.getCorpCode());
            company.setName(info.getCorpName());
            company.setConsultantEmail(info.getConsultantEmail());
            
            companySystem.setId(null);
            companySystem.setCallBackUrl(info.getCallBackUrl());
            
            system.setRechargeTime(system.getRechargeTime()+second);
           
            companyService.updateCompany(company, user, companySystemList);
            
            PaidAccount paidAccount = queryOne(QueryUtil.createQuery().eq("companySystemId",companySystem.getId()).get(), PaidAccount.class);
            
            paidAccountService.changeMinute(companySystem, paidAccount, second, fileSize, "自动分配", PaidAccountService.TYPE_RECHARGE);
            
            int creditsMultiple = companySystem.getCreditsMultiple();
            int credits = paidAccount.getRemainingSecond()*creditsMultiple;
            paidAccount.setCredits(credits);
            paidAccount.setUseCredits(0);
            updateObjectById(paidAccount);
            info.setPaidAccountId(paidAccount.getId());
        }
    }
    
    public List<Company> getOpenVideoCompany(){
        return dayeeVideoDao.getCompanyAll();
    }
    
}