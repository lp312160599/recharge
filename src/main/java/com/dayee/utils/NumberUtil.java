package com.dayee.utils;

import java.text.DecimalFormat;

public class NumberUtil {

	
	private static DecimalFormat format = new DecimalFormat("####0.0#");
	
	/**相乘*/
	public static double division(double one,double two){
		double result = one/two;
		return Double.valueOf(format.format(result));
	}
	/**相除*/
	public static double multiply(double one,double two){
		double result = one*two;
		return Double.valueOf(format.format(result));
	}
	/**相除*/
	public static int multiplyToInt(double one,double two){
		double result = one*two;
		return Double.valueOf(format.format(result)).intValue();  
	}
	/**相加*/
	public static double addTogether(double one,double two){
        double result = one+two;
        return Double.valueOf(format.format(result)).intValue();  
    }
	/**相减*/
    public static double subtraction(double one,double two){
        double result = one-two;
        return Double.valueOf(format.format(result)).intValue();  
    }
	
}
