package com.fh.shopapi.token.controller;

import com.fh.shopapi.annotation.Check;
import com.fh.shopapi.common.ServerResponse;
import com.fh.shopapi.token.biz.ITokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/tokens")
public class TokenController {
    @Resource
    private ITokenService tokenService;

    //创建token
    @Check
    @PostMapping
    public ServerResponse createToken() {
        return tokenService.createToken();
    }



}
