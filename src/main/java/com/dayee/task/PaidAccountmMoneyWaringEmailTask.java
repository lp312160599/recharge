package com.dayee.task;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.mail.HtmlEmail;
import org.springframework.stereotype.Component;

import com.dayee.model.Company;
import com.dayee.model.CompanySystem;
import com.dayee.model.EmailConfig;
import com.dayee.model.PaidAccount;
import com.dayee.service.CompanyService;
import com.dayee.service.PaidAccountService;
import com.dayee.utils.EmailUtil;
import com.dayee.utils.QueryUtil;
import com.dayee.utils.StringUtils;

@Component
public class PaidAccountmMoneyWaringEmailTask {

    @Resource
    private CompanyService companyService;
    @Resource
    private PaidAccountService paidAccountService;
    
    public void run() throws Exception{
        
        List<CompanySystem> companySystemList =   companyService.query(QueryUtil.createQuery().eq("systemId",3), CompanySystem.class);
        EmailConfig emailConfig = companyService.queryById(1,EmailConfig.class);
       
        
        for(CompanySystem companySystem:companySystemList){
            Company company = paidAccountService.queryById(companySystem.getCompanyId(), Company.class);
            if(StringUtils.isEmpty(company.getConsultantEmail()))continue;
            int paidAccountWarning =  companySystem.getPaidAccountWarning();
            
            List<PaidAccount> paidAccountList =   paidAccountService.query(QueryUtil.createQuery().eq("companySystemId",companySystem.getId()));
            
            for(PaidAccount paidAccount : paidAccountList){

                 if(paidAccount.getRemainingMoney()<paidAccountWarning){ 
                     HtmlEmail htmlEmail = EmailUtil.getHtmlEmail(emailConfig,"预警提醒");    
                     htmlEmail.addTo(company.getConsultantEmail(),company.getConsultantEmail());
                     htmlEmail.setHtmlMsg("您好，"+company.getName()+"账套-"+paidAccount.getName()+"共享费用账户 的小荐人悬赏金金额不足"+paidAccountWarning+"块钱，请及时充值。剩余金额用完时将暂停小荐人悬赏功能");
                     htmlEmail.send();   
                 }
            }
        }
    }
}
