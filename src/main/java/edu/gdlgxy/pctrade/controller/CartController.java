package edu.gdlgxy.pctrade.controller;

import edu.gdlgxy.pctrade.common.Result;
import edu.gdlgxy.pctrade.entity.Cart;
import edu.gdlgxy.pctrade.entity.CartItemVO;
import edu.gdlgxy.pctrade.entity.User;
import edu.gdlgxy.pctrade.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    // 加入购物车接口（由前端发起 AJAX 请求）
    @PostMapping("/add/{productId}")
    @ResponseBody
    public Result<String> add(@PathVariable Long productId, HttpSession session) {
        User loginUser=(User) session.getAttribute("loginUser");
        if(loginUser==null){
            return Result.error("请先登录");
        }
        return cartService.addToCart(productId,loginUser.getId());
    }
    // 获取购物车列表数据（JSON）
    @GetMapping("/list")
    @ResponseBody
    public Result<List<CartItemVO>> list(HttpSession session) {
        User loginUser=(User) session.getAttribute("loginUser");
        if(loginUser==null){
            return Result.error("请先登录");
        }
        return cartService.getMyCart(loginUser.getId());
    }

    // 删除购物车项接口
    @DeleteMapping("/delete/{cartId}")
    @ResponseBody
    public Result<String> delete(@PathVariable Long cartId, HttpSession session) {
        User loginUser=(User) session.getAttribute("loginUser");
        if(loginUser==null) return Result.error("请先登录");
        return cartService.deleteCartItem(cartId,loginUser.getId());
    }
}
