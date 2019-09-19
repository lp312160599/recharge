package com.dayee.model;

import java.util.Date;

import com.dayee.annotation.NoColumn;

public class RechargeAudit {

	@NoColumn
	public static final String NO_SEND="待发送";
	@NoColumn
	public static final String NO_AUDIT="待审核";
	@NoColumn
	public static final String OK="通过";
	@NoColumn
	public static final String FAIL="拒绝";
	
	private Integer id;
	
	private String auditorEmail;
	
	private String auditor;
	
	private String state;
	
	private Integer rechargeId;
	
	private Date auditorTime;
	
	private String sendMsg;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAuditorEmail() {
		return auditorEmail;
	}

	public void setAuditorEmail(String auditorEmail) {
		this.auditorEmail = auditorEmail;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getRechargeId() {
		return rechargeId;
	}

	public void setRechargeId(Integer rechargeId) {
		this.rechargeId = rechargeId;
	}

	public Date getAuditorTime() {
		return auditorTime;
	}

	public void setAuditorTime(Date auditorTime) {
		this.auditorTime = auditorTime;
	}

	public String getSendMsg() {
		return sendMsg;
	}

	public void setSendMsg(String sendMsg) {
		this.sendMsg = sendMsg;
	}
}
