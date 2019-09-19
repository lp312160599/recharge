package com.dayee.model;

import java.util.Date;

public class XiaoJianRenOptLog {

    public static final String ISSUE_REWARD = "确认发放";
    
    public static final String CANCEL_ISSUE_REWARD = "取消发放";
    
    private Integer id;
    
    private Integer xiaoJianRenConsumptionLogId;
    
    private Integer companySystemId;
    
    private Integer userId;
    
    private String userName;
    
    private String type;
    
    private String ip;
    
    private Date optTime;
    
    private String info;

    
    public Integer getId() {
    
        return id;
    }

    
    public void setId(Integer id) {
    
        this.id = id;
    }

    
    public Integer getUserId() {
    
        return userId;
    }

    
    public void setUserId(Integer userId) {
    
        this.userId = userId;
    }

    
    public String getUserName() {
    
        return userName;
    }

    
    public void setUserName(String userName) {
    
        this.userName = userName;
    }

    
    public String getType() {
    
        return type;
    }

    
    public void setType(String type) {
    
        this.type = type;
    }

    
    public String getIp() {
    
        return ip;
    }

    
    public void setIp(String ip) {
    
        this.ip = ip;
    }

    
    public Date getOptTime() {
    
        return optTime;
    }

    
    public void setOptTime(Date optTime) {
    
        this.optTime = optTime;
    }

    public String getInfo() {
    
        return info;
    }
    
    public void setInfo(String info) {
    
        this.info = info;
    }

    
    public Integer getXiaoJianRenConsumptionLogId() {
    
        return xiaoJianRenConsumptionLogId;
    }

    
    public void setXiaoJianRenConsumptionLogId(Integer xiaoJianRenConsumptionLogId) {
    
        this.xiaoJianRenConsumptionLogId = xiaoJianRenConsumptionLogId;
    }


    
    public Integer getCompanySystemId() {
    
        return companySystemId;
    }


    
    public void setCompanySystemId(Integer companySystemId) {
    
        this.companySystemId = companySystemId;
    }
}
