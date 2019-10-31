package com.fh.shopapi.address.biz;


import com.fh.shopapi.address.po.Address;
import com.fh.shopapi.common.ServerResponse;

public interface IAddressService {
    ServerResponse addAddress(Long memberId, Address address);

    ServerResponse findMemberAddress(Long memberId);

    ServerResponse deleteAddress(String id);

    ServerResponse findById(String id);

    ServerResponse updateAddress(Long id, Address address);
}
