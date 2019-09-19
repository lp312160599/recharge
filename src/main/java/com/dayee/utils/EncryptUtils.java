package com.dayee.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.Key;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Hex;

public class EncryptUtils {

	static Key KEY;
	private static final String DES = "DES";
	
	static {
        ObjectInputStream os = null;
        try {
            os = new ObjectInputStream(EncryptUtils.class
                    .getResource("des.key").openStream());
            KEY = (Key) os.readObject();

        } catch (Exception e) {
        } finally {
            try {
				os.close();
			} catch (IOException e) {
			}
        }
    }
	
	  public static String encode( String str) {

	        byte[] byteFina = null;
	        Cipher cipher;
	        try {
	            cipher = Cipher.getInstance(DES);
	            cipher.init(Cipher.ENCRYPT_MODE, KEY);
	            byteFina = cipher.doFinal(str.getBytes());
	            char[] c = Hex.encodeHex(byteFina);
	            return new String(c);
	        } catch (Exception e) {
	        } finally {
	            cipher = null;
	        }
         return "";
	    }
	  
	  public static String decode( String str) {

	        Cipher cipher;
	        byte[] byteFina = null;
	        try {
	            cipher = Cipher.getInstance(DES);
	            cipher.init(Cipher.DECRYPT_MODE, KEY);
	            byte[] b = Hex.decodeHex(str.toCharArray());
	            byteFina = cipher.doFinal(b);
	            return new String(byteFina);
	        } catch (Exception e) {
	        } finally {
	            cipher = null;
	        }
	        return "";
	    }
}
