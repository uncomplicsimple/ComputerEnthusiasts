package edu.gdlgxy.pctrade.service;

import edu.gdlgxy.pctrade.common.Result;
import edu.gdlgxy.pctrade.entity.Cart;
import edu.gdlgxy.pctrade.entity.CartItemVO;

import java.util.List;

public interface CartService {
    Result<String> addToCart(Long productId,Long userId);
    Result<List<CartItemVO>> getMyCart(Long userId);
    // 删除购物车记录
    Result<String> deleteCartItem(Long userId, Long productId);
}
