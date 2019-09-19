package com.dayee.exception;


public interface ExceptionCode {

     /**无效的信用额度**/
     String  ERROR_10001="10001";
     /**无效的共享账号ID**/
     String  ERROR_10002="10002";
     /**无效的共享账号**/
     String  ERROR_10003="10003";
     /**信用额度不足,扣除失败**/
     String  ERROR_10004="10004";
     /**该流水已经存在,请不要重复提交!**/
     String  ERROR_10005="10005";
     /**系统异常**/
     String  ERROR_1="1";
     /**json解析异常**/
     String  ERROR_10006="10006";
     /**"签名验证失败!**/
     String  ERROR_10007="10007";
     /**企业不存在**/
     String  ERROR_10008="10008";
     /**fileSize 无效**/
     String  ERROR_10009="10009";
     /**企业不存在**/
     String  ERROR_10010="10010";
}
