package com.dayee.interceptors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.dayee.exception.ExceptionCode;
import com.dayee.model.CompanySystem;
import com.dayee.service.CompanyService;
import com.dayee.utils.EncryptUtils;
import com.dayee.utils.QueryUtil;
import com.dayee.utils.StringUtils;

public class InterFacePermissionValidationInterceptor extends HandlerInterceptorAdapter {

    Logger log = Logger.getLogger(getClass());
    
	@SuppressWarnings("unchecked")
    @Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
		CompanyService companyService = wac.getBean(CompanyService.class);
		String uri = request.getRequestURI();
		HashMap<String,String> result = new HashMap<String,String>();
		List<CompanySystem>  list = null;
		String secretKey = null;
		
		log.info("访问地址===>"+uri);
		if(uri.contains("dayee/video")){
		    Map<String, Object> map =  null;
		    
		    String contType = request.getContentType();
		    
		    if(contType!=null&&contType.contains("application/json")){
		        String body =  getBody(request);
		        try{
		            map =JSONArray.parseObject(body, Map.class);
		        }catch (JSONException e) {
		            result.put("msg","json解析异常");
		            result.put("error",ExceptionCode.ERROR_10006);
		            response.setContentType("application/json;charset=utf-8");
		            response.getWriter().print(JSONArray.toJSON(result));
		            return false;
                }
		    }else{
		        Map<String, String[]> m =  request.getParameterMap();
		        map = new HashMap<String, Object>();
		        for(String k :m.keySet()){
		            if(m.get(k)!=null&&m.get(k).length!=0){
		                map.put(k, m.get(k)[0]);
		            }
		        }
		    }
		    
		    log.info("参数===>"+map);
		    
		    Object timestamp = map.get("timestamp");
		    Object apiKey = map.get("apiKey");
		    Object sign = map.get("sign");
		    Object systemType = map.get("systemType");
		    
		    long nowTimestamp = new Date().getTime();
		    if(timestamp==null){
                result.put("msg","时间戳不能为空!");
		    }else if(!timestamp.toString().matches("\\d{13}")){
                result.put("msg","时间戳错误!");
		    }else if(apiKey==null){
		        result.put("msg","apiKey不能为空!");
		    }else if(sign==null){
		        result.put("msg","sign不能为空!");
            }else if(systemType==null){
                result.put("msg","systemType不能为空!");
            }else{
                long time = new Date(Long.valueOf(timestamp.toString())).getTime();
                if((nowTimestamp-time)>(60*1000*2)){
                    result.put("msg","请求过期!");
                }else{
                    map.remove("sign");
                    Object keyNames[] =  map.keySet().toArray();
                    Arrays.sort(keyNames);
                    String corpCode = EncryptUtils.decode(apiKey.toString());
                    if(corpCode.endsWith("_"+systemType)) {
                        corpCode = corpCode.substring(0,corpCode.lastIndexOf("_"));
                    }
                    String sk = DigestUtils.md5Hex(corpCode);
                    String correctsSign = getSign(keyNames, map, sk);
                    log.info("correctsSign===>"+correctsSign);
                    if(!sign.equals(correctsSign)){
                        result.put("msg","签名验证失败!");
                    }else{
                        if(corpCode.equals("corpSynchronize")){
                            secretKey = "corpSynchronize";
                        }else{
                            secretKey = EncryptUtils.encode(corpCode+"_"+systemType);
                        }
                    }
                }
            }
		    
		    if(result.containsKey("msg")){
		        result.put("error",ExceptionCode.ERROR_10007);
		    }
		}else{
		    secretKey = request.getParameter("secretKey");
		    if(StringUtils.isEmpty(secretKey)){
		        result.put("msg","secretKey不能为空");
		        result.put("error",ExceptionCode.ERROR_1);
		    }
		}
		
		if(StringUtils.isNotEmpty(secretKey)
		   &&!secretKey.equals("corpSynchronize")){
		    QueryUtil q = QueryUtil.createQuery().eq("secretKey", secretKey);
		    list = companyService.query(q.get(),CompanySystem.class);
		    if(list.size()==0){
		        result.put("msg","企业不存在");
		        result.put("error",ExceptionCode.ERROR_10008);
		    }
		}
		
		
		if(result.containsKey("msg")){
	        response.setContentType("application/json;charset=utf-8");
	        response.getWriter().print(JSONArray.toJSON(result));
	        return false;
		}else{
		    if(handler instanceof HandlerMethod){  
                HandlerMethod method = (HandlerMethod)handler;  
                Object obj = method.getBean();
                Method m = obj.getClass().getMethod("setCompanySystem", CompanySystem.class);
                if(m!=null&&list!=null){
                    m.invoke(obj, list.get(0));
                }
            }
            return true;
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public static void main(String[] args) {

	    String corpCode = EncryptUtils.decode("54d1f00347c3a6ff");
	    HashMap<String,Object> h = new HashMap<String, Object>();
        TreeMap<String,Object> m = new TreeMap<String,Object>();
        ArrayList< HashMap<String,Object>> a =  new ArrayList<HashMap<String,Object>>();
        h.put("credits", -1);
        h.put("externalKey", "1212");
        a.add(h);
        m.put("timestamp",new Date().getTime());
        m.put("apiKey",corpCode);
        m.put("systemType",6);
        m.put("creditsArray",JSONArray.toJSONString(a));
        m.put("sign",new InterFacePermissionValidationInterceptor().getSign(m.keySet().toArray(),(Map) m,DigestUtils.md5Hex("pupu")));
        m.put("creditsArray",a);
        
        System.out.println(JSONArray.toJSONString(m));
        m.remove("sign");
        
        m.put("earlyWarningSeconds", 1);
        m.put("actualSeconds", 50);
        m.put("number","1212121" );
        m.put("orgName","ceshiyixia " );
        m.put("phone", "13389230227");
        m.put("videoResource",3 );
        m.put("startTime", "2018-12-26 10:00:00");
        m.put("endTime","2018-12-27 10:00:00");
        
        m.put("sign",new InterFacePermissionValidationInterceptor().getSign(m.keySet().toArray(),(Map) m,DigestUtils.md5Hex("pupu")));
        
        System.out.println(JSONArray.toJSONString(m));
        
        System.out.println(EncryptUtils.decode("87e587c04e3e0ef6"));
    }
	
	public String getBody(HttpServletRequest request) throws IOException{
	   BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8")) ;
	   String str="";
	   String temp = null;
	   while((temp=reader.readLine())!=null){
	       str+=temp;
	   }
	   return str;
	}
	
	public  String getSign( Object keyNames[],Map<String, Object> parameter, String secretKey) {

        String sign = assemblyParameter(keyNames, parameter);
        log.info("加密前sign===>"+sign);
        sign = sign.substring(0, sign.length() - 1) + secretKey;
        return DigestUtils.md5Hex(sign);
    }
	
	public  String assemblyParameter( Object keyNames[],Map<String, Object> parameter) {

        String sign = "";
        for (Object key : keyNames) {
            Object value = parameter.get(key);
            if(value!=null){
                sign += key + "=" + value + "&";
            }
        }
        return sign;
    }
}