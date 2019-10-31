package com.fh.shopapi.exception;

import com.fh.shopapi.common.ResponseEnum;
import com.fh.shopapi.common.ServerResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ServerResponse handleException(GlobalException globalException){
        ResponseEnum responseEnum = globalException.getResponseEnum();
        return ServerResponse.error(responseEnum);
    }
}
