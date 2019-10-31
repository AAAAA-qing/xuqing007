package com.fh.shopapi.cart.biz;


import com.alibaba.fastjson.JSONObject;
import com.fh.shopapi.cart.po.CartItemVo;
import com.fh.shopapi.cart.po.CartVo;
import com.fh.shopapi.common.ResponseEnum;
import com.fh.shopapi.common.ServerResponse;
import com.fh.shopapi.product.mapper.IProductMapper;
import com.fh.shopapi.product.po.Product;
import com.fh.shopapi.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Key;
import java.util.List;

@Service("cartService")
public class ICartServiceImpl implements ICartService {
    @Autowired
    private IProductMapper productMapper;

    //购物车查询
    @Override
    public ServerResponse findCarts(Long id) {
        String cartInfoJson = RedisUtil.hget(SystemConstant.PRODUCT_CART_MAP ,KeyUtil.buildCartFiled(id));
        if (cartInfoJson == null) {
            //购物车为空
            return ServerResponse.error(ResponseEnum.MEMBER_CART_IS_NULL);
        }
        //转换为java 对象 响应给前台
        CartVo cartVo = JSONObject.parseObject(cartInfoJson, CartVo.class);
        return ServerResponse.success(cartVo);
    }

    //删除购物车商品
    @Override
    public ServerResponse deleteCartItem(Long memberId, Long productId) {
        //根据memberId 查询redis中对应会员的购物车
        CartVo cartVo = buildCartVo(memberId);
        List<CartItemVo> cartItemVoList = cartVo.getCartItemVoList();
        if(cartItemVoList.size() == 0) {
            return ServerResponse.error(ResponseEnum.MEMBER_CART_IS_NULL);
        }
        //判断该商品是否存在购物车中
        Boolean deleteFlag = false;
        for (CartItemVo cartItemVo : cartItemVoList) {
            if (cartItemVo.getProductId() == productId){
                //存在该商品 删除
                cartItemVoList.remove(cartItemVo);
                deleteFlag = true;
                break;
            }
        }
        if (deleteFlag){
            //刷新redis中的数据
            return buildCartTotalCount(cartVo,memberId);
        }else {
            //购物车中不存在 该商品
            return ServerResponse.error(ResponseEnum.MEMBER_CART_ITEM_IS_NULL);
        }
    }

    //新增购物车
    @Override
    public ServerResponse addCart(Long memberId, Long productId, Long count) {
        //非空判断 根据productId判断当前商品是否存在
        Product product = productMapper.selectById(productId);
        if (product == null) {
            return ServerResponse.error(ResponseEnum.PRODUCT_IS_NULL);
        }
        //判断上下架 状态
        if (!(product.getIsValid() == SystemConstant.PRODUCT_VALID_STATUS)) {
            return ServerResponse.error(ResponseEnum.PRODUCT_IS_NOT_VALID);
        }
        //以及库存 true 返回库存不足
        if (product.getStock() < count) {
            return ServerResponse.error(ResponseEnum.PRODUCT_STOCK_IS_MIN);
        }
        //根据memberId 查询redis中对应会员的购物车
        CartVo cartVo = buildCartVo(memberId);
        List<CartItemVo> cartItemVoList = cartVo.getCartItemVoList();

        //判断购物车中是否存在商品
        if (cartItemVoList.size() == 0){
            //购物车为空 直接添加进购物车
            insertCartProduct(productId, count, product, cartVo);
            //设置购物车商品总条数 总价格 然后刷新redis的中的数据
            buildCartTotalCount(cartVo, memberId);
            return ServerResponse.success();
        }

        //购物车不为空时
        if (cartItemVoList.size() != 0){
            //redis中存在购物车  判断购物车是否有该商品
            for (CartItemVo cartItemVo : cartItemVoList) {
                if (productId.equals(cartItemVo.getProductId())){
                    //购物车中存在该商品 个数在原有基础++
                    cartItemVo.setCount(cartItemVo.getCount()+count);
                    if (cartItemVo.getCount() == 0) {
                        //购物车该商品个数为0时 删除该商品
                        cartItemVoList.remove(cartItemVo);
                    }
                    //重新计算价格 新增加购物车商品的价格 加原有的
                    BigDecimal mul = BigDecimalUtil.mul(cartItemVo.getPrice(), count.toString());
                    BigDecimal add = BigDecimalUtil.add(mul.toString(), cartItemVo.getSubTotalPrice());
                    cartItemVo.setSubTotalPrice(add.toString());
                    //设置购物车商品总条数 总价格 然后刷新redis的中的数据
                    return buildCartTotalCount(cartVo, memberId);
                }
            }
            //有购物车 但不存在该商品
            cartVo = insertCartProduct(productId, count, product, cartVo);
        }
        //设置购物车商品总条数 总价格 然后刷新redis的中的数据
        return buildCartTotalCount(cartVo, memberId);
    }



    private CartVo insertCartProduct(Long productId, Long count, Product product, CartVo cartVo) {
        CartItemVo cartItemVo = new CartItemVo();
        cartItemVo.setProductId(productId);
        cartItemVo.setProductName(product.getProductName());
        cartItemVo.setImage(product.getMainImage());
        cartItemVo.setPrice(product.getPrice().toString());
        cartItemVo.setCount(count);
        cartItemVo.setSubTotalPrice(BigDecimalUtil.mul(count.toString(),product.getPrice().toString()).toString());
        //刷新购物车中的数据
        List<CartItemVo> cartItemVoList = cartVo.getCartItemVoList();
        cartItemVoList.add(cartItemVo);
        cartVo.setCartItemVoList(cartItemVoList);
        return cartVo;
    }

    //getCartVoJson
    private ServerResponse buildCartTotalCount(CartVo cartVo,Long memberId) {
        //取出 该会员对应的购物车
        List<CartItemVo> cartItemVoList = cartVo.getCartItemVoList();
        Long totalCount = 0L;
        BigDecimal totalPrice = new BigDecimal(0);
        for (int i = 0; i < cartItemVoList.size(); i++) {
            if (cartItemVoList.get(i).getCount() != 0) {
                totalCount += cartItemVoList.get(i).getCount();
                totalPrice = BigDecimalUtil.add(totalPrice.toString(),cartItemVoList.get(i).getSubTotalPrice());
            }else {
                //购物车该商品个数为0时 删除该商品
                cartItemVoList.remove(cartItemVoList.get(i));
            }
        }
        //如果购物车此时为删除购物车
        if(cartItemVoList.size() == 0) {
            RedisUtil.hdel(SystemConstant.PRODUCT_CART_MAP,KeyUtil.buildCartFiled(memberId));
            return ServerResponse.success();
        }
        //更新数据
        cartVo.setTotalCount(totalCount);
        cartVo.setTotalPrice(totalPrice.toString());
        //将集合转为json格式
        String cartVoJson= JSONObject.toJSONString(cartVo);
        //刷新redis中的数据
        RedisUtil.hset(KeyUtil.buildCartFiled(memberId),cartVoJson);
        return ServerResponse.success();
    }


    //获取redis中所有的 会员对应的购物车
    private CartVo buildCartVo(Long memberId) {
        String carVo = RedisUtil.hget(SystemConstant.PRODUCT_CART_MAP, KeyUtil.buildCartFiled(memberId));
        if (carVo == null) {
            //会员对应的购物车不存在  new
            CartVo cartVo = new CartVo();
            return cartVo;
        }
        //转换为 java对象
        CartVo cartVo = JSONObject.parseObject(carVo, CartVo.class);
        return cartVo;
    }
}
