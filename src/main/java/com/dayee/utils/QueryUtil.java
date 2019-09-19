package com.dayee.utils;

import java.util.HashMap;

public class QueryUtil {

	private HashMap<String,Object> map = new HashMap<String,Object>();
	
	private StringBuffer sqlBuffer     = new StringBuffer();
	
	private final String AND = " AND ";
	
	public static QueryUtil createQuery(){
		return new QueryUtil();
	}

	public QueryUtil gt(String column,Object value){
		sqlBuffer.append(column).append(" > ").append(value);
		sqlBuffer.append(AND);
		return this;
	}
	
	public QueryUtil ge(String column,Object value){
		sqlBuffer.append(column).append(" >= ").append(value);
		sqlBuffer.append(AND);
		return this;
	}
	
	public QueryUtil lt(String column,Object value){
		sqlBuffer.append(column).append(" < ").append(value);
		sqlBuffer.append(AND);
		return this;
	}
	
	public QueryUtil le(String column,Object value){
		sqlBuffer.append(column).append(" <= ").append(value);
		sqlBuffer.append(AND);
		return this;
	}
	
	public QueryUtil eq(String column,Object value){
		if(value==null||StringUtils.isEmpty(value.toString())) return this;
		if(isBaseDataType(value)){
			sqlBuffer.append(column).append(" = ").append(value);
		}else {
			sqlBuffer.append(column).append(" = '").append(value).append("'");
		}
		sqlBuffer.append(AND);
		return this;
	}
	
	public QueryUtil noEq(String column,Object value){
		if(value==null||StringUtils.isEmpty(value.toString())) return this;
		if(isBaseDataType(value)){
			sqlBuffer.append(column).append(" != ").append(value);
		}else {
			sqlBuffer.append(column).append(" != '").append(value).append("'");
		}
		sqlBuffer.append(AND);
		return this;
	}
	
	public <T> QueryUtil in(String column,T[] value){
		if(value==null||value.length==0) return this;
		
		String col = "";
		for (int i = 0; i < value.length; i++) {
			if(isBaseDataType(value[i])){
				col+=value[i]+",";
			}else {
				col+="'"+value[i]+"',";
			}
		}
		col = col.substring(0,col.length()-1);
		sqlBuffer.append(column).append(" in (");
		sqlBuffer.append(col);
		sqlBuffer.append(" ) ");
		sqlBuffer.append(AND);
		return this;
	}
	
	public <T> QueryUtil notIn(String column,T[] value){
		if(value==null||value.length==0) return this;
		
		String col = "";
		for (int i = 0; i < value.length; i++) {
			if(isBaseDataType(value[i])){
				col+=value[i]+",";
			}else {
				col+="'"+value[i]+"',";
			}
		}
		col = col.substring(0,col.length()-1);
		sqlBuffer.append(column).append(" not in (");
		sqlBuffer.append(col);
		sqlBuffer.append(" ) ");
		sqlBuffer.append(AND);
		return this;
	}
	
	public QueryUtil like(String column,Object value){
		if(value==null||StringUtils.isEmpty(value.toString())) return this;
		if(isBaseDataType(value)){
			sqlBuffer.append(column).append(" like %").append(value).append("%");
		}else {
			sqlBuffer.append(column).append(" like '%").append(value).append("%'");
		}
		sqlBuffer.append(AND);
		return this;
	}
	
	public QueryUtil betweenDate(String column,String startTime,String endTime){
		if(StringUtils.isNotEmpty(startTime)){
			sqlBuffer.append(column).append(" >= '")
			.append(startTime)
			.append("'");
			sqlBuffer.append(AND);
		}
		
		if(StringUtils.isNotEmpty(endTime)){
			sqlBuffer.append(column).append(" <= '")
			.append(endTime)
			.append(" 23:59:59")
			.append("'");
			sqlBuffer.append(AND);
		}
		return this;
	}
	
	public QueryUtil nowDate(String column,String startTime){
		
		if(StringUtils.isNotEmpty(startTime)){
			betweenDate(column, startTime, startTime+" 23:59:59");
		}
		return this;
	}
	
	 private static boolean isBaseDataType(Object o) 
	 {   
		 return 
	     (   
	    	  o.getClass().equals(Integer.class)||   
	    	  o.getClass().equals(Byte.class) ||   
	    	  o.getClass().equals(Long.class) ||   
	    	  o.getClass().equals(Double.class) ||   
	    	  o.getClass().equals(Float.class) ||   
	    	  o.getClass().equals(Character.class) ||   
	    	  o.getClass().equals(Short.class) ||   
	    	  o.getClass().equals(Boolean.class) ||   
	    	  o.getClass().isPrimitive()   
	     );   
	 }
	
	public QueryUtil addColumn(String column,Object value){
		if(value!=null){
			map.put(column, value);
		}
		return this;
	}
	
	public QueryUtil likeColumn(String column,Object value){
		if(value!=null){
			map.put(column, "%"+value+"%");
		}
		return this;
	}
	
	public Object get(){
		if(map.size()>0) return map;
		else if(sqlBuffer.length()>0){
			return sqlBuffer.substring(0, sqlBuffer.lastIndexOf(AND));
		}
		return null;
	}
}
