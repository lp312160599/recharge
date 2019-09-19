package com.dayee.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static final String YYYY_MM = "yyyy-MM";
    
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	
	public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	
	public static String dateToString(Date date,String pattern){
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}
	
	public static String dateToStringByNow(String pattern){
		return dateToString(new Date(), pattern);
	}
	
	public static String dateToString(Date date){
		SimpleDateFormat dateFormat = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
		return dateFormat.format(date);
	}
	
	public static Date stringToDate(String date,String pattern) throws ParseException{
	        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
	        return dateFormat.parse(date);
	}
	
	public static Date afterYear(Date date,int year){
		   Calendar c =    Calendar.getInstance();
		   c.setTime(date);
		   c.add(Calendar.YEAR,year);
		   return c.getTime();
	}
	
	public static Date afterHour(Date date,int hour){
        Calendar c =    Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR,hour);
        return c.getTime();
    }
	
	public static long addNowTimestampByMinute(int minute) {
        return new Date().getTime()+(minute*60*1000);
    }
	
	public static long addTimestampByMinute(Date date,int minute) {
	    return date.getTime()+(minute*60*1000);
	}
	
	public static int nowTimestampDifferenceBySecond(long timestamp) {
        return nowTimestampDifferenceBySecond(new Date().getTime(), timestamp);
    }
	
	public static int nowTimestampDifferenceBySecond(long oneTimestamp,long twoTimestamp) {
        return (int) (oneTimestamp-twoTimestamp)/1000;
    }
	
}
