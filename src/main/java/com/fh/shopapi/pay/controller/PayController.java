package com.fh.shopapi.pay.controller;

import com.fh.shopapi.annotation.Check;
import com.fh.shopapi.common.ServerResponse;
import com.fh.shopapi.member.vo.MemberVo;
import com.fh.shopapi.pay.biz.IPayService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/pays")
public class PayController {
    @Resource
    private IPayService payService;

    //生成二维码
    @PostMapping
    @Check
    public ServerResponse createNative(MemberVo memberVo) {
        return payService.createNative(memberVo.getId());
    }

    //查询支付状态
    @GetMapping
    @Check
    public ServerResponse confirmPayStatus(MemberVo memberVo) {
        return payService.confirmPayStatus(memberVo.getId());
    }
}
