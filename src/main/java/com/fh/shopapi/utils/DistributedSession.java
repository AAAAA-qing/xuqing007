package com.fh.shopapi.utils;


import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class DistributedSession {

    public static String getSessionId(HttpServletRequest request, HttpServletResponse response) {
        //从请求中读取名字为sessionId的cookie
        String sessionId = CookieUtil.readCookie(SystemConst.NAME, request);
        if (StringUtils.isEmpty(sessionId)) {
            //生成sessiId
            sessionId = UUID.randomUUID().toString();
            //写入到客户端浏览器
            CookieUtil.writeCookie(SystemConst.NAME, sessionId, SystemConst.DOMAIN, response);
        }
        return sessionId;
    }
}
