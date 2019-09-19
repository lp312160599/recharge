package com.dayee.vo;


public class RechargeFreezeInfo {
    
    private Double money;
    
    private Integer fileSize;
    
    private Integer minute;
    
    public Integer getMinute() {
    
        return minute;
    }
    
    public void setMinute(Integer minute) {
    
        this.minute = minute;
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
}