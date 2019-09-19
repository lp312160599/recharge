package com.dayee.utils;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import com.dayee.model.EmailConfig;

public class EmailUtil {

	public static HtmlEmail getHtmlEmail(String hsot,String account,String password,String subject ) throws EmailException{
		  HtmlEmail htmlEmail = new HtmlEmail();  
          htmlEmail.setHostName(hsot);
          htmlEmail.setAuthentication(account,password);
          htmlEmail.setFrom(account);
          htmlEmail.setSubject(subject); 
          htmlEmail.setCharset("UTF-8");    
          return htmlEmail;
	}
	
	
	   public static HtmlEmail getHtmlEmail(EmailConfig emailConfig,String subject ) throws EmailException{
	          HtmlEmail htmlEmail = new HtmlEmail();  
	          htmlEmail.setHostName(emailConfig.getHost());
	          htmlEmail.setAuthentication(emailConfig.getAccount(),emailConfig.getPassword());
	          htmlEmail.setFrom(emailConfig.getAccount());
	          htmlEmail.setSubject(subject); 
	          htmlEmail.setCharset("UTF-8");    
	          return htmlEmail;
	    }
	
	
	
	public static HtmlEmail getHtmlEmail(String hsot,String account,String password ) throws EmailException{
        HtmlEmail htmlEmail = new HtmlEmail();  
        htmlEmail.setHostName(hsot);
        htmlEmail.setAuthentication(account,password);
        htmlEmail.setFrom(account);
        htmlEmail.setCharset("UTF-8");    
        return htmlEmail;
  }
	
}