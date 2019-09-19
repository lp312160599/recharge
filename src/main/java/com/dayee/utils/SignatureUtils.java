package com.dayee.utils;

import java.net.URLEncoder;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Encoder;

public class SignatureUtils {

    private static String ACCESSKEY_SECRET = "resumeSecret";

    public  static <T> String getSignature(Map<String,T> params) throws Exception{
        StringBuilder builder = new StringBuilder();
        int size = 1;
        for (Map.Entry<String, T> entry : params.entrySet()) {

            //对每个参数和值进行encode，对字符进行转义
            String key = URLEncoder.encode(entry.getKey(), "UTF-8");
            String value = URLEncoder.encode(entry.getValue().toString(), "UTF-8");

            builder.append(key + "=" + value);
            if (size != params.size()) {
                builder.append("&");
            }
            size++;
        }
        String stringToSign = URLEncoder.encode(builder.toString(), "UTF-8");

        byte[] bytes =HmacSHA1Encrypt(stringToSign, ACCESSKEY_SECRET + "&");
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(bytes);
    }

    public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKey secretKey = new SecretKeySpec(encryptKey.getBytes(), "HmacSHA1");
        mac.init(secretKey);
        byte[] text = encryptText.getBytes();
        byte[] bytes = mac.doFinal(text);
        return bytes;
    }

}