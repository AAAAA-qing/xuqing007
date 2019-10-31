package com.fh.shopapi.address.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shopapi.address.po.Address;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface IAddressMapper extends BaseMapper<Address> {
    List<Address> findMemberAddress(Long memberId);

    Address findAddressById(String addressId);
}
