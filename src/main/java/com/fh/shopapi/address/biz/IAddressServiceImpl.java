package com.fh.shopapi.address.biz;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fh.shopapi.address.mapper.IAddressMapper;
import com.fh.shopapi.address.po.Address;
import com.fh.shopapi.address.vo.AddressVo;
import com.fh.shopapi.area.mapper.IAreaMapper;
import com.fh.shopapi.area.po.Area;
import com.fh.shopapi.common.ResponseEnum;
import com.fh.shopapi.common.ServerResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("addressService")
@Transactional(rollbackFor = Exception.class)
public class IAddressServiceImpl implements IAddressService {
    @Autowired
    private IAddressMapper addressMapper;
    @Autowired
    private IAreaMapper areaMapper;

    //新增地址接口
    @Override
    public ServerResponse addAddress(Long memberId, Address address) {
        //非空验证
        if (StringUtils.isEmpty(address.getRealName()) || StringUtils.isEmpty(address.getConsigneePhone()) || StringUtils.isEmpty(address.getStreetFloor())
                || StringUtils.isEmpty(address.getArea1().toString()) || StringUtils.isEmpty(address.getArea2().toString())
                || StringUtils.isEmpty(address.getArea3().toString()) || StringUtils.isEmpty(address.getPostcode())){{
                return ServerResponse.error(ResponseEnum.ADDRESS_INFO_IS_NULL);
            }
        }
        //手机号 正则验证
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (address.getConsigneePhone().length() != 11) {
            //手机号应为11位数
            return ServerResponse.error(ResponseEnum.PHONE_IS_11);
        }
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(address.getConsigneePhone());
        boolean isMatch = m.matches();
        if (!isMatch) {
            //您的手机号是错误格式
            return ServerResponse.error(ResponseEnum.PHONE_STYLE_ERROR);
        }
        //设置地址信息
        address.setId(IdWorker.getTimeId());
        address.setMemberId(memberId);
        //新增地址
        addressMapper.insert(address);
        return ServerResponse.success();
    }

    //查询当前登录用户的
    @Transactional(readOnly = true)
    @Override
    public ServerResponse findMemberAddress(Long memberId) {
        //查询当前会员的收货地址信息
        List<Address> addresses = addressMapper.findMemberAddress(memberId);
        if (addresses.size() == 0) {
            return ServerResponse.error(ResponseEnum.MEMBER_ADDRESS_INFO_IS_NULL);
        }
        //po转vo
        List<AddressVo> addressVoList = new ArrayList<>();
        for (Address address : addresses) {
            AddressVo addressVo = new AddressVo();
            addressVo.setId(address.getId());
            addressVo.setRealName(address.getRealName());
            addressVo.setAreaName(address.getAreaName());
            addressVo.setStreetFloor(address.getStreetFloor());
            String consigneePhone = address.getConsigneePhone().substring(0,3)+"****"+address.getConsigneePhone().substring(8);
            addressVo.setConsigneePhone(consigneePhone);
            addressVo.setPostcode(address.getPostcode());
            addressVoList.add(addressVo);
        }
        return ServerResponse.success(addressVoList);
    }

    //删除地址
    @Override
    public ServerResponse deleteAddress(String id) {
        //非空验证
        if (StringUtils.isEmpty(id)) {
            return ServerResponse.error(ResponseEnum.DLELTE_ADDRESS_IS_NULL);
        }
        //删除地址
        addressMapper.deleteById(id);
        return ServerResponse.success();
    }

    //回显
    @Override
    public ServerResponse findById(String id) {
        Address address = addressMapper.selectById(id);
        //查询地区集合
        List<Address> addresses = addressMapper.selectList(null);
        //拼接地区信息
        Area area1 = areaMapper.selectById(address.getArea1());
        Area area2 = areaMapper.selectById(address.getArea2());
        Area area3 = areaMapper.selectById(address.getArea3());
        String areaName = area1.getCityName() + "-->" + area2.getCityName() + "-->"+ area3.getCityName();
        address.setAreaName(areaName);
        return ServerResponse.success(address);
    }

    @Override
    public ServerResponse updateAddress(Long id, Address address) {
        //非空验证
        if (StringUtils.isEmpty(address.getRealName()) || StringUtils.isEmpty(address.getConsigneePhone()) || StringUtils.isEmpty(address.getStreetFloor())
                || StringUtils.isEmpty(address.getArea1().toString()) || StringUtils.isEmpty(address.getArea2().toString())
                || StringUtils.isEmpty(address.getArea3().toString()) || StringUtils.isEmpty(address.getPostcode())){{
            return ServerResponse.error(ResponseEnum.ADDRESS_INFO_IS_NULL);
        }
        }
        //手机号 正则验证
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (address.getConsigneePhone().length() != 11) {
            //手机号应为11位数
            return ServerResponse.error(ResponseEnum.PHONE_IS_11);
        }
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(address.getConsigneePhone());
        boolean isMatch = m.matches();
        if (!isMatch) {
            //您的手机号是错误格式
            return ServerResponse.error(ResponseEnum.PHONE_STYLE_ERROR);
        }
        //设置地址信息
        address.setMemberId(id);
        //修改地址
        addressMapper.updateById(address);
        return ServerResponse.success();
    }


}
