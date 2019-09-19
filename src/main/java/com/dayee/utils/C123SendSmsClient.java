package com.dayee.utils;



import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class C123SendSmsClient {

    private static final Logger logger          = LoggerFactory.getLogger(C123SendSmsClient.class);

    private static final String sendOnce        = "sendOnce";

    private static final String action          = "action";

    // 企业账号
    private static final String ac              = "ac";

    // 密钥
    private static final String authkey         = "authkey";

    // 通道组
    private static final String cgid            = "cgid";

    // private static final String pcsid = "csid";

    // 内容
    private static final String c               = "c";

    // 号码
    private static final String m               = "m";

    // 1 操作成功
    // 0 帐户格式不正确(正确的格式为:员工编号@企业编号)
    // -1 服务器拒绝(速度过快、限时或绑定IP不对等)如遇速度过快可延时再发
    // -2 密钥不正确
    // -3 密钥已锁定
    // -4 参数不正确(内容和号码不能为空，手机号码数过多，发送时间错误等)
    // -5 无此帐户
    // -6 帐户已锁定或已过期
    // -7 帐户未开启接口发送
    // -8 不可使用该通道组
    // -9 帐户余额不足
    // -10 内部错误
    // -11 扣费失败

   

    public static final String  LABEL_YUAN      = "LABEL_YUAN";
    
    
    public static final String  c123Url      = "http://smsapi.c123.cn/OpenPlatform/OpenApi";
    
    public static final String  c123Account      = "1001@500774980084";
    
    public static final String  interfaceKey      = "37EE3ECFAB4E4537061CF961F26E9057";
    
    public static final String  channelGroup      = "6410";
    


   

    public static void send(String phone,String content) {

        try {
            if (logger.isDebugEnabled()) {
                logger.debug("通过c123发送短信开始,{}", phone);
            }
           
            HashMap<String, String> params =  new HashMap<String, String>();
            params.put(action,sendOnce);
            params.put(ac,c123Account);
            params.put(authkey,interfaceKey);
            params.put(c,content);
            params.put(m,phone);
            params.put(cgid,channelGroup);
            String xml = SendRequest.sendPost(c123Url, new HashMap<String, String>() , params, "UTF-8").getBody();
            System.out.println(xml);
            logger.info("发送短信结果==>"+xml);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    
    public static void main(String[] args) throws Exception {

        C123SendSmsClient.send("13389230227","测试2");
    }
}
