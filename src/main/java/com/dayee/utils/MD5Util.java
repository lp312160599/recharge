package com.dayee.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

	public static String toMd5(String values){
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md.update(values.getBytes());
		String txt = new BigInteger(1, md.digest()).toString(16);
		return txt;
	}
	
}