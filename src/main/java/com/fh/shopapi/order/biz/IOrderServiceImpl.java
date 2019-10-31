package com.fh.shopapi.order.biz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fh.shopapi.address.mapper.IAddressMapper;
import com.fh.shopapi.address.po.Address;


import com.fh.shopapi.cart.po.CartItemVo;
import com.fh.shopapi.cart.po.CartVo;
import com.fh.shopapi.common.ResponseEnum;
import com.fh.shopapi.common.ServerResponse;
import com.fh.shopapi.order.mapper.IOrderDetailMapper;
import com.fh.shopapi.order.mapper.IOrderMapper;
import com.fh.shopapi.order.param.OrderParam;
import com.fh.shopapi.order.po.Order;
import com.fh.shopapi.order.po.OrderDetail;
import com.fh.shopapi.payLog.mapper.IPayLogMapper;
import com.fh.shopapi.payLog.po.PayLog;
import com.fh.shopapi.product.mapper.IProductMapper;
import com.fh.shopapi.product.po.Product;
import com.fh.shopapi.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("orderService")
@Transactional(rollbackFor = Exception.class)
public class IOrderServiceImpl implements IOrderService {
    @Autowired
    private IOrderMapper orderMapper;
    @Autowired
    private IOrderDetailMapper orderDetailMapper;
    @Autowired
    private IAddressMapper addressMapper;
    @Autowired
    private IProductMapper productMapper;
    @Autowired
    private IPayLogMapper payLogMapper;

    //新增订单
    @Override
    public ServerResponse addOrder(Long memberId, OrderParam orderParam) {
        //判断该用户购物车是否为空
        String cartItmeJson = RedisUtil.hget(SystemConstant.PRODUCT_CART_MAP, KeyUtil.buildCartFiled(memberId));
        if (cartItmeJson == null) {
            //购物车 为空
            return ServerResponse.error(ResponseEnum.MEMBER_CART_IS_NULL);
        }
        //将json数据转为java对象
        CartVo cartVo = JSONObject.parseObject(cartItmeJson,CartVo.class);
        //新增订单详情
        String orderId = IdWorker.getTimeId();
        List<CartItemVo> cartItemVoList = cartVo.getCartItemVoList();
        //库存不足的集合
        ArrayList<CartItemVo> stockShortage = new ArrayList<>();
        for (int i=cartItemVoList.size()-1; i>=0;i--) {
            //判断该商品库存是否足够
            Product product = productMapper.selectById(cartItemVoList.get(i).getProductId());
            if (cartItemVoList.get(i).getCount() <= product.getStock()){
                //更新数据库的数据
                Long payStatus = productMapper.updateStock(cartItemVoList.get(i).getProductId(), cartItemVoList.get(i).getCount());
                if (payStatus == 0){
                    //库存不足
                    stockShortage.add(cartItemVoList.get(i));
                    //购物车中移出
                    cartItemVoList.remove(cartItemVoList.get(i));
                    //return ServerResponse.error(ResponseEnum.PRODUCT_STOCK_IS_MIN);
                }else{
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrderId(orderId);
                    orderDetail.setUserId(memberId);
                    orderDetail.setProductId(cartItemVoList.get(i).getProductId());
                    orderDetail.setProductName(cartItemVoList.get(i).getProductName());
                    orderDetail.setCount(cartItemVoList.get(i).getCount());
                    orderDetail.setPrice(new BigDecimal(cartItemVoList.get(i).getPrice()));
                    orderDetail.setImage(cartItemVoList.get(i).getImage());
                    orderDetail.setSubTotalCount(cartItemVoList.get(i).getCount().toString());
                    orderDetailMapper.insert(orderDetail);
                }
            }else {
                //库存不足
                stockShortage.add(cartItemVoList.get(i));
                //购物车中移出
                cartItemVoList.remove(cartItemVoList.get(i));
            }

           }
        //如果此时订单为空 无商品取消订单
        if(cartItemVoList.size() == 0) {
            //购买的商品全部都库存不足
            return ServerResponse.error(ResponseEnum.STOCK_IS_SHORTAGE);
        }
        //更新数据
        Long totalCount = 0L;
        BigDecimal totalPrice = new BigDecimal(0);
        for (int i = 0; i < cartItemVoList.size(); i++) {
            totalCount += cartItemVoList.get(i).getCount();
            totalPrice = BigDecimalUtil.add(totalPrice.toString(),cartItemVoList.get(i).getSubTotalPrice());
        }
        //新增订单
        Order order = new Order();
        order.setId(orderId);
        order.setUserId(memberId);
        order.setStatus(SystemConstant.ORDER_STATUS_NO_PAY);//未支付状态
        order.setPayType(orderParam.getPayType());//支付方式
        order.setCreateTime(new Date());
        order.setTotalCount(totalCount);
        order.setTotalPrice(totalPrice.toString());
        order.setInvoice(1);//发票
        //通过地址id查询地址信息
        Address address = addressMapper.findAddressById(orderParam.getAddressId());
        order.setConsignee(address.getRealName());//收货人
        order.setConsigneePhone(address.getConsigneePhone());//收货人电话
        order.setAddress(address.getAreaName());//收货地址
        order.setPostcode(address.getPostcode());//邮编
        /*order.setOrderStatusDescribe("");//订单描述
        order.setPostage(new BigDecimal(1));//邮费
        order.setInvoice(2);//邮费
        order.setOrderCloseTime(new Date());//订单关闭时间
        order.setOrderSuccessTime(new Date());//订单完成时间
        order.setShipmentsTime(new Date());//订单发货时间
        order.setOrderCommentTime(new Date());//订单完成评论时间*/
        orderMapper.insert(order);
        //添加支付日志记录
        PayLog payLog = new PayLog();
        String id = IdUtil.getID();
        payLog.setOutTradeNo(id);
        payLog.setOrderId(orderId);
        payLog.setUserId(memberId);
        payLog.setCreateTime(new Date());
        payLog.setPayType(orderParam.getPayType());
        payLog.setPayPrice(new BigDecimal(order.getTotalPrice()));
        payLog.setPayStatic(SystemConstant.ORDER_STATUS_NO_PAY);//未支付
        payLogMapper.insert(payLog);
        //将支付日志信息存入redis
        String payLogJson = JSONObject.toJSONString(payLog);
        //将这未支付的订单存入redis
        RedisUtil.setEx(KeyUtil.buildPayLogInfo(memberId),payLogJson, SystemConst.REDIS_TOKEN_TIME);
        //清空购物车
        RedisUtil.hdel(SystemConstant.PRODUCT_CART_MAP, KeyUtil.buildCartFiled(memberId));
        return ServerResponse.success(stockShortage);
    }




}
