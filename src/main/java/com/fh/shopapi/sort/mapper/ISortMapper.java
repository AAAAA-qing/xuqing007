package com.fh.shopapi.sort.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shopapi.sort.po.Sort;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface ISortMapper extends BaseMapper<Sort> {
}
