package com.fh.shopapi.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shopapi.order.po.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface IOrderDetailMapper extends BaseMapper<OrderDetail> {
}
