package com.dayee.model;

import java.util.Date;

public class Recharge {
    
    public static String TYPE_RECHARGE = "充值";
    public static String TYPE_RECHARGE_REFUND = "退款";

	private Integer id;
	
	private String type;
	
	private Double money;
	
	private String description;
	
	private String state;
	
	private String auditor;
	
	private String rechargeUser;
	
	private Date createTime;
	
	private String system;
	
	private Integer companyId;
	
	private Integer systemId;
	
	private Date validityPeriod;
	
	/**大易视频系统专用字段,充值分钟**/
	private Integer minute;
	
    private Integer fileSize;
    
    private Date spaceEndTime;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public String getRechargeUser() {
		return rechargeUser;
	}

	public void setRechargeUser(String rechargeUser) {
		this.rechargeUser = rechargeUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getSystemId() {
		return systemId;
	}

	public void setSystemId(Integer systemId) {
		this.systemId = systemId;
	}

	public Date getValidityPeriod() {
		return validityPeriod;
	}

	public void setValidityPeriod(Date validityPeriod) {
		this.validityPeriod = validityPeriod;
	}
    
    public Integer getMinute() {
    
        return minute;
    }
    
    public void setMinute(Integer minute) {
    
        this.minute = minute;
    }

    
    public Integer getFileSize() {
    
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
    
        this.fileSize = fileSize;
    }
    
    public Date getSpaceEndTime() {
    
        return spaceEndTime;
    }
    
    public void setSpaceEndTime(Date spaceEndTime) {
    
        this.spaceEndTime = spaceEndTime;
    }
}