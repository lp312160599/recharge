package com.dayee.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dayee.model.DayeeVideoConsumptionLog;
import com.dayee.service.DayeeVideoService;
import com.dayee.utils.QueryUtil;
import com.dayee.vo.Orders;
import com.dayee.vo.Page;
import com.dayee.vo.QueryParameter;

@Controller
@RequestMapping("/dayeeVideo")
public class DayeeVideoController extends BaseController {

    @Resource
    private DayeeVideoService dayeeVideoService;
    
    @RequestMapping("{companyId}/consumption/list")
    public String queryConsumption(@PathVariable("companyId")Integer companyId,DayeeVideoConsumptionLog dayeeVideoConsumptionLog,QueryParameter queryParameter,Orders orders,Page page,Model model) throws Exception{
        
        if(dayeeVideoConsumptionLog==null) dayeeVideoConsumptionLog= new DayeeVideoConsumptionLog();
        queryParameter = getQueryParameter(queryParameter);
        QueryUtil queryUtil = QueryUtil.createQuery().like("number",dayeeVideoConsumptionLog.getNumber())
                .like("phone", dayeeVideoConsumptionLog.getPhone())
                .like("paidAccountName",dayeeVideoConsumptionLog.getPaidAccountName())
                .like("orgName",dayeeVideoConsumptionLog.getOrgName())
                .eq("companySystemId", companyId)
                .betweenDate("startTime",queryParameter.getStartDate(),queryParameter.getEndDate());
        
        if(dayeeVideoConsumptionLog.getVideoResource()!=null
           &&dayeeVideoConsumptionLog.getVideoResource()!=-1){
            queryUtil.eq("videoResource", dayeeVideoConsumptionLog.getVideoResource());
        }
        
        List<DayeeVideoConsumptionLog> dayeeVideoConsumptionLogList = dayeeVideoService.query(queryUtil.get(),getPage(page), DayeeVideoConsumptionLog.class, orders);
        model.addAttribute("dayeeVideoConsumptionLogList", dayeeVideoConsumptionLogList);
        model.addAttribute("consumption", dayeeVideoConsumptionLog);
        model.addAttribute("companyId", companyId);
        addModel(model, page, queryParameter, orders);
        return "dayeeVideo/consumption";
    }
    
}
