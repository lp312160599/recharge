package com.dayee.model;

import java.util.Date;


public class PaidAccountDetail {
    
    public static String TYPE_RECHARGE = "充值";
    public static String TYPE_RECHARGE_REFUND = "退款";
    public static String TYPE_CONSUMPTION = "消费";

    private Integer id;
    
    private Integer second;
    
    private Integer fileSize;
    
    private Double money;
    
    private Date createTime;
    
    private Integer paidAccountId;
    
    private Integer companySystemId;
    
    private String type;
    
    private Integer xiaoJianRenLogId;

    public Integer getId() {
    
        return id;
    }
    
    public void setId(Integer id) {
    
        this.id = id;
    }

    public Integer getSecond() {
    
        return second;
    }
    
    public void setSecond(Integer second) {
    
        this.second = second;
    }

    public Date getCreateTime() {
    
        return createTime;
    }

    public void setCreateTime(Date createTime) {
    
        this.createTime = createTime;
    }
    
    
    public Integer getPaidAccountId() {
    
        return paidAccountId;
    }

    
    public void setPaidAccountId(Integer paidAccountId) {
    
        this.paidAccountId = paidAccountId;
    }

    public Integer getCompanySystemId() {
    
        return companySystemId;
    }
    
    public void setCompanySystemId(Integer companySystemId) {
    
        this.companySystemId = companySystemId;
    }
    
    public String getType() {
    
        return type;
    }
    
    public void setType(String type) {
    
        this.type = type;
    }

    
    public Double getMoney() {
    
        return money;
    }

    
    public void setMoney(Double money) {
    
        this.money = money;
    }

    
    public Integer getFileSize() {
    
        return fileSize;
    }

    
    public void setFileSize(Integer fileSize) {
    
        this.fileSize = fileSize;
    }

    public Integer getXiaoJianRenLogId() {
    
        return xiaoJianRenLogId;
    }
    
    public void setXiaoJianRenLogId(Integer xiaoJianRenLogId) {
    
        this.xiaoJianRenLogId = xiaoJianRenLogId;
    }
}
