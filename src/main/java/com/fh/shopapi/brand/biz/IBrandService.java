package com.fh.shopapi.brand.biz;


import com.fh.shopapi.brand.param.BrandSearchParam;
import com.fh.shopapi.brand.po.Brand;
import com.fh.shopapi.common.ServerResponse;

public interface IBrandService {
    ServerResponse findSellWellBrand();

    void add(Brand brand);

    ServerResponse findBrandAll();

    ServerResponse delete(Long id);

    ServerResponse find(Long id);

    ServerResponse update(Brand brand);

    ServerResponse batchDelete(String ids);

    ServerResponse findPageList(BrandSearchParam brandSearchParam);
}
