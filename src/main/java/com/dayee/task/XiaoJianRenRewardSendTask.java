package com.dayee.task;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dayee.model.XiaoJianRenConsumptionLog;
import com.dayee.service.XiaoJianRenService;
import com.dayee.utils.DateUtil;
import com.dayee.utils.QueryUtil;

@Component
public class XiaoJianRenRewardSendTask {

    @Resource
    XiaoJianRenService xiaoJianRenService;
    
    public void run() throws Exception {
        List<XiaoJianRenConsumptionLog> list =  xiaoJianRenService.query(
                                                    QueryUtil.createQuery().eq("status", XiaoJianRenConsumptionLog.REVIEW_STATUS_CONFIRM_SEND_CANCEL)
                                                    .betweenDate("issueDate", null,DateUtil.dateToString(new Date())));
        xiaoJianRenService.issueRewards(null,list);
    }
    
}
