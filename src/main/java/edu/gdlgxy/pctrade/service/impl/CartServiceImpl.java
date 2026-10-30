package edu.gdlgxy.pctrade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.gdlgxy.pctrade.common.Result;
import edu.gdlgxy.pctrade.entity.Cart;
import edu.gdlgxy.pctrade.entity.CartItemVO;
import edu.gdlgxy.pctrade.entity.Product;
import edu.gdlgxy.pctrade.mapper.CartMapper;
import edu.gdlgxy.pctrade.mapper.ProductMapper;
import edu.gdlgxy.pctrade.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public Result<String> addToCart(Long productId, Long userId){
        //判断购物车中是否已经存在该商品
        LambdaQueryWrapper<Cart> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId,userId).eq(Cart::getProductId,productId);
        if(cartMapper.selectCount(wrapper)>0){
             return Result.error("购物车中已存在该商品");
        }
        //构建购物车对象并保存
        Cart cart=new Cart();
        cart.setUserId(userId);
        cart.setProductId(productId);
        cartMapper.insert(cart);

        return Result.success("添加成功");
    }
    @Override
    public Result<List<CartItemVO>> getMyCart(Long userId){
        // 1. 查出该用户的购物车记录
        LambdaQueryWrapper<Cart> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId,userId).orderByDesc(Cart::getCreateTime);
        List<Cart> cartList=cartMapper.selectList(wrapper);

        // 2. 组装数据
        List<CartItemVO> voList=new ArrayList<>();
        for(Cart cart:cartList){
            CartItemVO vo=new CartItemVO();
            vo.setCartId(cart.getId());
            vo.setProductId(cart.getProductId());

            // 根据商品ID去查商品详情
            Product product=productMapper.selectById(cart.getProductId());
            if(product!=null){
                vo.setTitle(product.getTitle());
                vo.setDescription(product.getDescription());
                vo.setPrice(product.getPrice());
                vo.setImageUrl(product.getImageUrl());
                voList.add(vo);
            }
        }

        return Result.success(voList);
    }
    @Override
    public Result<String> deleteCartItem(Long cartId, Long userId) {
        // 直接按照 cartId 和 userId 去删除，防止越权删除别人的购物车
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getId, cartId).eq(Cart::getUserId, userId);

        int rows = cartMapper.delete(wrapper);
        if (rows > 0) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败，记录不存在或无权删除");
        }
    }
}
