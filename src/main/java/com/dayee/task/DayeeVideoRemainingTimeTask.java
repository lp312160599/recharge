package com.dayee.task;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.mail.HtmlEmail;
import org.springframework.stereotype.Component;

import com.dayee.model.Company;
import com.dayee.model.CompanySystem;
import com.dayee.model.EmailConfig;
import com.dayee.model.PaidAccount;
import com.dayee.model.Status;
import com.dayee.service.CompanyService;
import com.dayee.service.PaidAccountService;
import com.dayee.utils.EmailUtil;
import com.dayee.utils.QueryUtil;
import com.dayee.utils.StringUtils;

@Component
public class DayeeVideoRemainingTimeTask {

    @Resource
    private CompanyService companyService;
    @Resource
    private PaidAccountService paidAccountService;
    
    public void run() throws Exception{
        
        List<CompanySystem> companySystemList =   companyService.query(QueryUtil.createQuery().eq("systemId",6), CompanySystem.class);
        EmailConfig emailConfig = companyService.queryById(1,EmailConfig.class);
       
        
        for(CompanySystem companySystem:companySystemList){
            Company company = paidAccountService.queryById(companySystem.getCompanyId(), Company.class);
            if(StringUtils.isEmpty(company.getConsultantEmail()))continue;
            HtmlEmail htmlEmail = EmailUtil.getHtmlEmail(emailConfig,"预警提醒");    
            htmlEmail.addTo(company.getConsultantEmail(),company.getConsultantEmail());
            int warning =  companySystem.getWarning()*60;
            int paidAccountWarning =  companySystem.getPaidAccountWarning()*60;
            if(companySystem.getRemainingTime()<warning){
                htmlEmail.setHtmlMsg("您的总账户余额已经不足"+companySystem.getWarning()+"分钟啦!请及时充值");
                htmlEmail.send();   
            }
            List<PaidAccount> paidAccountList =   paidAccountService.query(
                                                                           QueryUtil.createQuery().eq("companySystemId",companySystem.getId())
                                                                           .eq("state",Status.EFFECTIVE ));
            
            for(PaidAccount paidAccount : paidAccountList){
                htmlEmail = EmailUtil.getHtmlEmail(emailConfig,"预警提醒");    
                htmlEmail.addTo(company.getConsultantEmail(),company.getConsultantEmail());

                 if(paidAccount.getRemainingSecond()<paidAccountWarning){ 
                     htmlEmail.setHtmlMsg("您的共享账户"+paidAccount.getName()+"余额已经不足"+companySystem.getPaidAccountWarning()+"分钟啦!请及时充值");
                     htmlEmail.send();   
                 }
            }
        }
    }
}
