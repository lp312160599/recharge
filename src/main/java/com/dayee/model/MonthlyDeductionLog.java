package com.dayee.model;

import java.util.Date;


public class MonthlyDeductionLog {

    private Integer id;
    
    private Date time;
    
    private Double money;
    
    private Double balance;
    
    private Integer companySystemId;

    
    public Integer getId() {
    
        return id;
    }
    
    public void setId(Integer id) {
    
        this.id = id;
    }
    
    public Date getTime() {
    
        return time;
    }

    public void setTime(Date time) {
    
        this.time = time;
    }

    public Double getMoney() {
    
        return money;
    }
    
    public void setMoney(Double money) {
    
        this.money = money;
    }
    
    public Double getBalance() {
    
        return balance;
    }
    
    public void setBalance(Double balance) {
    
        this.balance = balance;
    }
    
    public Integer getCompanySystemId() {
    
        return companySystemId;
    }

    public void setCompanySystemId(Integer companySystemId) {
    
        this.companySystemId = companySystemId;
    }
}
