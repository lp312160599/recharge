package com.dayee.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleDateFormatNew extends SimpleDateFormat{

	private static final long serialVersionUID = 1L;

	@Override
	public Date parse(String text, ParsePosition pos) {
		
		text = text.replaceAll("[^\\d]+","");
		if(text.matches("[\\d]{8}")){
			applyPattern("yyyyMMdd");
		}else if(text.matches("[\\d]{14}")){
			applyPattern("yyyyMMddHHmmss");
		}else{
			pos.setIndex(1);
			return null;
		}
		return super.parse(text, pos);
	}

}
