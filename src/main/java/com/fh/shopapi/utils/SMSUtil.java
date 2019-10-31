package com.fh.shopapi.utils;


import java.util.*;

public class SMSUtil {

    //发送验证码的请求路径URL
    private static final String
    SERVER_URL="https://api.netease.im/sms/sendcode.action";
    //网易云信分配的账号，请替换你在管理后台应用下申请的Appkey
    private static final String
    APP_KEY="c1aa8dcaadcb896737f38a639aa1f951";
    //网易云信分配的密钥，请替换你在管理后台应用下申请的appSecret
    private static final String APP_SECRET="f0b3b04fb254";
    //随机数
    private static final String NONCE="123456";
    //验证码长度，范围4～10，默认为4
    private static final String CODELEN="6";


    public static String postPhoneSMS(String phone){
        Map<String, String> herders = new HashMap<String, String>();
        //String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String curTime = System.currentTimeMillis()+"";
        /*
         * 参考计算CheckSum的java代码，在上述文档的参数列表中，有CheckSum的计算文档示例
         */
        String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET, NONCE, curTime);
        // 设置请求的header
        herders.put("AppKey", APP_KEY);
        herders.put("Nonce", NONCE);
        herders.put("CurTime", curTime);
        herders.put("CheckSum", checkSum);
        herders.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        Map<String, String> params = new HashMap<>();
        params.put("mobile", phone);
        params.put("codeLen", CODELEN);
        String result = HttpClientUtil.sendPost(SERVER_URL, herders, params);
        return result;
    }



}
