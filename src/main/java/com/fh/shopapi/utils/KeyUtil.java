package com.fh.shopapi.utils;

public class KeyUtil {
    //构建redis中 验证码的key
    public static String buildSMSCodeKey(String data) {
        return "SMSCode:"+data;
    }

    //token 登陆 redis中存储的当前登录会员的key
    public static String buildLoginMemberKey(String memberName,String uuid) {
        return "member:"+memberName+":"+uuid;
    }

    //redis 购物车map中 filed
    public static String buildCartFiled(Long id) {
        return "memberCart:"+id;
    }

    //redis 支付日志信息
    public static String buildPayLogInfo(Long id) {
        return "pay:"+id;
    }


}
