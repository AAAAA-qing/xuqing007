package com.fh.shopapi.sms.controller;

import com.fh.shopapi.common.ServerResponse;
import com.fh.shopapi.sms.biz.ISMSService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/sms")
@CrossOrigin("*")
public class SMSController {
    @Resource(name = "smsService")
    private ISMSService smsService;

    //发送验证码
    @GetMapping
    public ServerResponse postPhoneCode(String phone) {
        return smsService.postPhoneCode(phone);
    }

}
