package com.dayee.model;

import java.util.Date;

public class XiaoJianRenConsumptionLog {

	public static String REVIEW_STATUS_AUDITED = "已审核";
	public static String REVIEW_STATUS_UNAUDITED = "未审核";
	public static String REVIEW_STATUS_NOTICE_SEND = "已通知发放";
	public static String REVIEW_STATUS_CONFIRM_SEND = "已发放";
	public static String REVIEW_STATUS_CONFIRM_SEND_CANCEL = "已发放,可撤销";
	public static String REVIEW_STATUS_CONFIRM_SEND_RECEIVED = "已领取";
	public static String REVIEW_STATUS_SEND_FAIL = "发放失败";
	
	private Integer id;
	
	private String recommender;
	private String candidate;
	private String recommenderPhone;
	private String recommendPostName;
	private String orgName;
	private Integer orgId;
	private String candidatePhone;
	private String candidateStatus;
	private Double reward;
	private String remarke;
	private Date sendTime;
	private String status;
	private Integer statusNumber;
	private Date reviewTime;
	private Date applyTime;
	private String companyName;
	private Integer companyId;
	private String recommendMode;
	private String uniqueCode;
	
	private Date recommendTime;
	private Date entryTime;
	private String reviewer;
	
	private String bonusId;
	
	private Integer prePaId;
	
	private Integer paidAccountId;
	
	private String paidAccountName;
	
	private String auditNotWithApply;
	
	/**悬赏金预计发放时间*/
	private Date issueDate;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRecommender() {
		return recommender;
	}
	public void setRecommender(String recommender) {
		this.recommender = recommender;
	}
	public String getCandidate() {
		return candidate;
	}
	public void setCandidate(String candidate) {
		this.candidate = candidate;
	}
	public String getRecommenderPhone() {
		return recommenderPhone;
	}
	public void setRecommenderPhone(String recommenderPhone) {
		this.recommenderPhone = recommenderPhone;
	}
	public String getRecommendPostName() {
		return recommendPostName;
	}
	public void setRecommendPostName(String recommendPostName) {
		this.recommendPostName = recommendPostName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getCandidatePhone() {
		return candidatePhone;
	}
	public void setCandidatePhone(String candidatePhone) {
		this.candidatePhone = candidatePhone;
	}
	public String getCandidateStatus() {
		return candidateStatus;
	}
	public void setCandidateStatus(String candidateStatus) {
		this.candidateStatus = candidateStatus;
	}
	public Double getReward() {
		return reward;
	}
	public void setReward(Double reward) {
		this.reward = reward;
	}
	public String getRemarke() {
		return remarke;
	}
	public void setRemarke(String remarke) {
		this.remarke = remarke;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getReviewTime() {
		return reviewTime;
	}
	public void setReviewTime(Date reviewTime) {
		this.reviewTime = reviewTime;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public String getRecommendMode() {
		return recommendMode;
	}
	public void setRecommendMode(String recommendMode) {
		this.recommendMode = recommendMode;
	}
	public Date getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}
	public String getUniqueCode() {
		return uniqueCode;
	}
	public void setUniqueCode(String uniqueCode) {
		this.uniqueCode = uniqueCode;
	}
	public Date getRecommendTime() {
		return recommendTime;
	}
	public void setRecommendTime(Date recommendTime) {
		this.recommendTime = recommendTime;
	}
	public Date getEntryTime() {
		return entryTime;
	}
	public void setEntryTime(Date entryTime) {
		this.entryTime = entryTime;
	}
	public String getReviewer() {
		return reviewer;
	}
	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}
	public String getBonusId() {
		return bonusId;
	}
	public void setBonusId(String bonusId) {
		this.bonusId = bonusId;
	}
	public Integer getPrePaId() {
		return prePaId;
	}
	public void setPrePaId(Integer prePaId) {
		this.prePaId = prePaId;
	}
    public Integer getPaidAccountId() {
    
        return paidAccountId;
    }
    
    public void setPaidAccountId(Integer paidAccountId) {
    
        this.paidAccountId = paidAccountId;
    }
    public String getPaidAccountName() {
    
        return paidAccountName;
    }
    public void setPaidAccountName(String paidAccountName) {
    
        this.paidAccountName = paidAccountName;
    }
    public Integer getOrgId() {
    
        return orgId;
    }
    public void setOrgId(Integer orgId) {
    
        this.orgId = orgId;
    }
    
    public String getAuditNotWithApply() {
    
        return auditNotWithApply;
    }
    public void setAuditNotWithApply(String auditNotWithApply) {
    
        this.auditNotWithApply = auditNotWithApply;
    }
    
    public Integer getStatusNumber() {
        if(status==null)return null;
        switch (status) {
        case "未审核":
            statusNumber = 1;
            break;
        case "已审核":
            statusNumber = 0;
            break;
        case "已发放":
            statusNumber = 4;
            break;
        case "已发放,可撤销":
            statusNumber = 0;
            break;
        case "已发放,已领取":
            statusNumber = 5;
            break;
        }
        return statusNumber;
    }
    public void setStatusNumber(Integer statusNumber) {
    
        this.statusNumber = statusNumber;
    }
    
    public Date getIssueDate() {
    
        return issueDate;
    }
    
    public void setIssueDate(Date issueDate) {
    
        this.issueDate = issueDate;
    }
}