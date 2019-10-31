package com.fh.shopapi.pay.biz;

import com.fh.shopapi.common.ServerResponse;

public interface IPayService {
    ServerResponse createNative(Long memberId);

    ServerResponse confirmPayStatus(Long memberId);
}
