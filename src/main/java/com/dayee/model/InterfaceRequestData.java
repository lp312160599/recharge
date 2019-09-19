package com.dayee.model;

import java.util.Date;

import com.dayee.vo.InterFaceRequestCallBack;

public class InterfaceRequestData {

	public static final String METHOD_POST="post";
	public static final String METHOD_GET="get";
	
	public static final String STATUS_NOT_SEND="未发送";
	public static final String STATUS_SEND_SUCCESS="发送成功";
	public static final String STATUS_SEND_FAIL="发送失败";
	
	public static final String PARAMETER_TYPE_BODY="body";
	public static final String PARAMETER_TYPE_PARAMETER="parameter";
	
	private Integer id;
	
	private String url;
	private String method;
	private String header;
	private String body;
	private String parameterType;
	private String status;
	private String validateTextRegular;
	private Integer errorNumber ;
	private Date addDate;
	private Date topSendTime;
	private Date nextSendTime;
	private String result;
	
	public InterfaceRequestData(){}
	
	public InterfaceRequestData(String url, String method, String header,
			String body, String parameterType,
			String validateTextRegular, Date addDate) {
		this.url = url;
		this.method = method;
		this.header = header;
		this.body = body;
		this.parameterType = parameterType;
		this.validateTextRegular = validateTextRegular;
		this.addDate = addDate;
		this.status = "未发送";
	}
	
	public InterfaceRequestData(String url, String method, String header,
			String body, String parameterType,
			String validateTextRegular, Date addDate,InterFaceRequestCallBack callBack) {
		
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getParameterType() {
		return parameterType;
	}
	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getValidateTextRegular() {
		return validateTextRegular;
	}
	public void setValidateTextRegular(String validateTextRegular) {
		this.validateTextRegular = validateTextRegular;
	}
	public Integer getErrorNumber() {
		return errorNumber;
	}
	public void setErrorNumber(Integer errorNumber) {
		this.errorNumber = errorNumber;
	}
	public Date getAddDate() {
		return addDate;
	}
	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}
	public Date getTopSendTime() {
		return topSendTime;
	}
	public void setTopSendTime(Date topSendTime) {
		this.topSendTime = topSendTime;
	}
	public Date getNextSendTime() {
		return nextSendTime;
	}
	public void setNextSendTime(Date nextSendTime) {
		this.nextSendTime = nextSendTime;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}