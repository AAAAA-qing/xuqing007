package com.fh.shopapi.brand.controller;


import com.fh.shopapi.brand.biz.IBrandService;
import com.fh.shopapi.brand.param.BrandSearchParam;
import com.fh.shopapi.brand.po.Brand;
import com.fh.shopapi.common.ServerResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
@RestController
@RequestMapping("brands")
@CrossOrigin("*")
public class BrnadApi {

    @Resource(name="brandService")
    private IBrandService brandService;

    //分页 条件查询
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ServerResponse findPageList(BrandSearchParam brandSearchParam) {
        return brandService.findPageList(brandSearchParam);
    }

    //新增
    @RequestMapping(method = RequestMethod.POST)
    public ServerResponse add(Brand brand) {
        brandService.add(brand);
        return ServerResponse.success();
    }

    //查询
    @RequestMapping(method = RequestMethod.GET)
    public ServerResponse findBrandAll() {
        return brandService.findBrandAll();
    }

    //删除
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ServerResponse delete(@PathVariable Long id) {
        return brandService.delete(id);
    }

    //回显
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ServerResponse find(@PathVariable Long id) {
        return brandService.find(id);
    }

    //修改
    @RequestMapping(method = RequestMethod.PUT)
    public ServerResponse update(@RequestBody Brand brand) {
        return brandService.update(brand);
    }

    //批量删除
    @RequestMapping(method = RequestMethod.DELETE)
    public ServerResponse batchDelete(String ids) {
        return brandService.batchDelete(ids);
    }


}
