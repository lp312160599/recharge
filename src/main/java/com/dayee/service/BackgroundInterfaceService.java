package com.dayee.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dayee.model.BackgroundConsumptionLog;
import com.dayee.model.CompanySystem;
import com.dayee.model.Systems;
import com.dayee.utils.NumberUtil;

@Component
public class BackgroundInterfaceService extends BaseService<CompanySystem>{

	@Transactional(rollbackFor=Exception.class)
	public void addConsumptionLog(BackgroundConsumptionLog backgroundConsumptionLog,CompanySystem companySystem) throws Exception{
		log.info("=====>回调背调数据===>backgroundConsumptionLog:"+getColumnValues(backgroundConsumptionLog)+"====>companySystem:"+getColumnValues(companySystem));
		addObject(backgroundConsumptionLog);
		log.info("=====>添加背调数据成功");
		if(companySystem.getBackgroundNumber()==null){
			companySystem.setBackgroundNumber(1);
		}else{
			companySystem.setBackgroundNumber(companySystem.getBackgroundNumber()+1);
		}
		
		if(companySystem.getCountMoney()==null){
			companySystem.setCountMoney(backgroundConsumptionLog.getMoney());
		}else{
		    
			companySystem.setCountMoney(NumberUtil.addTogether(companySystem.getCountMoney(), backgroundConsumptionLog.getMoney()));
		}
		
		companySystem.setBalance(NumberUtil.subtraction(companySystem.getRechargeMoney(), backgroundConsumptionLog.getMoney()));
		
		updateObjectById(companySystem);
		
		Systems systems = queryById(companySystem.getSystemId(), Systems.class);
		if(systems.getBackgroundNumber()==null){
			systems.setBackgroundNumber(1);
		}else{
			systems.setBackgroundNumber(systems.getBackgroundNumber()+1);
		}
		updateObjectById(systems);
		log.info("=====>结束背调数据回调");
	}
	
}
