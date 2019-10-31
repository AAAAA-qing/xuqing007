package com.fh.shopapi.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shopapi.product.po.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@Mapper
public interface IProductMapper extends BaseMapper<Product> {

    Long updateStock(@Param("productId") Long productId, @Param("count") Long count);

    List<Product> selectValidProduct();
}
