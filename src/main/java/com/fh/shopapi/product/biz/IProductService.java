package com.fh.shopapi.product.biz;

import com.fh.shopapi.common.ServerResponse;

public interface IProductService {
    ServerResponse findList();

    void stockWarning();
}
