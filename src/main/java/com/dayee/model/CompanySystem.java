package com.dayee.model;

import java.math.BigDecimal;
import java.util.Date;

import com.dayee.utils.StringUtils;

public class CompanySystem {

	public static final int TYPE_KUAI_YONG_YUN = 1;
	public static final int TYPE_BACKGROUND = 2;
	public static final int TYPE_XIAO_JIAN_REN = 3;
	public static final int TYPE_YILU = 4;
	public static final int TYPE_RONG_YING = 5;
	public static final int TYPE_DAYEE_VIDEO = 6;
	
	private Integer id;
	
	private Integer companyId;
	
	private String name;
	
	private Double balance;
	
	private Double applyReward;
	
	private Integer duration;
	
	private Double countMoney;
	
	private Integer remainingDuration;
	
	private String companyName;
	
	private Double rechargeMoney;
	
	private Double unit;
	
	private Integer securityCodeCountNumber;
	
	private Integer useSecurityCode;
	
	private Date createTime;
	
	private Integer createUserId;
	
	private String  createUserName;
	
	private String secretKey;
	
	private String callBackUrl;
	
	private String xiaoJianRenCallBackUrl;
	
	private Integer systemId;
	
	private Integer backgroundNumber;
	
	private Double  backgroundSimple;
	private Double  backgroundEducation;
	private Double  backgroundPractical;
	private Double  backgroundBusinessConflict;
	private Double  backgroundBachelorScience;
	private Double  backgroundStandard;
	private Double  backgroundEarlyEducation;
	private Double  xiaoJianRenIssuedMoney;
	
	private Double  price;
	
	private Date  validityPeriod;
	
	private Double  serviceMoney;;
	
	private Integer  dayeeVideoType;
	
	private Integer rechargeTime;

	private Integer remainingTime;
	
	private Integer balanceTime;
	
	private Integer warning;
	
	private Integer paidAccountWarning;
	
	private Integer creditsMultiple;
	
	private Integer useTime;
	
	private Double useMoney;
	
	private Integer fileSize;
	
	private Integer useFileSize;
	
	private Integer remainingFileSize;
	
	private Date spaceEndTime;
	
	/**商户充值金额*/
	private Double merchantRechargeMoney;
	
	/**商户可用余额*/
	private Double merchantBalance;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getBalance() {
	    if(balance!=null) {
          return new BigDecimal(balance).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	    }
		return balance;
	}

	public void setBalance(Double balance) { 
	    
		this.balance = balance;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Double getCountMoney() {
		return countMoney;
	}

	public void setCountMoney(Double countMoney) {
		this.countMoney = countMoney;
	}

	public Integer getRemainingDuration() {
		return remainingDuration;
	}

	public void setRemainingDuration(Integer remainingDuration) {
		this.remainingDuration = remainingDuration;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Double getRechargeMoney() {
		return rechargeMoney;
	}

	public void setRechargeMoney(Double rechargeMoney) {
		this.rechargeMoney = rechargeMoney;
	}

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

	public Integer getUseSecurityCode() {
		return useSecurityCode;
	}

	public void setUseSecurityCode(Integer useSecurityCode) {
		this.useSecurityCode = useSecurityCode;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getCallBackUrl() {
		return callBackUrl;
	}

	public void setCallBackUrl(String callBackUrl) {
		this.callBackUrl = callBackUrl;
	}
	
	public String getCallBackUrlUnderline() {
        if(StringUtils.isNotEmpty(callBackUrl)){
            if(callBackUrl.endsWith("\\")){
                return callBackUrl.substring(0,callBackUrl.length()-1);
            }
        }
        return callBackUrl;
    }

	public Integer getSystemId() {
		return systemId;
	}

	public void setSystemId(Integer systemId) {
		this.systemId = systemId;
	}

	public Integer getBackgroundNumber() {
		return backgroundNumber;
	}

	public void setBackgroundNumber(Integer backgroundNumber) {
		this.backgroundNumber = backgroundNumber;
	}

	public Double getBackgroundSimple() {
		return backgroundSimple;
	}

	public void setBackgroundSimple(Double backgroundSimple) {
		this.backgroundSimple = backgroundSimple;
	}

	public Double getBackgroundEducation() {
		return backgroundEducation;
	}

	public void setBackgroundEducation(Double backgroundEducation) {
		this.backgroundEducation = backgroundEducation;
	}

	public Double getBackgroundPractical() {
		return backgroundPractical;
	}

	public void setBackgroundPractical(Double backgroundPractical) {
		this.backgroundPractical = backgroundPractical;
	}

	public Double getBackgroundBusinessConflict() {
		return backgroundBusinessConflict;
	}

	public void setBackgroundBusinessConflict(Double backgroundBusinessConflict) {
		this.backgroundBusinessConflict = backgroundBusinessConflict;
	}

	public Double getBackgroundBachelorScience() {
		return backgroundBachelorScience;
	}

	public void setBackgroundBachelorScience(Double backgroundBachelorScience) {
		this.backgroundBachelorScience = backgroundBachelorScience;
	}

	public Double getBackgroundStandard() {
		return backgroundStandard;
	}

	public void setBackgroundStandard(Double backgroundStandard) {
		this.backgroundStandard = backgroundStandard;
	}

	public Double getBackgroundEarlyEducation() {
		return backgroundEarlyEducation;
	}

	public void setBackgroundEarlyEducation(Double backgroundEarlyEducation) {
		this.backgroundEarlyEducation = backgroundEarlyEducation;
	}

	public String getXiaoJianRenCallBackUrl() {
		return xiaoJianRenCallBackUrl;
	}

	public void setXiaoJianRenCallBackUrl(String xiaoJianRenCallBackUrl) {
		this.xiaoJianRenCallBackUrl = xiaoJianRenCallBackUrl;
	}

	public Double getApplyReward() {
		return applyReward;
	}

	public void setApplyReward(Double applyReward) {
		this.applyReward = applyReward;
	}

	public Double getXiaoJianRenIssuedMoney() {
		return xiaoJianRenIssuedMoney;
	}

	public void setXiaoJianRenIssuedMoney(Double xiaoJianRenIssuedMoney) {
		this.xiaoJianRenIssuedMoney = xiaoJianRenIssuedMoney;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Date getValidityPeriod() {
		return validityPeriod;
	}

	public void setValidityPeriod(Date validityPeriod) {
		this.validityPeriod = validityPeriod;
	}

	public Double getServiceMoney() {
		return serviceMoney;
	}

	public void setServiceMoney(Double serviceMoney) {
		this.serviceMoney = serviceMoney;
	}
    
    public Integer getDayeeVideoType() {
    
        return dayeeVideoType;
    }

    
    public void setDayeeVideoType(Integer dayeeVideoType) {
    
        this.dayeeVideoType = dayeeVideoType;
    }

    public Integer getRechargeTime() {
    
        return rechargeTime;
    }
    
    public void setRechargeTime(Integer rechargeTime) {
    
        this.rechargeTime = rechargeTime;
    }
    
    public Integer getRemainingTime() {
    
        return remainingTime;
    }
    
    public void setRemainingTime(Integer remainingTime) {
    
        this.remainingTime = remainingTime;
    }

    public Integer getBalanceTime() {
    
        return balanceTime;
    }

    public void setBalanceTime(Integer balanceTime) {
    
        this.balanceTime = balanceTime;
    }

    
    public Integer getWarning() {
    
        return warning;
    }
    
    public void setWarning(Integer warning) {
    
        this.warning = warning;
    }

    public Integer getCreditsMultiple() {
    
        return creditsMultiple;
    }

    public void setCreditsMultiple(Integer creditsMultiple) {
    
        this.creditsMultiple = creditsMultiple;
    }
    
    public Integer getPaidAccountWarning() {
    
        return paidAccountWarning;
    }

    public void setPaidAccountWarning(Integer paidAccountWarning) {
    
        this.paidAccountWarning = paidAccountWarning;
    }

    public Integer getUseTime() {
    
        return useTime;
    }

    public void setUseTime(Integer useTime) {
    
        this.useTime = useTime;
    }

    
    public Double getUseMoney() {
    
        return useMoney;
    }

    
    public void setUseMoney(Double useMoney) {
    
        this.useMoney = useMoney;
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

    
    public Integer getUseFileSize() {
    
        return useFileSize;
    }

    
    public void setUseFileSize(Integer useFileSize) {
    
        this.useFileSize = useFileSize;
    }

    
    public Integer getRemainingFileSize() {
    
        return remainingFileSize;
    }
    
    public void setRemainingFileSize(Integer remainingFileSize) {
    
        this.remainingFileSize = remainingFileSize;
    }

    
    public Double getMerchantRechargeMoney() {
    
        return merchantRechargeMoney;
    }

    public void setMerchantRechargeMoney(Double merchantRechargeMoney) {
    
        this.merchantRechargeMoney = merchantRechargeMoney;
    }

    public Double getMerchantBalance() {
    
        return merchantBalance;
    }

    public void setMerchantBalance(Double merchantBalance) {
    
        this.merchantBalance = merchantBalance;
    }
}
