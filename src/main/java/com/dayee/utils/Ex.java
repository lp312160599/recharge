package com.dayee.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Ex {

	public  static String gEx(Exception e){
		StringWriter stringWriter =  new StringWriter() ;
		e.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}
}
