package com.dayee.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ApplicationConfigCache {

	private static Properties pro ;
	
	public static void init(){
		InputStream inStream = ApplicationConfigCache.class.getResourceAsStream("/com/dayee/config/commonConfig.properties");
		pro = new Properties();
		try {
			pro.load(inStream);
		} catch (IOException e) {
			Logger.getLogger(ApplicationConfigCache.class).error(e.getMessage(),e);
		}
	}
	
	public static String[] getXiaoJianRenUseLoginUser(){
		String user = pro.getProperty("xiao.jian.ren.send.money.use.login.user");
		return user.split(",");
	}
	
	public static int getRequestErrorCount(){
		return  Integer.valueOf(pro.getProperty("request.error.count"));
	}
	
	public static String getRongYingApplyId(){
		return  pro.getProperty("rong.ying.apply.id");
	}
	
	public static String getRongYingCompanyAccount(){
		return  pro.getProperty("rong.ying.company.account");
	}
	
	public static String getRongYingToken(){
		return  pro.getProperty("rong.ying.token");
	}
	
	public static String getConsultantEmail(){
	    return  pro.getProperty("consultant.email");
	}
}