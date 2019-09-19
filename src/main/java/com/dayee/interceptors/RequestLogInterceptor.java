package com.dayee.interceptors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.dayee.dao.BaseDao;
import com.dayee.model.User;

public class RequestLogInterceptor extends HandlerInterceptorAdapter{

    @Resource
    private BaseDao<Object> baseDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        Integer userId = null;
        
        Object o = request.getSession().getAttribute("user");
        if(o!=null) {
            userId = ((User)o).getId();
        }
        
        String contType = request.getContentType();
        StringBuffer logBuffer = new StringBuffer();
        logBuffer.append("有访问进入===>")
                 .append(request.getRequestURI())
                 .append(",contType==>")
                 .append(contType).append("\n");
        
        if(contType!=null
           &&contType.contains("application/json")) {
            logBuffer.append("body===>")
                     .append(getBody(request))
                     .append("\n");
        }else {
            logBuffer.append("QueryString===>")
                     .append(request.getQueryString())
                     .append("\n");
            
            logBuffer.append("parameterMap===>")
            .append(parseMap(request.getParameterMap()))
            .append("\n");
        }
        baseDao.insertRequestLog(logBuffer.toString(),userId,request.getRemoteAddr());
        return true;
    }
    
    public String parseMap(Map<String, String[]> map) {
        if(map==null)return "";
        String str = "";
        for(String key:map.keySet()) {
            str+=key+"="+parseArray(map.get(key));
        }
        return str;
    }
    
    public String parseArray(String str[]) {
        if(str==null) return ",";
        String ss = "";
        for(String s:str) {
            ss+=s+",";
        }
        return ss;
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
}
