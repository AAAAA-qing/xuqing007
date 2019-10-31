package com.fh.shopapi.order.biz;

import com.fh.shopapi.common.ServerResponse;
import com.fh.shopapi.order.param.OrderParam;

public interface IOrderService {
    ServerResponse addOrder(Long memberId, OrderParam orderParam);

}
