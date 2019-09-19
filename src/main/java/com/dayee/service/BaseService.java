package com.dayee.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.dayee.annotation.NoColumn;
import com.dayee.dao.BaseDao;
import com.dayee.utils.DateUtil;
import com.dayee.utils.QueryUtil;
import com.dayee.utils.StringUtils;
import com.dayee.utils.TextParse;
import com.dayee.vo.Orders;
import com.dayee.vo.Page;

public class BaseService<T> {

	@Resource
	private BaseDao<T> baseDao;
	
	protected Logger log = Logger.getLogger(getClass());
	
	protected Class<Object> clazz;
	
	public <T1> void addObject(T1 t) throws Exception{
		
		String name = t.getClass().getSimpleName();
		
		HashMap<String, Object>  map = getColumnValues(t);
		List<String> column = new ArrayList<String>();
		String values = "";
		for(String c:map.keySet()){
			if("id".equals(c))continue;
			Object value = map.get(c);
			if(value==null)continue;
			else if(value instanceof String) values+="'"+value+"',";
			else if(value instanceof Date) values+="'"+DateUtil.dateToString((Date)value)+"',";
			else values+=value.toString()+",";
			column.add(c);
		}
		values = values.substring(0,values.length()-1);
		
		HashMap<String, Object>  idMap = new HashMap<String, Object>();
		idMap.put("tableName", getTableName(name));
		idMap.put("column", StringUtils.stitching(",", column));
		idMap.put("values", values);
		baseDao.addObject(idMap);
		setValue("id",idMap.get("id"), t);
	}

	public <T1> void updateObjectById(T1 t) throws Exception{
		
		String name = t.getClass().getSimpleName();
		HashMap<String, Object>  map = getColumnValues(t);
		String values = "";
		for(String c:map.keySet()){
			if("id".equals(c))continue;
			Object value = map.get(c);
			if(value==null) continue;
			if(value instanceof String) values+=c+"='"+value+"',";
			else if(value instanceof Date) values+=c+"='"+DateUtil.dateToString((Date)value)+"',";
			else values+=c+"="+value.toString()+",";
		}
		values = values.substring(0,values.length()-1);
		baseDao.updateObjectById(getTableName(name),values,map.get("id").toString());
	}

	public void deleteObject(T t) throws Exception{
		deleteObject(t,getClz());
	}
	
	public <T1> void deleteObject(T1 t,Class<T1> claz) throws Exception{
		List<T1> list = new ArrayList<T1>();
		list.add(t);
		deleteObjects(list,claz);
	}

	public void deleteObjects(List<T> list) throws Exception{
		deleteObjects(list,getClz());
	}
	
	public <T1> void deleteObjects(List<T1> list,Class<T1> claz) throws Exception{
		List<Integer> ids = new ArrayList<Integer>();
		for(T1 t : list){
			HashMap<String, Object>  map = getColumnValues(t);
			int id = (Integer) map.get("id");
			ids.add(id);
		}
		deleteObjectByIds(ids,claz);
	}

	public void deleteObjectById(int id){
		deleteObjectById(id,getClz());
	}
	
	public <T1> void deleteObjectById(int id,Class<T1> claz){
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(id);
		deleteObjectByIds(ids,claz);
	}

	public void deleteObjectByIds(List<Integer> list){
		deleteObjectByIds(list, getClz());
	}
	
	public <T1> void deleteObjectByIds(List<Integer> list,Class<T1> claz){
		String name = claz.getSimpleName();
		baseDao.deleteObjectByIds(getTableName(name),list);
	}

	public T queryById(int id) throws Exception{
		return queryById(id, getClz());
	}
	
	public <T1> T1 queryById(int id,Class<T1> claz) throws Exception{
		String name = claz.getSimpleName();
		Map<String, Object> mp = baseDao.queryById(getTableName(name),id);
		if(mp==null)return null;
		T1 t = getObject(mp,claz);
		return t;
	}

	public List<T> query(Object parameter,Page page) throws Exception{
		return query(parameter, page,null);
	}
	
	public List<T> query(Object parameter,Page page,Orders orders) throws Exception{
		if(page==null) page = new Page();
		return query(parameter, page, getClz(),orders);
	}
	
	@SuppressWarnings("unchecked")
	public <T1> List<T1> query(Object parameter,Page page,Class<T1> clazz,Orders orders) throws Exception{
		String condition="";
		if(parameter!=null){
			if(parameter instanceof QueryUtil){
				parameter = ((QueryUtil)parameter).get();
			}
			if(parameter instanceof String){
				condition = (String) parameter;
			}else{
				HashMap<String, Object> map = null;
				if(parameter instanceof HashMap){
					map = (HashMap<String, Object>) parameter;
				}else{
					map = getColumnValues(parameter);
				}
				for(String c:map.keySet()){
					Object value = map.get(c);
					if(value==null) continue;
					else if(value instanceof String) condition+=c+"='"+value+"' AND ";
					else if(value instanceof Date) condition+=c+"='"+DateUtil.dateToString((Date)value)+"' AND ";
					else condition+=c+"="+value.toString()+" AND ";
				}
				if(StringUtils.isNotEmpty(condition))condition = condition.substring(0,condition.lastIndexOf("AND"));
			}
		}
		if(StringUtils.isEmpty(condition)) condition =" 1=1 ";
		List<T1> resultList = new ArrayList<T1>();
		List<Map<String, Object>>  list = null;
		
		String orderBy = getOrderBy(orders);
		
		if(page==null){
			list = baseDao.queryByCondition(getTableName(clazz), condition,orderBy);
		}else{
			int pageNumber = page.getPageNumber();
			int pageSize   = page.getPageSize();
			list = baseDao.queryByConditionAndPagination(getTableName(clazz), condition, (pageNumber-1)*pageSize,pageSize,orderBy);
			int count = baseDao.queryByConditionCount(getTableName(clazz), condition,orderBy);
			page.setCount(count);
		}
		
		for(Map<String, Object> m :list){
			T1 t = getObject(m,clazz);
			resultList.add(t);
		}
		return resultList;
	}
	
	public String getOrderBy(Orders order) throws Exception{
		String orderBy = null;
		if(order!=null){
			orderBy = "";
			HashMap<String, Object> orders = getColumnValues(order);
			for(String col:orders.keySet()){
				Object o  = orders.get(col);
				if(o!=null&&StringUtils.isNotEmpty(o.toString())){
					String temp[] = o.toString().split("_");
					String c   = temp[0];
					String by  = temp[1];
					orderBy +=c+" "+by+",";
				}
			}
			if(!orderBy.isEmpty()){
				orderBy = orderBy.substring(0,orderBy.length()-1);
			}else orderBy = null;
		}
	  return orderBy;
	}
	
	
	public List<T> query() throws Exception{
		return query(getClz());
	}
	
	public <T1> List<T1> query(Class<T1> clazz) throws Exception{
		return query(clazz,(Orders)null);
	}
	
	public <T1> List<T1> query(Class<T1> clazz,Orders orders) throws Exception{
		return query(null, null,clazz,orders);
	}
	
	public List<T> query(Object parameter) throws Exception{
		return query(parameter, getClz());
	}
	
	public T queryOne(Object parameter) throws Exception{
		
		return queryOne(parameter,getClz());
	}
	
	public <T1> T1 queryOne(Object parameter,Class<T1> clazz) throws Exception{
        List<T1> list = query(parameter, clazz);
        if(list.size()!=0){
            return list.get(0);
        }
        return null;
    }
	
	public <T1> List<T1> query(Object parameter,Class<T1> clazz) throws Exception{
		return query(parameter,clazz,null);
	}
	
	public <T1> List<T1> query(Object parameter,Class<T1> clazz,Orders orders) throws Exception{
		return query(parameter, null,clazz,orders);
	}
	
	@SuppressWarnings("unchecked")
	public Class<T> getClz(){  
		return    (Class<T>)(((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);  
	}  
	
	protected  T getObject(Map<String, Object> mp) throws Exception{
		return getObject(mp,getClz());
	}
	
	protected <T1> T1 getObject(Map<String, Object> mp,Class<T1> clazz) throws Exception{
		T1 t =null;
			t = clazz.newInstance();
			for(String column:mp.keySet()){
				setValue(column,  mp.get(column), t);
			}
		return t;
	}
	
	protected <T1> void setValue(String column,Object value,T1 t) throws Exception{
		try{
			Field field = t.getClass().getDeclaredField(column);
			if(field!=null){
				Method  method  = t.getClass().getMethod("set"+StringUtils.fistLetterToUpperCase(column),field.getType());
				method.invoke(t,value);
			}
		}catch (NoSuchFieldException e) {
			return ;
		}
	}
	
	protected HashMap<String,Object>  getColumnValues(Object t) throws Exception{
		
        Field  fields[] = t.getClass().getDeclaredFields();
        HashMap<String,Object> maps = new HashMap<String, Object>();
		for(Field field:fields){
			if(Modifier.isStatic(field.getType().getModifiers()))continue;
			String colunmName = field.getName();
			NoColumn noColumn = field.getAnnotation(NoColumn.class);
			if(noColumn!=null)continue;
			if("serialVersionUID".equals(colunmName))continue;
			if(Modifier.isStatic(field.getModifiers()))continue;
			String methodName = field.getName().substring(0,1).toUpperCase()+field.getName().substring(1);
			Method getMethod;
			getMethod = t.getClass().getMethod("get"+methodName, new Class[0]);
			Object value = getMethod.invoke(t, new Object[0]);
			maps.put(colunmName, value);
		}
		return maps;
	}
	
	@SuppressWarnings("rawtypes")
	protected String getTableName(Class claz){
		return getTableName(claz.getSimpleName());	
	}
	
	protected String getTableName(String name){
		List<String> list = TextParse.prase(name, "([A-Z][a-z]+)");
		String tableName = "t_";
		tableName = tableName+StringUtils.stitching("_", list);
		return tableName;
	}
}
