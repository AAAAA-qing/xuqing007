package com.fh.shopapi.pay.biz;

import com.alibaba.fastjson.JSONObject;
import com.fh.shopapi.common.ResponseEnum;
import com.fh.shopapi.common.ServerResponse;
import com.fh.shopapi.order.mapper.IOrderMapper;
import com.fh.shopapi.order.po.Order;
import com.fh.shopapi.pay.result.PayResult;
import com.fh.shopapi.payLog.mapper.IPayLogMapper;
import com.fh.shopapi.payLog.po.PayLog;
import com.fh.shopapi.utils.*;
import com.fh.github.wxpay.sdk.WXPay;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("payService")
@Transactional(rollbackFor = Exception.class)
public class IPayServiceImpl implements IPayService {
    @Autowired
    private IOrderMapper orderMapper;
    @Autowired
    private IPayLogMapper payLogMapper;



    //创建支付二维码
    @Override
    public ServerResponse createNative(Long memberId) {
        //redis中获取订单
        String payLogJson = RedisUtil.get(KeyUtil.buildPayLogInfo(memberId));
        //判断订单是否存在
        if (StringUtils.isEmpty(payLogJson)) {
            //订单不存在
            return ServerResponse.error(ResponseEnum.ORDER_IS_MISS);
        }
        //转为java对象
        PayLog payLog = JSONObject.parseObject(payLogJson, PayLog.class);
        //生成二维码 统一下单
        MyWXConfig wxConfig = new MyWXConfig();
        try {
            WXPay wxpay = new WXPay(wxConfig);
            Class<SSLUtil> sslUtilClass = SSLUtil.class;
            //获取交易单号 支付金额 二维码一次的失效时间格式为yyyyMMddHHmmss
            String outTradeNo = payLog.getOutTradeNo();
            int payPrice = BigDecimalUtil.mul(payLog.getPayPrice().toString(), "100").intValue();
            Date date = DateUtils.addMinutes(new Date(), 2);
            String dateTime = DateUtil.date2str(date, DateUtil.YYYYMMDDHHMMSS);
            Map<String, String> data = new HashMap<String, String>();
            data.put("body", "订单支付");
            data.put("out_trade_no", outTradeNo);
            data.put("total_fee", payPrice +"");
            data.put("time_expire", dateTime);
            data.put("notify_url", "http://www.example.com/wxpay/notify");
            data.put("trade_type", "NATIVE");  // 此处指定为扫码支付

            Map<String, String> resp = wxpay.unifiedOrder(data);

            //判断 通信标识
            String return_code = resp.get("return_code");
            String return_msg = resp.get("return_msg");
            if (!return_code.equalsIgnoreCase("SUCCESS")) {
                //错误信息
                return ServerResponse.error(9999, "微信支付平台:" + return_msg);
            }
            // 以下字段在return_code为SUCCESS的时候有返回
            String result_code = resp.get("result_code");
            String err_code_des = resp.get("result_code");
            if (!result_code.equalsIgnoreCase("SUCCESS")) {
                //错误信息
                return ServerResponse.error(9999, "微信支付平台:" + err_code_des);
            }
            //组装返回值
            String code_url = resp.get("code_url");
            PayResult payResult = new PayResult();
            payResult.setCodeUrl(code_url);
            payResult.setOutTradeNo(outTradeNo);
            payResult.setPayPrice(payLog.getPayPrice().toString());
            return ServerResponse.success(payResult);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    //查询支付状态
    @Override
    @Transactional(readOnly = true)
    public ServerResponse confirmPayStatus(Long memberId) {
        //查询redis中对应用户的订单
        String payLogJson = RedisUtil.get(KeyUtil.buildPayLogInfo(memberId));
        //判断
        if (StringUtils.isEmpty(payLogJson)) {
            //订单不存在
            return ServerResponse.error(ResponseEnum.ORDER_IS_MISS);
        }
        PayLog payLog = JSONObject.parseObject(payLogJson, PayLog.class);


        try {
            MyWXConfig config = new MyWXConfig();

            WXPay wxpay = new WXPay(config);
            Map<String, String> data = new HashMap<String, String>();
            Class<SSLUtil> sslUtilClass = SSLUtil.class;
            data.put("out_trade_no", payLog.getOutTradeNo());
            //订单状态
            int count = 0;
            while(true) {
                Map<String, String> resp = wxpay.orderQuery(data);

                //判断 通信标识
                String return_code = resp.get("return_code");
                String return_msg = resp.get("return_msg");
                if (!return_code.equalsIgnoreCase("SUCCESS")){
                    //错误信息
                    return ServerResponse.error(9999, "微信支付平台:" + return_msg);
                }
                // 以下字段在return_code为SUCCESS的时候有返回
                String result_code = resp.get("result_code");
                String err_code_des = resp.get("result_code");
                if (!result_code.equalsIgnoreCase("SUCCESS")) {
                    //错误信息
                    return ServerResponse.error(9999, "微信支付平台:" + err_code_des);
                }
                //以下在return_code 、result_code、trade_state都为SUCCESS时有返回 ，如trade_state不为 SUCCESS，则只返回out_trade_no
                String trade_state = resp.get("trade_state");
                //String trade_state_desc = resp.get("trade_state_desc");
                if (trade_state.equalsIgnoreCase("SUCCESS")) {
                    //支付成功 修改订单的支付状态
                    Order order = orderMapper.selectById(payLog.getOrderId());
                    order.setPayTime(new Date());
                    order.setStatus(SystemConstant.ORDER_STATUS_YES_PAY);
                    orderMapper.updateById(order);
                    //修改 支付状态记录表
                    String transaction_id = resp.get("transaction_id");
                    payLog.setPayStatic(SystemConstant.ORDER_STATUS_YES_PAY);
                    payLog.setPayTime(new Date());
                    payLog.setTransactionId(transaction_id);
                    payLogMapper.updateById(payLog);
                    return ServerResponse.success();
                }
                count++;
                Thread.sleep(3000);
                if (count >= 20) {
                    //支付超时 使其二维码失效  二维码 失效之后  刷新页面 重新生成二维码
                    return ServerResponse.error(ResponseEnum.NATIVE_OUT_TIME);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }

}
