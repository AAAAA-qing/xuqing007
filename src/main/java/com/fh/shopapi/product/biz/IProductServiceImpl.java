package com.fh.shopapi.product.biz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shopapi.common.ServerResponse;
import com.fh.shopapi.product.mapper.IProductMapper;
import com.fh.shopapi.product.po.Product;
import com.fh.shopapi.product.vo.ProductVo;
import com.fh.shopapi.utils.DateUtil;
import com.fh.shopapi.utils.RedisUtil;
import com.fh.shopapi.utils.SendEmail;
import com.fh.shopapi.utils.SystemConst;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("productService")
@Transactional(rollbackFor = Exception.class)
public class IProductServiceImpl implements IProductService {
    @Autowired
    private IProductMapper productMapper;


    @Override
    public ServerResponse findList() {
        String showProductListJson = RedisUtil.get("showProductList");
        if (StringUtils.isNotEmpty(showProductListJson)){
            //不为空 将json数据转换为对应的java对象
            List<ProductVo> productVoList = JSONObject.parseArray(showProductListJson, ProductVo.class);
            return ServerResponse.success(productVoList);
        }

        //redis中不存在
        QueryWrapper queryWrapper = new QueryWrapper<Product>();
        queryWrapper.orderByDesc("id");
        queryWrapper.eq("isValid",1);
        List<Product> productList = productMapper.selectList(queryWrapper);
        //po转vo
        List<ProductVo> productVoList = getProductVoList(productList);

        String showProductList = JSONObject.toJSONString(productList);
        RedisUtil.setEx("showProductList",showProductList, SystemConst.SHOW_PRODUCT_TIME);
        return ServerResponse.success(productVoList);
    }

    //扫描库存 发送警告
    @Override
    @Transactional(readOnly = true)
    public void stockWarning() {
        //查询所有上架的商品
        List<Product> validProductList = productMapper.selectValidProduct();
        for (Product product : validProductList) {
            if (product.getStock() <= 10){
                //库存小于10发送邮件
                String subJect = "库存警告";
                String content = "<h1>您店铺中价格为:"+product.getPrice()+"元的 "+product.getProductName()+" 商品库存不足,目前剩余:"+product.getStock()+"件,请您尽快上货</h1>";
                System.out.println(product.getProductName()+"库存不足");
                //SendEmail.sendEmail(SystemConst.receiveMailAccount, subJect, content);
            }
        }
    }


    private List<ProductVo> getProductVoList(List<Product> productList) {
        List<ProductVo> productVoList = new ArrayList<>();
        for (Product product : productList) {
            ProductVo productVo=new ProductVo();
            productVo.setId(product.getId());
            productVo.setProductName(product.getProductName());
            productVo.setPrice(product.getPrice().toString());
            productVo.setStock(product.getStock());
            productVo.setIsSellWell(product.getIsSellWell());
            productVo.setIsValid(product.getIsValid());
            productVo.setBrandId(product.getBrandId());
            productVo.setMainImage(product.getMainImage());
            productVo.setCreateTime(DateUtil.date2str(product.getCreateTime(),DateUtil.FUFF_YEAR));
            productVoList.add(productVo);
        }
        return productVoList;
    }
    @Transactional(readOnly = true)
    public ServerResponse selectProductOne (Long productId){
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",productId);
        Product product = productMapper.selectOne(queryWrapper);
        return ServerResponse.success(product);
    }
}
