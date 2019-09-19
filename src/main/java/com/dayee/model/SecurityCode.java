package com.dayee.model;

public class SecurityCode {

	private Integer id;
	
	private String secretKey;
	
	private Integer companySystemId;
	
	private String state;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public Integer getCompanySystemId() {
		return companySystemId;
	}

	public void setCompanySystemId(Integer companySystemId) {
		this.companySystemId = companySystemId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}