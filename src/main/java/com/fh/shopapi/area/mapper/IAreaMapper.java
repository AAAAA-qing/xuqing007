package com.fh.shopapi.area.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shopapi.area.po.Area;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface IAreaMapper extends BaseMapper<Area> {

}
