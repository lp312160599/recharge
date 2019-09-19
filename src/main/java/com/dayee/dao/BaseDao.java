package com.dayee.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface BaseDao<T> {

	   void addObject(HashMap<String,Object> map);
	   
	   void updateObjectById(@Param("tableName")String tableName,
			                 @Param("values")String values,
			                 @Param("id")String id);
	   
	   void deleteObject(T t);
	   void deleteObjects(List<T> list);
	   void deleteObjectById(int id);
	   void deleteObjectByIds(@Param("tableName")String tableName,
			                  @Param("list")List<Integer> list);
	   Map<String,Object> queryById(@Param("tableName")String tableName,
			                        @Param("id")int id);
	   List<Map<String,Object>> queryByCondition(@Param("tableName")String tableName,
			                                     @Param("condition")String condition,
			    			                     @Param("orderBy")String orderBy);
	   List<Map<String,Object>> queryByConditionAndPagination(@Param("tableName")String tableName,
			                                                   @Param("condition")String condition,
			                                                   @Param("pageNumber")int pageNumber,
			                                                   @Param("pageSize")int pageSize,
			                  			                       @Param("orderBy")String orderBy);
	   
	   int queryByConditionCount(@Param("tableName")String tableName,
			                     @Param("condition")String condition,
			                     @Param("orderBy")String orderBy);
	   
	   void insertRequestLog(@Param("str")String text,
	                         @Param("userId")Integer userId,
	                         @Param("ip")String ip);
	   
}
