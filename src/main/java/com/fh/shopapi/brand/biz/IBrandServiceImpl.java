package com.fh.shopapi.brand.biz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shopapi.brand.mapper.IBrandMapper;
import com.fh.shopapi.brand.param.BrandSearchParam;
import com.fh.shopapi.brand.po.Brand;
import com.fh.shopapi.brand.vo.BrandVo;
import com.fh.shopapi.common.DataTableResult;
import com.fh.shopapi.common.ResponseEnum;
import com.fh.shopapi.common.ServerResponse;
import com.fh.shopapi.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("brandService")

public class IBrandServiceImpl implements IBrandService {
    @Autowired
    private IBrandMapper brandMapper;

    @Override
    public ServerResponse findSellWellBrand() {
        String brandListJson = RedisUtil.get("brandList");
        if (StringUtils.isNotEmpty(brandListJson)) {
            //不为空 遍历出热销的返回
            List<Brand> brandList = JSONObject.parseArray(brandListJson, Brand.class);
            List<Brand> sellWellBrandList = getSellWellBrandList(brandList);
            return ServerResponse.success(sellWellBrandList);
        }

        //为空时  redis中不存在
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("sort");
        List<Brand> selectList = brandMapper.selectList(queryWrapper);

        //存储到redis
        brandListJson = JSONObject.toJSONString(selectList);
        RedisUtil.set("brandList", brandListJson);
        List<Brand> sellWellBrandList = getSellWellBrandList(selectList);
        return ServerResponse.success(sellWellBrandList);
    }

    @Override
    public void add(Brand brand) {
        brandMapper.insert(brand);
    }

    @Override
    public ServerResponse findBrandAll() {
        List<Brand> brands = brandMapper.selectList(null);
        return ServerResponse.success(brands);
    }

    @Override
    public ServerResponse delete(Long id) {
        brandMapper.deleteById(id);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse find(Long id) {
        Brand brand = brandMapper.selectById(id);
        if (brand == null) {
            return ServerResponse.error(ResponseEnum.BRAND_IS_NULL);
        }
        return ServerResponse.success(brand);
    }

    @Override
    public ServerResponse update(Brand brand) {
        brandMapper.updateById(brand);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse batchDelete(String ids) {
        if (StringUtils.isEmpty(ids)) {
            return ServerResponse.error(ResponseEnum.BRAND_IDS_IS_NULL);
        }
        String[] idArr = ids.split(",");
        List<Long> idList = new ArrayList<>();
        for (int i = 0; i < idArr.length; i++) {
            idList.add(Long.parseLong(idArr[i]));
        }
        brandMapper.deleteBatchIds(idList);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse findPageList(BrandSearchParam brandSearchParam) {
        //查询总条数
        Long totalCount = brandMapper.findTotalCount(brandSearchParam);
        //查询分页数据
        List<Brand> dataList = brandMapper.findPageList(brandSearchParam);
        //po转vo
        List<BrandVo> brandVoList = getBrandVoList(dataList);
        DataTableResult dataTableResult = new DataTableResult(brandSearchParam.getDraw(),totalCount,totalCount,brandVoList);
        return ServerResponse.success(dataTableResult);
    }

    private List<BrandVo> getBrandVoList(List<Brand> dataList) {
        List<BrandVo> brandVoList = new ArrayList<>();
        for (Brand brand : dataList) {
            BrandVo brandVo = new BrandVo();
            brandVo.setId(brand.getId());
            brandVo.setBrandName(brand.getBrandName());
            brandVo.setIsSellWell(brand.getIsSellWell());
            brandVo.setSort(brand.getSort());
            brandVoList.add(brandVo);
        }
        return brandVoList;
    }

    private List<Brand> getSellWellBrandList(List<Brand> selectList) {
        List<Brand> sellWellBrandList = new ArrayList<>();
        for (Brand brand : selectList) {
            if (brand.getIsSellWell() == 1) {
                sellWellBrandList.add(brand);
            }
        }
        return sellWellBrandList;
    }

}
