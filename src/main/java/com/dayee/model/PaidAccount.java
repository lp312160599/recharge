package com.dayee.model;

import java.util.Date;

/**
 * 共享账号
 * @author lipeng
 *
 */
public class PaidAccount implements Status{

    private Integer id;

    private String name;
    
    private Date createTime;
    
    private String createUser;
    
    private Date lastOptionTime;
    
    private String description;
    
    private Integer useTime;
    
    private Integer remainingSecond;
    
    private Integer countSecond;
    
    private Double countMoney;
    
    private Double remainingMoney;
    
    private Double useMoney;
    
    private String state;
    
    private Integer companySystemId;
    
    private Integer credits;
    
    private Integer useCredits;
    
    private Integer fileSize;
    
    private Integer useFileSize;
    
    private Integer remainingFileSize;

    /**未发放金额*/
    private Double  notIssuedMoney;
    /**已发放金额*/
    private Double  issuedMoney;
    /**已领取金额*/
    private Double  receivedMoney;

    public Integer getId() {
    
        return id;
    }
    
    public void setId(Integer id) {
    
        this.id = id;
    }
    
    public String getName() {
    
        return name;
    }
    
    public void setName(String name) {
    
        this.name = name;
    }
    
    public Date getCreateTime() {
    
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
    
        this.createTime = createTime;
    }
    
    public String getDescription() {
    
        return description;
    }
    
    public void setDescription(String description) {
    
        this.description = description;
    }
    
    public String getState() {
    
        return state;
    }
    
    public void setState(String state) {
    
        this.state = state;
    }
    
    public Integer getCompanySystemId() {
    
        return companySystemId;
    }

    public void setCompanySystemId(Integer companySystemId) {
    
        this.companySystemId = companySystemId;
    }

    
    public Date getLastOptionTime() {
    
        return lastOptionTime;
    }

    public void setLastOptionTime(Date lastOptionTime) {
    
        this.lastOptionTime = lastOptionTime;
    }

    
    public Integer getRemainingSecond() {
    
        return remainingSecond;
    }
    
    public void setRemainingSecond(Integer remainingSecond) {
    
        this.remainingSecond = remainingSecond;
    }
    
    public Integer getCountSecond() {
    
        return countSecond;
    }

    
    public void setCountSecond(Integer countSecond) {
    
        this.countSecond = countSecond;
    }

    public String getCreateUser() {
    
        return createUser;
    }

    public void setCreateUser(String createUser) {
    
        this.createUser = createUser;
    }
    
    public Integer getCredits() {
    
        return credits;
    }

    public void setCredits(Integer credits) {
    
        this.credits = credits;
    }
    
    public Integer getUseCredits() {
    
        return useCredits;
    }
    
    public void setUseCredits(Integer useCredits) {
    
        this.useCredits = useCredits;
    }

    public Integer getUseTime() {
    
        return useTime;
    }
    
    public void setUseTime(Integer useTime) {
    
        this.useTime = useTime;
    }

    
    public Double getCountMoney() {
    
        return countMoney;
    }

    
    public void setCountMoney(Double countMoney) {
    
        this.countMoney = countMoney;
    }

    
    public Double getRemainingMoney() {
    
        return remainingMoney;
    }

    
    public void setRemainingMoney(Double remainingMoney) {
    
        this.remainingMoney = remainingMoney;
    }

    public Double getUseMoney() {
    
        return useMoney;
    }

    public void setUseMoney(Double useMoney) {
    
        this.useMoney = useMoney;
    }

    
    public Integer getFileSize() {
    
        return fileSize;
    }

    
    public void setFileSize(Integer fileSize) {
    
        this.fileSize = fileSize;
    }

    
    public Integer getUseFileSize() {
    
        return useFileSize;
    }

    
    public void setUseFileSize(Integer useFileSize) {
    
        this.useFileSize = useFileSize;
    }
    
    public Integer getRemainingFileSize() {
    
        return remainingFileSize;
    }

    public void setRemainingFileSize(Integer remainingFileSize) {
    
        this.remainingFileSize = remainingFileSize;
    }

    
    public Double getNotIssuedMoney() {
    
        return notIssuedMoney;
    }

    
    public void setNotIssuedMoney(Double notIssuedMoney) {
    
        this.notIssuedMoney = notIssuedMoney;
    }

    
    public Double getIssuedMoney() {
    
        return issuedMoney;
    }

    
    public void setIssuedMoney(Double issuedMoney) {
    
        this.issuedMoney = issuedMoney;
    }

    
    public Double getReceivedMoney() {
    
        return receivedMoney;
    }

    public void setReceivedMoney(Double receivedMoney) {
    
        this.receivedMoney = receivedMoney;
    }
}