package com.fh.shopapi.brand.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shopapi.brand.param.BrandSearchParam;
import com.fh.shopapi.brand.po.Brand;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@Mapper
public interface IBrandMapper extends BaseMapper<Brand> {

    Long findTotalCount(BrandSearchParam brandSearchParam);

    List<Brand> findPageList(BrandSearchParam brandSearchParam);
}
