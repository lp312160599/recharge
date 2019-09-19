package com.dayee.model;

import java.util.Date;


public class CreditsLog {
    
    public static String TYPE_DEDUCTION ="扣除";
    
    public static String TYPE_RESTORE ="恢复";

    private Integer id;
    
    private Integer credits;
    
    private  Integer remainingCredits;
    
    private  Integer companySystemId;
    
    private Date createTime;
    
    private String type;
    
    private String uniqueId;
    
    private Integer paidAccountId;

    public Integer getId() {
    
        return id;
    }

    public void setId(Integer id) {
    
        this.id = id;
    }
    
    public Integer getCredits() {
    
        return credits;
    }
    
    public void setCredits(Integer credits) {
    
        this.credits = credits;
    }

    public Integer getRemainingCredits() {
    
        return remainingCredits;
    }

    public void setRemainingCredits(Integer remainingCredits) {
    
        this.remainingCredits = remainingCredits;
    }
    
    public Integer getCompanySystemId() {
    
        return companySystemId;
    }
    
    public void setCompanySystemId(Integer companySystemId) {
    
        this.companySystemId = companySystemId;
    }
    
    public Date getCreateTime() {
    
        return createTime;
    }

    public void setCreateTime(Date createTime) {
    
        this.createTime = createTime;
    }
    
    public String getType() {
    
        return type;
    }
    
    public void setType(String type) {
    
        this.type = type;
    }
    
    public String getUniqueId() {
    
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
    
        this.uniqueId = uniqueId;
    }

    
    public Integer getPaidAccountId() {
    
        return paidAccountId;
    }
    
    public void setPaidAccountId(Integer paidAccountId) {
    
        this.paidAccountId = paidAccountId;
    }
}