package edu.gdlgxy.pctrade.service;

import edu.gdlgxy.pctrade.common.Result;
import edu.gdlgxy.pctrade.entity.Order;

public interface OrderService {
    // 结算生成订单
    Result<String> createOrder(Long userId);
    // 获取我的订单列表
    Result<java.util.List<Order>> getMyOrders(Long userId);
}
