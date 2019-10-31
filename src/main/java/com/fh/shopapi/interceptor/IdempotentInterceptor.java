package com.fh.shopapi.interceptor;

import com.fh.shopapi.annotation.ApiIdempotent;
import com.fh.shopapi.common.ResponseEnum;
import com.fh.shopapi.exception.GlobalException;
import com.fh.shopapi.utils.RedisUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class IdempotentInterceptor extends HandlerInterceptorAdapter {

    //幂等性拦截器
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        //通过方法签名获取method
        Method method = handlerMethod.getMethod();
        //判断该方法上是否加上了幂等性
        if (!method.isAnnotationPresent(ApiIdempotent.class)){
            return true;
        }
        //判断header是否有token
        String token = request.getHeader("token");
        if (token == null) {
            //token不存在
            throw new GlobalException(ResponseEnum.HEADER_TOKEN_IS_NULL);
        }
        //验证redis中是否存在token
        boolean exists = RedisUtil.exists(token);
        if (!exists){
            throw new GlobalException(ResponseEnum.HEADER_TOKEN_IS_NULL);
        }
        //判断redis中是否存在该token
        Long del = RedisUtil.del(token);
        if (del == 0) {
            //redis中不存在  重复提交
            throw new GlobalException(ResponseEnum.METHOD_REPET_SUBMIT);
        }


        return true;
    }
}
