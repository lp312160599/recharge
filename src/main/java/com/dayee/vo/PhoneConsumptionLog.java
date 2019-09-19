package com.dayee.vo;


import java.util.Date;

public class PhoneConsumptionLog {

	private String number ;
	
	private Double money ;
	
	private int callTime;
	
	private Integer  shareAccountId  ;
	
	private String  hrAccountName  ;
	
	private String  hrAccountPhone  ;
	
	private String  called  ;
	
	private String  recordingUrl  ;
	
	private Date    startTime;
	
	private Date  endTime  ;
	
	private String   callId  ;
	
	private Integer  status  ;
	
	private Double   unitPrice;
	
	private String systemType;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public int getCallTime() {
		return callTime;
	}

	public void setCallTime(int callTime) {
		this.callTime = callTime;
	}

	public String getHrAccountName() {
		return hrAccountName;
	}

	public void setHrAccountName(String hrAccountName) {
		this.hrAccountName = hrAccountName;
	}

	public String getHrAccountPhone() {
		return hrAccountPhone;
	}

	public void setHrAccountPhone(String hrAccountPhone) {
		this.hrAccountPhone = hrAccountPhone;
	}

	public String getCalled() {
		return called;
	}

	public void setCalled(String called) {
		this.called = called;
	}

	public String getRecordingUrl() {
		return recordingUrl;
	}

	public void setRecordingUrl(String recordingUrl) {
		this.recordingUrl = recordingUrl;
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
 
	public String getCallId() {
		return callId;
	}

	public void setCallId(String callId) {
		this.callId = callId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getShareAccountId() {
		return shareAccountId;
	}

	public void setShareAccountId(Integer shareAccountId) {
		this.shareAccountId = shareAccountId;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getSystemType() {
		return systemType;
	}

	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}
}