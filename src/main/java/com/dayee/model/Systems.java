package com.dayee.model;

public class Systems {

	private Integer id;
	
	private String name;
	
	private Double balance;
	
	private Integer remainingDuration;
	
	private Double callMoney;
	
	private Integer useCompany;
	
	private String icon;
	
	private Integer backgroundNumber;
	
	private Double issueMoney;//发放金额
	
	private Double recharge;//充值金额
	
	private Integer rechargeTime;//充值分钟
	

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

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Integer getRemainingDuration() {
		return remainingDuration;
	}

	public void setRemainingDuration(Integer remainingDuration) {
		this.remainingDuration = remainingDuration;
	}

	public Double getCallMoney() {
		return callMoney;
	}

	public void setCallMoney(Double callMoney) {
		this.callMoney = callMoney;
	}

	public Integer getUseCompany() {
		return useCompany;
	}

	public void setUseCompany(Integer useCompany) {
		this.useCompany = useCompany;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getBackgroundNumber() {
		return backgroundNumber;
	}

	public void setBackgroundNumber(Integer backgroundNumber) {
		this.backgroundNumber = backgroundNumber;
	}

	public Double getIssueMoney() {
		return issueMoney;
	}

	public void setIssueMoney(Double issueMoney) {
		this.issueMoney = issueMoney;
	}

	public Double getRecharge() {
		return recharge;
	}

	public void setRecharge(Double recharge) {
		this.recharge = recharge;
	}

    public Integer getRechargeTime() {
    
        return rechargeTime;
    }
    
    public void setRechargeTime(Integer rechargeTime) {
    
        this.rechargeTime = rechargeTime;
    }
}
