package com.dayee.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.log4j.Logger;

public class ObjectUtil {

    public static <T> T merge(Object from,T to){
        return merge(from.getClass(), from, to);
    }
    
	public static <T> T merge(Class<?> clazz,Object from,T to){
		
		Field[] fields = clazz.getDeclaredFields();
		
		for (int i = 0; i < fields.length; i++) {
			String name =  fields[i].getName();
			name = StringUtils.fistLetterToUpperCase(name);
			String setmethodName  = "set"+name;
			String getmethodName  = "get"+name;
			if(Modifier.isStatic(fields[i].getModifiers()))continue;
			try {
				Method  getmethod  = clazz.getMethod(getmethodName,new Class[]{});
				Object value = getmethod.invoke(from,new Object[]{});
				if(value!=null){
					Method  method  = to.getClass().getMethod(setmethodName,fields[i].getType());
					method.invoke(to,getmethod.invoke(from,new Object[]{}));
				}
			} catch (Exception e) {
				Logger.getLogger(ObjectUtil.class).error(e.getMessage(),e);
				continue;
			}
		}
		return to;
	}
}
