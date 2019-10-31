package com.fh.shopapi.order.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shopapi.order.po.Order;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface IOrderMapper extends BaseMapper<Order> {

}
