package com.dayee.task;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dayee.model.CompanySystem;
import com.dayee.model.MonthlyDeductionLog;
import com.dayee.service.CompanyService;
import com.dayee.service.CompanySystemService;
import com.dayee.service.MonthlyDeductionLogService;
import com.dayee.utils.DateUtil;
import com.dayee.utils.QueryUtil;

@Component
public class MonthlyDeductionTask {

    @Resource
    private MonthlyDeductionLogService monthlyDeductionLogService;
    @Resource
    private CompanyService companyService;
    @Resource
    private CompanySystemService companySystemService;
    
    public void run() throws Exception{
        
        List<CompanySystem> companySystemList =   companyService.query(QueryUtil.createQuery().eq("systemId",5), CompanySystem.class);
        Date date = new Date();
        for(CompanySystem companySystem:companySystemList){
            MonthlyDeductionLog log = monthlyDeductionLogService.queryOne(QueryUtil.createQuery().eq("companySystemId",companySystem.getId())
                                                        .betweenDate("time", DateUtil.dateToString(date,DateUtil.YYYY_MM)+"-01", DateUtil.dateToString(date)));
            if(log==null){
                companySystemService.updateBalance(companySystem, -companySystem.getServiceMoney(),CompanySystemService.TYPE_CONSUMPTION);
                log = new MonthlyDeductionLog();
                log.setCompanySystemId(companySystem.getId());
                log.setBalance(companySystem.getBalance());
                log.setMoney(companySystem.getServiceMoney());
                log.setTime(new Date());
                monthlyDeductionLogService.addObject(log);   
            }
        }
        
    }
    
}
