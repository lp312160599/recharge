package com.dayee.model;

import java.util.Date;


public class DayeeVideoConsumptionLog {

    
    private Integer id;
    
    private String orgName;
    
    private String number;
    
    private String phone;
    
    private Integer actualSeconds;
    
    private Integer creditLimitSeconds;
    
    private Integer videoResource;
    
    private Integer shareCostId;
    
    private String paidAccountName;
    
    private Integer paidAccountId;
    
    private Integer companySystemId;
    
    private Integer balanceTime;
    
    private Date createTime;
    
    private Date startTime;
    
    private Date endTime;
    
    private Double fileSize;
    
    private String externalKey;
    

    public Integer getId() {
    
        return id;
    }
    
    public void setId(Integer id) {
    
        this.id = id;
    }

    public String getOrgName() {
    
        return orgName;
    }

    public void setOrgName(String orgName) {
    
        this.orgName = orgName;
    }
    
    public String getNumber() {
    
        return number;
    }

    public void setNumber(String number) {
    
        this.number = number;
    }
    
    public String getPhone() {
    
        return phone;
    }
    
    public void setPhone(String phone) {
    
        this.phone = phone;
    }
    
    public Integer getActualSeconds() {
    
        return actualSeconds;
    }
    
    public void setActualSeconds(Integer actualSeconds) {
    
        this.actualSeconds = actualSeconds;
    }

    
    public Integer getCreditLimitSeconds() {
    
        return creditLimitSeconds;
    }

    
    public void setCreditLimitSeconds(Integer creditLimitSeconds) {
    
        this.creditLimitSeconds = creditLimitSeconds;
    }

    public Integer getVideoResource() {
    
        return videoResource;
    }
    
    public void setVideoResource(Integer videoResource) {
    
        this.videoResource = videoResource;
    }
    
    public Integer getShareCostId() {
    
        return shareCostId;
    }
    
    public void setShareCostId(Integer shareCostId) {
    
        this.shareCostId = shareCostId;
    }
    
    public Integer getCompanySystemId() {
    
        return companySystemId;
    }
    
    public void setCompanySystemId(Integer companySystemId) {
    
        this.companySystemId = companySystemId;
    }

    public Integer getBalanceTime() {
    
        return balanceTime;
    }

    public void setBalanceTime(Integer balanceTime) {
    
        this.balanceTime = balanceTime;
    }

    public Date getStartTime() {
    
        return startTime;
    }
    
    public void setStartTime(Date startTime) {
    
        this.startTime = startTime;
    }
    
    public Date getEndTime() {
    
        return endTime;
    }
    
    public void setEndTime(Date endTime) {
    
        this.endTime = endTime;
    }

    
    public String getPaidAccountName() {
    
        return paidAccountName;
    }

    public void setPaidAccountName(String paidAccountName) {
    
        this.paidAccountName = paidAccountName;
    }

    
    public Integer getPaidAccountId() {
    
        return paidAccountId;
    }

    public void setPaidAccountId(Integer paidAccountId) {
    
        this.paidAccountId = paidAccountId;
    }

    
    public Double getFileSize() {
    
        return fileSize;
    }

    public void setFileSize(Double fileSize) {
    
        this.fileSize = fileSize;
    }

    
    public Date getCreateTime() {
    
        return createTime;
    }

    public void setCreateTime(Date createTime) {
    
        this.createTime = createTime;
    }

    
    public String getExternalKey() {
    
        return externalKey;
    }

    
    public void setExternalKey(String externalKey) {
    
        this.externalKey = externalKey;
    }
}