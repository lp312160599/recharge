package com.dayee.vo;

public class YiLuVo {

    private Integer id;
	
    private Double price;
	
	private Integer securityCodeCountNumber;
	
	private String callBackUrl;

	private Integer systemId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
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
}