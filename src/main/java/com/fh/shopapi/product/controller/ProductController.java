package com.fh.shopapi.product.controller;

import com.fh.shopapi.common.ServerResponse;
import com.fh.shopapi.product.biz.IProductService;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@Component
@RestController
@RequestMapping("/product")
public class ProductController {
    @Resource(name="productService")
    private IProductService productService;

    //@Scheduled(cron = "0 0 0 * * ?")  //每天晚上12点执行
    @Scheduled(cron = " */5 * * * * ?")// 五秒一次
    public void stockWarning(){
        //扫描库存 发送警告
        System.out.println("========================");
        productService.stockWarning();
    }

    @RequestMapping("findList")
    public Object findList(String callback){
        ServerResponse list = productService.findList();
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(list);
        mappingJacksonValue.setJsonpFunction(callback);
        return mappingJacksonValue;
    }

}
