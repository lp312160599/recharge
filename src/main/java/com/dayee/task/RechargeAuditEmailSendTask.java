package com.dayee.task;

import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dayee.model.Company;
import com.dayee.model.EmailConfig;
import com.dayee.model.Recharge;
import com.dayee.model.RechargeAudit;
import com.dayee.service.RechargeAuditService;
import com.dayee.utils.DateUtil;
import com.dayee.utils.EmailUtil;
import com.dayee.utils.FileOperation;
import com.dayee.utils.QueryUtil;

@Component
public class RechargeAuditEmailSendTask {
 
	@Resource
 	private RechargeAuditService service;
	
	public void run() throws Exception{
		QueryUtil queryUtil = QueryUtil.createQuery().addColumn("state",RechargeAudit.NO_SEND);
		List<RechargeAudit> rechargeAuditList  =  service.query(queryUtil.get());
		EmailConfig emailConfig = service.queryById(1,EmailConfig.class);
		InputStream inputStream = RechargeAuditEmailSendTask.class.getResourceAsStream("table.html");
        FileOperation 		fileOperation = new FileOperation();
        String html =  fileOperation.readFile(inputStream);
		for (int i = 0; i < rechargeAuditList.size(); i++) {
			HtmlEmail htmlEmail = EmailUtil.getHtmlEmail(emailConfig.getHost(),emailConfig.getAccount(),emailConfig.getPassword(),"充值审核");
			RechargeAudit rechargeAudit = rechargeAuditList.get(i);
			Recharge recharge = service.queryById(rechargeAudit.getRechargeId(),Recharge.class);
			Company company = service.queryById(recharge.getCompanyId(),Company.class);
			String content = html.replace("#{companyName}", company.getName())
			    .replace("#{type}", recharge.getType())
			    .replace("#{money}", String.valueOf(recharge.getMoney()))
			    .replace("#rechargeId", String.valueOf(recharge.getId()))
			    .replace("#rechargeAuditId", String.valueOf(rechargeAudit.getId()))
			    .replace("#{userName}", recharge.getRechargeUser());
			
			if(recharge.getMinute()!=null){
			    content = content.replace("#{minute}", String.valueOf(recharge.getMinute())+"分钟");
			}else{
			    content = content.replace("#{minute}", "");
			}
			
			if(recharge.getFileSize()!=null){
			    content =content.replace("#{fileSize}", String.valueOf(recharge.getFileSize())+"G");
			}else{
			    content =content.replace("#{fileSize}", "");
			}
			
			if(recharge.getSpaceEndTime()!=null){
			    content = content.replace("#{spaceEndTime}",DateUtil.dateToString(recharge.getSpaceEndTime(),DateUtil.YYYY_MM_DD));
            }else{
                content =content.replace("#{spaceEndTime}","");
            }
			    
			try{
				htmlEmail.addTo(rechargeAudit.getAuditorEmail(),rechargeAudit.getAuditor());
				htmlEmail.setHtmlMsg(content);
				htmlEmail.send();   
				rechargeAudit.setState(RechargeAudit.NO_AUDIT);
				service.updateObjectById(rechargeAudit);
			}catch (Exception e) {
			    Logger.getLogger(getClass()).error(e.getMessage(),e);
				rechargeAudit.setSendMsg("邮件发送异常!!");
				service.updateObjectById(rechargeAudit);
			}
		}
	}
}