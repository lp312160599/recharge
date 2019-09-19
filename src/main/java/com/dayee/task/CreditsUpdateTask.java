package com.dayee.task;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dayee.model.CompanySystem;
import com.dayee.model.PaidAccount;
import com.dayee.service.CompanyService;
import com.dayee.service.PaidAccountService;
import com.dayee.utils.QueryUtil;

@Component
public class CreditsUpdateTask {

    @Resource
    private CompanyService companyService;
    @Resource
    private PaidAccountService paidAccountService;
    
    public void run() throws Exception{
        
        List<CompanySystem> companySystemList =   companyService.query(QueryUtil.createQuery().eq("systemId",6), CompanySystem.class);
        
        for(CompanySystem companySystem:companySystemList){
            int creditsMultiple = companySystem.getCreditsMultiple();
            List<PaidAccount> paidAccountList =   paidAccountService.query();
            
            for(PaidAccount paidAccount : paidAccountList){
                int credits = paidAccount.getRemainingSecond()*creditsMultiple;
                paidAccount.setCredits(credits);
                paidAccount.setUseCredits(0);
                paidAccountService.updateObjectById(paidAccount);
            }
        }
    }
    
}
