package com.dayee.model;

import com.dayee.annotation.NoColumn;

public class SmsCode {

    @NoColumn
    public static int VALIDITY_PERIOD_MINUTE=30;
    
    private Integer id;
    private String phone;
    private Integer code;
    private Long validityPeriod;
    private Long sendTimestamp;
    
    public Integer getId() {
    
        return id;
    }
    
    public void setId(Integer id) {
    
        this.id = id;
    }
    
    public String getPhone() {
    
        return phone;
    }
    
    public void setPhone(String phone) {
    
        this.phone = phone;
    }
    
    public Integer getCode() {
    
        return code;
    }
    
    public void setCode(Integer code) {
    
        this.code = code;
    }
    
    public Long getValidityPeriod() {
    
        return validityPeriod;
    }
    
    public void setValidityPeriod(Long validityPeriod) {
    
        this.validityPeriod = validityPeriod;
    }
    
    public Long getSendTimestamp() {
    
        return sendTimestamp;
    }
    
    public void setSendTimestamp(Long sendTimestamp) {
    
        this.sendTimestamp = sendTimestamp;
    }
}
