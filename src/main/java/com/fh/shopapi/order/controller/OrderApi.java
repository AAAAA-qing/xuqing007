package com.fh.shopapi.order.controller;


import com.fh.shopapi.annotation.ApiIdempotent;
import com.fh.shopapi.annotation.Check;
import com.fh.shopapi.common.ServerResponse;
import com.fh.shopapi.member.vo.MemberVo;
import com.fh.shopapi.order.biz.IOrderService;
import com.fh.shopapi.order.param.OrderParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/orders")
public class OrderApi {
    @Resource(name = "orderService")
    private IOrderService orderService;


    //添加订单
    @ApiIdempotent
    @Check
    @PostMapping
    public ServerResponse addOrder(OrderParam orderParam, MemberVo memberVo) {
        Long memberId = memberVo.getId();
        return orderService.addOrder(memberId,orderParam);
    }

}
