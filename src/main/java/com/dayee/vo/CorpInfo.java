package com.dayee.vo;


public class CorpInfo {

    private String corpCode;
    
    private String corpName;
    
    private String consultantEmail;
    
    private String callBackUrl;
    
    private Integer paidAccountId;

    public String getCorpCode() {
    
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
    
        this.corpCode = corpCode;
    }
    
    public String getCorpName() {
    
        return corpName;
    }
    
    public void setCorpName(String corpName) {
    
        this.corpName = corpName;
    }

    public String getConsultantEmail() {
    
        return consultantEmail;
    }
    
    public void setConsultantEmail(String consultantEmail) {
    
        this.consultantEmail = consultantEmail;
    }

    public String getCallBackUrl() {
    
        return callBackUrl;
    }
    
    public void setCallBackUrl(String callBackUrl) {
    
        this.callBackUrl = callBackUrl;
    }

    
    public Integer getPaidAccountId() {
    
        return paidAccountId;
    }

    public void setPaidAccountId(Integer paidAccountId) {
    
        this.paidAccountId = paidAccountId;
    }
}
