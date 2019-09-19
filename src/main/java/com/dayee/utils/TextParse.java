package com.dayee.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 根据正则从text中提取响应的数据
 * @author 李鹏
 * @version 1.0
 *
 */

public class TextParse {
    
	/**
	 * 根据正则查找文本中的内容
	 * @param text
	 * 文本内容
	 * @param regex
	 * 正则表达式
	 * @param number
	 * 最多找到几个文本
	 * @return
	 * 返回找到的所有文本集合
	 */
	public static List<String> prase(String text,String regex,int number){
		
		Pattern patten = Pattern.compile(regex);
		Matcher mat = patten.matcher(text);
		List<String> list = new ArrayList<String>();
		while(mat.find()) {
	   if(number==-1){
		   list.add(mat.group());
		   continue;
	   }
		if(number>0){
			list.add(mat.group());
			number--;
     	  }else{
     		  break;
     	  }
		}
		return list;
	}	
	
	/**
	 * 根据正则查找文本中的内容
	 * @param text
	 * 文本内容
	 * @param regex
	 * 正则表达式
	 * @return
	 * 返回找到的所有文本集合
	 */
	public static List<String> prase(String text,String regex){
		return prase(text, regex, -1);
	}
}