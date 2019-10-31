package com.fh.shopapi.payLog.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shopapi.payLog.po.PayLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface IPayLogMapper extends BaseMapper<PayLog> {

}
