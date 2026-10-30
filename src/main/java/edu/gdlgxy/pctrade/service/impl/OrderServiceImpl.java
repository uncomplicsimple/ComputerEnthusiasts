package edu.gdlgxy.pctrade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import edu.gdlgxy.pctrade.common.Result;
import edu.gdlgxy.pctrade.entity.*;
import edu.gdlgxy.pctrade.mapper.*;
import edu.gdlgxy.pctrade.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    @Override
    @Transactional(rollbackFor=Exception.class)//开启事务，任何一步报错全部回滚
    public Result<String> createOrder(Long userId){
        // 1. 查询该用户的购物车
        LambdaQueryWrapper<Cart> cartWrapper = new LambdaQueryWrapper<>();
        cartWrapper.eq(Cart::getUserId,userId);
        List<Cart> cartList=cartMapper.selectList(cartWrapper);
        if(cartList.isEmpty()){
           return Result.error("购物车为空");
        }

        // 2. 计算总金额，并检查商品是否还在售
        BigDecimal totalAmount=BigDecimal.ZERO;
        for(Cart cart:cartList){
            Product product=productMapper.selectById(cart.getProductId());
            if(product==null||product.getStatus()!=1){
                return Result.error("商品 [" + (product != null ? product.getTitle() : "未知") + "] 已售出或下架");
            }
            totalAmount=totalAmount.add(product.getPrice());
        }

        // 3. 生成订单主表
        String orderNo = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setTotalPrice(totalAmount);
        order.setStatus(1); // 为了演示方便，直接设为 1 已支付（实际应有支付接口）
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.insert(order);

        // 4. 生成订单明细，并把商品状态改为已售出（防超卖）
        for(Cart cart:cartList){
            Product product = productMapper.selectById(cart.getProductId());

            // 写入明细表
            OrderItem item = new OrderItem();
            item.setOrderNo(orderNo);
            item.setProductId(product.getId());
            item.setPrice(product.getPrice());
            item.setQuantity(1);
            item.setCreateTime(LocalDateTime.now());
            orderItemMapper.insert(item);

            // 更新商品状态为 2已售出（这里加了乐观锁的概念：必须是 status=1 才能改成 2）
            LambdaUpdateWrapper<Product> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Product::getId, product.getId())
                    .eq(Product::getStatus, 1) // 关键：只有状态是1在售才更新
                    .set(Product::getStatus, 2)
                    .set(Product::getUpdateTime, LocalDateTime.now());
            int rows = productMapper.update(null, updateWrapper);
            if (rows == 0) {
                // 如果更新失败，说明被其他人抢先买走了
                throw new RuntimeException("商品 [" + product.getTitle() + "] 已被别人抢先买走！");
            }
        }
        // 5. 清空购物车
        cartMapper.delete(cartWrapper);
        return Result.success("下单成功!订单号：" + orderNo);
    }
    @Override
    public Result<List<Order>> getMyOrders(Long userId){
        LambdaQueryWrapper<Order> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId,userId).orderByDesc(Order::getCreateTime);
        return Result.success(orderMapper.selectList(wrapper));
    }



}
