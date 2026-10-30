package edu.gdlgxy.pctrade.controller;

import edu.gdlgxy.pctrade.common.Result;
import edu.gdlgxy.pctrade.entity.Order;
import edu.gdlgxy.pctrade.entity.User;
import edu.gdlgxy.pctrade.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 结算接口（AJAX 调用
    @PostMapping("/create")
    @ResponseBody
    public Result<String> createOrder(HttpSession session){
        User loginUser=(User) session.getAttribute("loginUser");
        if(loginUser==null) return Result.error("请先登录");
        return orderService.createOrder(loginUser.getId());
    }
    // 获取我的订单列表(JSON)
    @GetMapping("/list")
    @ResponseBody
    public Result<List<Order>> list(HttpSession session){
        User loginUser=(User) session.getAttribute("loginUser");
        if(loginUser==null) return Result.error("请先登录");
        return orderService.getMyOrders(loginUser.getId());
    }

}
