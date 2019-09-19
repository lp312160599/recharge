package com.dayee.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class StringUtils {

	public static final String EMPTY = "";

    /**
	 * 拼接字符串
	 */
	public static String stitching(String character,Collection<String> list){
		
		Iterator<String> it = list.iterator();
		String str = "";
		while(it.hasNext()){
			String s = it.next();
			str+=s+character;
		}
		str = str.substring(0,str.lastIndexOf(character));
		return str;
	} 
	
    public static String stitching(String character,Object [] obj){
        
        String str = "";
        for(Object o :obj){
            String s = o.toString();
            str+=s+character;
        }
        str = str.substring(0,str.lastIndexOf(character));
        return str;
    } 
    
 // 这是组装参数
    public static <T> String assemblyParameter(Map<String, T> parameters) {

        StringBuilder para = new StringBuilder();
        for (String str : parameters.keySet()) {
            para.append(str).append("=").append(parameters.get(str)).append("&");
        }
        return para.substring(0, para.length() - 1);
    }
	
	/**
	 * 首字母大写
	 */
	public static String fistLetterToUpperCase(String str){
		return str.substring(0,1).toUpperCase()+str.substring(1);
	}
	
	public static boolean isEmpty(String str){
		if(str==null||str.isEmpty())return true;
		else return false;
	}
	
	public static boolean isNotEmpty(String str){
		return !isEmpty(str);
	}
}
