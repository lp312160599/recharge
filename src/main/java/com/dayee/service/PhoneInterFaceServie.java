package com.dayee.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dayee.dao.PhoneInterFaceDao;
import com.dayee.exception.PhoneInterfaceException;
import com.dayee.model.CallConsumption;
import com.dayee.model.CompanySystem;
import com.dayee.model.Config;
import com.dayee.model.InterfaceRequestData;
import com.dayee.model.Systems;
import com.dayee.utils.ApplicationConfigCache;
import com.dayee.utils.Base64;
import com.dayee.utils.DateUtil;
import com.dayee.utils.MD5Util;
import com.dayee.utils.NumberUtil;
import com.dayee.utils.QueryUtil;
import com.dayee.utils.SendRequest;
import com.dayee.vo.PhoneConsumptionLog;

@Service
public class PhoneInterFaceServie extends BaseService<Object> {

	@Resource
	PhoneInterFaceDao phoneInterFaceDao;
	@Resource
	PhoneService phoneService;
	@Resource
	RechargeService rechargeService;
	@Resource
	SystemService systemService;
	@Resource
	CompanySystemService companySystemService;
	
	public static void main(String[] args) throws ParseException, ClientProtocolException, IOException {
		/**快用云测试**/
		String time = DateUtil.dateToStringByNow(DateUtil.YYYYMMDDHHMMSS);
		HashMap<String,String> map = new HashMap<String, String>();
		map.put("sign", MD5Util.toMd5("shanghaidayi"+"sxb68w1k1n7z0x291g2k10k5e17h0297"+time));
		map.put("authorization", Base64.encodeToString(("shanghaidayi"+":"+time).getBytes(), Base64.NO_WRAP));
		map.put("appId", "shanghaidayi");
		map.put("caller", "13389230227");
		map.put("callee", "18629016747");
		String body = SendRequest.sendPost("http://sip.kyong.net/api/callback/api/startcall.php", new HashMap<String,String>(), JSONArray.toJSON(map).toString(), "UTF-8").getBody();
		JSONObject  jsonObject  = JSONArray.parseObject(body);
		String code = jsonObject.getString("code");
		System.out.println(code);
		
		
		
		
	    /**融赢测试
		String time = DateUtil.dateToStringByNow(DateUtil.YYYYMMDDHHMMSS);
		String applyId = "0000000066e1f2b7016710c2f9ba026a";
		String token = "03d73b246c14d5a96c49bd59f3e5de8d";
		String companyAccount = "dayiyunjisuan";
		String authorization = applyId+":"+time+":"+token;
		authorization = Base64.encodeToString(authorization.getBytes(), Base64.NO_WRAP);
		String sig     = applyId+":"+companyAccount+":"+time;
		sig = MD5Util.toMd5(sig).toUpperCase();
		HashMap<String,String> para = new HashMap<String, String>();
		HashMap<String,String> header = new HashMap<String, String>();
		
		header.put("Accept","application/json");
		header.put("Content-Type","application/json;charset=utf-8");
		header.put("Authorization",authorization);
		
		para.put("Phone","13389230227");
		para.put("voipAccount","80379200000004");
		para.put("CompanyName",companyAccount);
		
		      String body = SendRequest.sendPost("http://47.96.62.197:8090/bind/agentOnWork/v2?Sig="+sig, header, JSONArray.toJSONString(para), "UTF-8").getBody();
		      System.out.println(body);
		     
		String body = SendRequest.sendPost("http://47.96.62.197:8090/bind/callEvent/v2?Sig="+sig, header, JSONArray.toJSONString(para), "UTF-8").getBody();
		System.out.println(body);
	   **/
	}
	
	public String callPhoneByKuaiYongYun(CompanySystem companySystem,String orgName,String accountName,String caller,String called,String systemType) throws Exception{
		log.info("开始拨打快用云电话====>"+getColumnValues(companySystem)+",=====>orgName:"+orgName+"===accountName:"+accountName+"===>caller"+caller+"====>called:"+called);
        Config config = query(Config.class).get(0);
		String time = DateUtil.dateToStringByNow(DateUtil.YYYYMMDDHHMMSS);
		HashMap<String,String> map = new HashMap<String, String>();
		map.put("sign", MD5Util.toMd5(config.getAppId()+config.getKeyCode()+time));
		map.put("authorization", Base64.encodeToString((config.getAppId()+":"+time).getBytes(), Base64.NO_WRAP));
		map.put("appId", config.getAppId());
		map.put("caller", caller);
		map.put("callee", called);
		log.info("开始拨打快用云电话====>发送参数==>"+map);
		String body = SendRequest.sendPost("http://sip.kyong.net/api/callback/api/startcall.php", new HashMap<String,String>(), JSONArray.toJSON(map).toString(), "UTF-8").getBody();
		log.info("开始拨打快用云电话====>返回结果==>"+map);
		JSONObject  jsonObject  = JSONArray.parseObject(body);
		String code = jsonObject.getString("code");
		if(code.equals("0")){
			jsonObject  = JSONArray.parseObject(jsonObject.getString("data"));
			String session = jsonObject.getString("session");
			addConsumptionInfo(companySystem, session, accountName, caller, called, orgName,systemType);
			return session;
		}else{
			throw new PhoneInterfaceException(jsonObject.get("msg"));
		}	
	}
	public String callPhoneByRongYing(CompanySystem companySystem,String orgName,String accountName,String caller,String called,String systemType) throws Exception{
		log.info("开始拨打容赢电话====>"+getColumnValues(companySystem)+",=====>orgName:"+orgName+"===accountName:"+accountName+"===>caller"+caller+"====>called:"+called);

		String time = DateUtil.dateToStringByNow(DateUtil.YYYYMMDDHHMMSS);
		String applyId = ApplicationConfigCache.getRongYingApplyId();
		String token = ApplicationConfigCache.getRongYingToken();
		String companyAccount = ApplicationConfigCache.getRongYingCompanyAccount();
		String authorization = applyId+":"+time+":"+token;
		authorization = Base64.encodeToString(authorization.getBytes(), Base64.NO_WRAP);
		String sig     = applyId+":"+companyAccount+":"+time;
		log.info("sig===>"+sig);
		sig = MD5Util.toMd5(sig).toUpperCase();
		HashMap<String,String> para = new HashMap<String, String>();
		HashMap<String,String> header = new HashMap<String, String>();
		
		header.put("Accept","application/json");
		header.put("Content-Type","application/json;charset=utf-8");
		header.put("Authorization",authorization);
		
		para.put("Phone",called);
		para.put("voipAccount",caller);
		para.put("CompanyName",companyAccount);
		
		String body = SendRequest.sendPost("http://47.96.62.197:8090/bind/agentOnWork/v2?Sig="+sig, header, JSONArray.toJSONString(para), "UTF-8").getBody();
		log.info("客服上班接口调用===>"+body);
		body = SendRequest.sendPost("http://47.96.62.197:8090/bind/callEvent/v2?Sig="+sig, header, JSONArray.toJSONString(para), "UTF-8").getBody();
		JSONObject  jsonObject  = JSONArray.parseObject(body);
		String statuscode = jsonObject.getString("statuscode");
		if(statuscode.equals("200")){
			String session = jsonObject.getString("data");
			addConsumptionInfo(companySystem, session, accountName, caller, called, orgName,systemType);
			return session;
		}else{
			throw new PhoneInterfaceException(jsonObject.get("statusmsg"));		
		}
	}
	
	public void addConsumptionInfo(CompanySystem companySystem,String number,String accountName,String caller,String called,String orgName,String systemType) throws Exception{
		
			CallConsumption callConsumption = new CallConsumption();
			callConsumption.setNumber(number);
			callConsumption.setMoney(0d);
			callConsumption.setSysCompanyId(companySystem.getId());
			callConsumption.setSystem(companySystem.getName());
			callConsumption.setAccountNumber(accountName);
			callConsumption.setCalled(called);
			callConsumption.setCaller(caller);
			callConsumption.setOrgName(orgName);
			callConsumption.setSystemType(systemType);
			addObject(callConsumption);
	
	}
	
	
	public void billGetByRongYing() throws Exception {

		Systems systems = systemService.queryById(5);
		List<CallConsumption> callConsumptionList =  phoneService.query(QueryUtil.createQuery().noEq("status","0").eq("system",systems.getName()).get());
        
		String time = DateUtil.dateToStringByNow(DateUtil.YYYYMMDDHHMMSS);
		String applyId = ApplicationConfigCache.getRongYingApplyId();
		String token = ApplicationConfigCache.getRongYingToken();
		String companyAccount = ApplicationConfigCache.getRongYingCompanyAccount();
		String authorization = applyId+":"+time+":"+token;
		authorization = Base64.encodeToString(authorization.getBytes(), Base64.NO_WRAP);
		String sig     = applyId+":"+companyAccount+":"+time;
		sig = MD5Util.toMd5(sig).toUpperCase();;
		HashMap<String,String> para = new HashMap<String, String>();
		HashMap<String,String> header = new HashMap<String, String>();
		
		header.put("Accept","application/json");
		header.put("Content-Type","application/json;charset=utf-8");
		header.put("Authorization",authorization);
		
		para.put("CompanyName", companyAccount);
		
		for (int i = 0; i < callConsumptionList.size(); i++) {
			para.put("CallSid", callConsumptionList.get(i).getNumber());
			para.put("MaxId", "1");
			String body = SendRequest.sendPost("http://47.96.62.197:8090/query/callRecord/v1?Sig="+sig, header,  JSONArray.toJSONString(para), "UTF-8").getBody();
			JSONObject  jsonObject  = JSONArray.parseObject(body);
			String statuscode = jsonObject.getString("statuscode");
			if(statuscode.equals("200")){
				JSONArray data = jsonObject.getJSONArray("data");
				if(data==null||data.size()==0)continue;
				jsonObject = jsonObject.getJSONArray("data").getJSONObject(0);
				callBack(callConsumptionList.get(i).getNumber()
						 , jsonObject.getDate("Start_Stamp"), jsonObject.getDate("End_Stamp")
						 , jsonObject.getInteger("Billsec"),jsonObject.getString("RecordUrl"));
			}else{
				log.error("话单获取失败===>"+callConsumptionList.get(i).getNumber()+"===>"+jsonObject.get("statusmsg"));
			}
		}
		
		
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void callBack(String bindUUId,Date calleeAcceptTime,Date endCallTime,int duration,String recordfile) throws Exception{
		
		if(calleeAcceptTime==null||endCallTime==null||calleeAcceptTime.equals(endCallTime)){
			duration =0;
		}else{
			duration = duration/60;
			if(duration==0)duration = 1;
			else duration = duration%60==0?duration:duration+1;
		}
		
		log.info("回调number===>"+bindUUId);
		QueryUtil q =  QueryUtil.createQuery().eq("number",bindUUId);
 		List<CallConsumption> list = query(q.get(),CallConsumption.class);
 		log.info("回调查询数据===>"+list.size());
		if(list.size()!=0){
			CallConsumption callConsumption = list.get(0);
			
			q =  QueryUtil.createQuery().eq("id",callConsumption.getSysCompanyId());
			CompanySystem companySystem = query(q.get(),CompanySystem.class).get(0);
			
			callConsumption.setStartTime(calleeAcceptTime);
			callConsumption.setEndTime(endCallTime);
			callConsumption.setDuration(duration);
			callConsumption.setUnit(companySystem.getUnit());
			callConsumption.setMoney(NumberUtil.division(duration, companySystem.getUnit()));
			callConsumption.setStatus(0);
			
			log.info("StartTime===>"+calleeAcceptTime);
			log.info("EndTime===>"+endCallTime);
			log.info("duration===>"+duration);
			log.info("执行更新callConsumption===>");
			updateObjectById(callConsumption);
			log.info("执行更新公司余额===>开始");
			companySystemService.updateBalance(companySystem, -callConsumption.getMoney(),CompanySystemService.TYPE_CONSUMPTION);
			log.info("执行更新公司余额===>结束");
			updateObjectById(companySystem);
			log.info("更新企业信息===>"+companySystem.getCountMoney());
			
			String callBackUrl = companySystem.getCallBackUrl();
			log.info("企业回调地址===>"+callBackUrl);
			
			PhoneConsumptionLog phoneConsumptionLog = new PhoneConsumptionLog();
			phoneConsumptionLog.setNumber(bindUUId);
			phoneConsumptionLog.setCallId(bindUUId);
			phoneConsumptionLog.setMoney(callConsumption.getMoney());
			phoneConsumptionLog.setCallTime(duration);
			phoneConsumptionLog.setRecordingUrl(recordfile);
			phoneConsumptionLog.setStartTime(calleeAcceptTime);
			phoneConsumptionLog.setEndTime(endCallTime);
			phoneConsumptionLog.setUnitPrice(companySystem.getUnit());
			phoneConsumptionLog.setSystemType(callConsumption.getSystemType());
			String body = JSONArray.toJSONString(phoneConsumptionLog);
			
			InterfaceRequestData in = new InterfaceRequestData(callBackUrl, InterfaceRequestData.METHOD_POST, "", body, InterfaceRequestData.PARAMETER_TYPE_BODY, "0",new Date());
			addObject(in);
		}
	}
	
}