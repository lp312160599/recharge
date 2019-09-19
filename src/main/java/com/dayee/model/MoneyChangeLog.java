package com.dayee.model;

import java.util.Date;

public class MoneyChangeLog {

    private  Integer id;
    
    private String  changeColumnName;
    private String  remarke;
    private Double  oldMoney;
    private Double  newMoney;
    private Integer optUserId  ;
    private String optUserName  ;
    private Integer companySystemId  ;
    private Date  addDate;
    
    public Integer getId() {
    
        return id;
    }
    
    public void setId(Integer id) {
    
        this.id = id;
    }
    
    public String getChangeColumnName() {
    
        return changeColumnName;
    }
    
    public void setChangeColumnName(String changeColumnName) {
    
        this.changeColumnName = changeColumnName;
    }
    
    public Double getOldMoney() {
    
        return oldMoney;
    }
    
    public void setOldMoney(Double oldMoney) {
    
        this.oldMoney = oldMoney;
    }
    
    public Double getNewMoney() {
    
        return newMoney;
    }
    
    public void setNewMoney(Double newMoney) {
    
        this.newMoney = newMoney;
    }
    
    public Integer getOptUserId() {
    
        return optUserId;
    }
    
    public void setOptUserId(Integer optUserId) {
    
        this.optUserId = optUserId;
    }
    
    public Date getAddDate() {
    
        return addDate;
    }
    
    public void setAddDate(Date addDate) {
    
        this.addDate = addDate;
    }

    
    public Integer getCompanySystemId() {
    
        return companySystemId;
    }
    
    public void setCompanySystemId(Integer companySystemId) {
    
        this.companySystemId = companySystemId;
    }
    public String getOptUserName() {
    
        return optUserName;
    }
    public void setOptUserName(String optUserName) {
    
        this.optUserName = optUserName;
    }

    
    public String getRemarke() {
    
        return remarke;
    }

    public void setRemarke(String remarke) {
    
        this.remarke = remarke;
    }
    
    
}
