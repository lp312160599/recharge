package com.dayee.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DateConvert implements Converter<String, Date>{

	@Override
	public Date convert(String str) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
		str = str.replaceAll("[^\\d]+","");
		if(str.matches("[\\d]{4}[-][\\d]{2}[-][\\d]{2}")){
			simpleDateFormat.applyPattern("yyyyMMdd");
		}else if(str.matches("[\\d]{4}[-][\\d]{2}[-][\\d]{2}[\\d]{2}[-][\\d]{2}[-][\\d]{2}[-]")){
			simpleDateFormat.applyPattern("yyyyMMddHHmmss");
		}else{
			return null;
		}
		
        try {
            return simpleDateFormat.parse(str);
        } catch (ParseException e) {
            Logger.getLogger(DateConvert.class).error(e.getMessage(),e);
        }
        return null;
	}
}