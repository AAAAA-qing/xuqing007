package com.fh.shopapi.exception;

import com.fh.shopapi.common.ResponseEnum;

public class GlobalException extends RuntimeException {
    private ResponseEnum responseEnum;

    public GlobalException(ResponseEnum responseEnum) {
        this.responseEnum = responseEnum;
    }

    public ResponseEnum getResponseEnum() {
        return responseEnum;
    }

}
