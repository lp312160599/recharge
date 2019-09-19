package com.dayee.vo;

public class RongYingVo {

	private Integer id;
	
    private Double unit;
	
	private Integer securityCodeCountNumber;
	
	private String callBackUrl;

	private Integer systemId;
	
	private Double  serviceMoney;
	
	public Double getUnit() {
		return unit;
	}

	public void setUnit(Double unit) {
		this.unit = unit;
	}

	public Integer getSecurityCodeCountNumber() {
		return securityCodeCountNumber;
	}

	public void setSecurityCodeCountNumber(Integer securityCodeCountNumber) {
		this.securityCodeCountNumber = securityCodeCountNumber;
	}

	public String getCallBackUrl() {
		return callBackUrl;
	}

	public void setCallBackUrl(String callBackUrl) {
		this.callBackUrl = callBackUrl;
	}

	public Integer getSystemId() {
		return systemId;
	}

	public void setSystemId(Integer systemId) {
		this.systemId = systemId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public Double getServiceMoney() {
		return serviceMoney;
	}

	public void setServiceMoney(Double serviceMoney) {
		this.serviceMoney = serviceMoney;
	}
}