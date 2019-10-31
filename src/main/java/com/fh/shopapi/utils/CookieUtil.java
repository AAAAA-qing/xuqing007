package com.fh.shopapi.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    //读cookie
    public static String readCookie(String name, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return "";
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }
        return "";
    }

    //写cookie
    public static void writeCookie(String name, String value,String doMain, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(doMain);//域名
        cookie.setPath("/");//代表网站的根目录
        //写入到客户端
        response.addCookie(cookie);
    }

}
