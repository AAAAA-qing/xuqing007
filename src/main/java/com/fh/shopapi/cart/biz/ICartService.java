package com.fh.shopapi.cart.biz;

import com.fh.shopapi.common.ServerResponse;

public interface ICartService {
    ServerResponse addCart(Long memberId, Long productId, Long count);

    ServerResponse findCarts(Long id);

    ServerResponse deleteCartItem(Long memberId, Long productId);
}
