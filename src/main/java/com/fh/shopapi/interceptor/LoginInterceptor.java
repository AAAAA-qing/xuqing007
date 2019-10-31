package com.fh.shopapi.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.fh.shopapi.annotation.Check;
import com.fh.shopapi.common.ResponseEnum;
import com.fh.shopapi.exception.GlobalException;
import com.fh.shopapi.member.vo.MemberVo;
import com.fh.shopapi.utils.*;

import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Base64;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,"http://localhost:8082");
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,"x-token,content-type,token");
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,"GET,POST,DELETE,PUT");

        //过滤options请求
        String methodType = request.getMethod();
        if(methodType.equalsIgnoreCase("options")){
            return false;
        }

        //获取头信息
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        Method method = handlerMethod.getMethod();
        //判断访问的是否是公共资源
        if (!method.isAnnotationPresent(Check.class)){
            return true;
        }

        //在登陆的情况下 这是有值的
        String header = request.getHeader("x-token");
        //判断是否有头信息
        if (header == null) {
            //头信息不存在
            throw new GlobalException(ResponseEnum.HEADER_INFO_IS_NULL);
        }
        //验签
        String[] split = header.split("\\.");
        if (split.length != 2) {
            //头信息丢失
            throw new GlobalException(ResponseEnum.HEADER_INFO_IS_MISS);
        }
        String memberInfoBase64 = split[0];
        String signBase64 = split[1];
        //验证头信息是否正确
        String sign = new String(Base64.getDecoder().decode(signBase64));
        String signMd5 = Md5Util.sign(memberInfoBase64, SystemConstant.SECRETKEY);
        if (!sign.equals(signMd5)) {
            //头信息被篡改
            throw new GlobalException(ResponseEnum.HEADER_INFO_IS_UPDATE);
        }
        //获取json对象 然后转为java对象
        String memberInfoJson = new String(Base64.getDecoder().decode(memberInfoBase64));
        MemberVo memberVo = JSONObject.parseObject(memberInfoJson, MemberVo.class);
        //判断redis中信息是否过期
        String memberName = memberVo.getMemberName();
        String uuid = memberVo.getUuid();
        boolean exists = RedisUtil.exists(KeyUtil.buildLoginMemberKey(memberName, uuid));
        if (!exists){
            //登录信息不存在 也就是过期
            throw new GlobalException(ResponseEnum.LOGIN_INFO_TIMEOUT);
        }
        //续命 重置过期时间
        RedisUtil.expire(KeyUtil.buildLoginMemberKey(memberName,uuid),SystemConst.MEMBER_REDIS_TIME_OUT);
        //将当前登录用户 放入request作用域
        request.setAttribute(SystemConst.LOGIN_MEMBER,memberVo);
        return true;


    }

}
