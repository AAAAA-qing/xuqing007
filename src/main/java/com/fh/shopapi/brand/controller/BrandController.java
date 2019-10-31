package com.fh.shopapi.brand.controller;


import com.fh.shopapi.annotation.Check;
import com.fh.shopapi.brand.biz.IBrandService;
import com.fh.shopapi.common.ServerResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/brand")
@CrossOrigin("*")
public class BrandController {
    @Resource
    private IBrandService brandService;

    //查询热销品牌
    @RequestMapping("findSellWellBrand")
    @Check
    public ServerResponse findSellWellBrand(){
        ServerResponse list = brandService.findSellWellBrand();
        return list;
    }
}
