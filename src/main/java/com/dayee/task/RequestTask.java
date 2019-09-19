package com.dayee.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.dayee.model.InterfaceRequestData;
import com.dayee.service.InterfaceRequestDataService;
import com.dayee.utils.ApplicationConfigCache;
import com.dayee.utils.Ex;
import com.dayee.utils.QueryUtil;
import com.dayee.utils.SendRequest;

@Component
public class RequestTask {

	@Resource
	private InterfaceRequestDataService interfaceRequestDataService;
	
	@SuppressWarnings("unchecked")
	public void run(){
		try{
			QueryUtil queryUtil = QueryUtil.createQuery().in("status",new  String[]{InterfaceRequestData.STATUS_NOT_SEND,InterfaceRequestData.STATUS_SEND_FAIL})
					                                       .le("errorNumber",ApplicationConfigCache.getRequestErrorCount());
			List<InterfaceRequestData> list = interfaceRequestDataService.query(queryUtil.get());
			
			for(InterfaceRequestData data :list){
				
				HashMap<String,String> header = new HashMap<String,String>();
				if(!StringUtils.isEmpty(data.getHeader())){
					header = JSONArray.parseObject(data.getHeader(), HashMap.class);
				}
				try{
					String result = null;
					if(data.getMethod().equals(InterfaceRequestData.METHOD_GET)){
						result = SendRequest.sendGet(data.getUrl()).getBody();
					}else if(data.getParameterType().equals(InterfaceRequestData.PARAMETER_TYPE_BODY)){
						result = SendRequest.sendPost(data.getUrl(), header, data.getBody(), "UTF-8").getBody();
					}else{
						HashMap<String,String> parameter = new HashMap<String,String>();
						if(!StringUtils.isEmpty(data.getBody())){
							parameter = JSONArray.parseObject(data.getBody(), new TypeReference<HashMap<String,String>>(){});
						}
						result = SendRequest.sendPost(data.getUrl(), header, parameter, "UTF-8").getBody();
					}
					if(!StringUtils.isEmpty(result)
					  &&result.matches(data.getValidateTextRegular())){
						data.setStatus(InterfaceRequestData.STATUS_SEND_SUCCESS);
					}else{
						data.setStatus(InterfaceRequestData.STATUS_SEND_FAIL);
					}
					if(result.indexOf("html>")==-1){
					    data.setResult(result);
					}else{
					    Logger.getLogger(RequestTask.class).error("id:"+data.getId()+"=================>"+result);
					}
				}catch (Exception e) {
					data.setStatus(InterfaceRequestData.STATUS_SEND_FAIL);
					data.setResult(Ex.gEx(e));
					Logger.getLogger(RequestTask.class).error(e.getMessage(),e);
				}
				data.setTopSendTime(new Date());
				if(data.getStatus().equals(InterfaceRequestData.STATUS_SEND_FAIL)){
					data.setErrorNumber(data.getErrorNumber()+1);
				}
				interfaceRequestDataService.updateObjectById(data);
			}
		}catch (Exception e) {
		    Logger.getLogger(RequestTask.class).error(e.getMessage(),e);
		}
	}
}
