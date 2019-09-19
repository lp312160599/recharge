package com.dayee.utils;


import java.io.IOException;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

/**
 * 封住请求返回的参数
 * @author Legend、
 *
 */

public class Result {
    
	private String cookie;
	private int statusCode;
	private HashMap<String, Header> headerAll;
	private HttpEntity httpEntity;
	public String getCookie() {
		return cookie;
	}
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public HashMap<String, Header> getHeaders() {
		return headerAll;
	}
	
	public void setHeaders(Header[] headers){
		headerAll = new HashMap<String, Header>();
		for (Header header : headers) {
			headerAll.put(header.getName(), header);
		}
	}
	public HttpEntity getHttpEntity() {
		return httpEntity;
	}
	public void setHttpEntity(HttpEntity httpEntity) {
		this.httpEntity = httpEntity;
	}
	
	public String getBody() throws ParseException, IOException{
		String body = EntityUtils.toString(httpEntity,"UTF-8");
		LoggerFactory.getLogger(Result.class).info(",body==>"+body);;
		return body;
	}
	
	public String getBody(String encoding) throws ParseException, IOException{
		String body = EntityUtils.toString(httpEntity,encoding);
		LoggerFactory.getLogger(Result.class).info(",body==>"+body);;
		return body;
	}
	
}
