package com.dayee.vo;

import java.util.Date;

public class KuaiYongYunResult {

	private String bindUUId;
	
	private Date calleeAcceptTime;
	private Date endCallTime;
	private Integer duration;
	private String recordfile;
	
	public String getBindUUId() {
		return bindUUId;
	}
	public void setBindUUId(String bindUUId) {
		this.bindUUId = bindUUId;
	}
	public Date getCalleeAcceptTime() {
		return calleeAcceptTime;
	}
	public void setCalleeAcceptTime(Date calleeAcceptTime) {
		this.calleeAcceptTime = calleeAcceptTime;
	}
	public Date getEndCallTime() {
		return endCallTime;
	}
	public void setEndCallTime(Date endCallTime) {
		this.endCallTime = endCallTime;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public String getRecordfile() {
		return recordfile;
	}
	public void setRecordfile(String recordfile) {
		this.recordfile = recordfile;
	}
}