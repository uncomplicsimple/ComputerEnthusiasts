package edu.gdlgxy.pctrade.controller;

import edu.gdlgxy.pctrade.common.Result;
import edu.gdlgxy.pctrade.entity.Post;
import edu.gdlgxy.pctrade.entity.Product;
import edu.gdlgxy.pctrade.entity.User;
import edu.gdlgxy.pctrade.mapper.PostMapper;
import edu.gdlgxy.pctrade.mapper.ProductMapper;
import edu.gdlgxy.pctrade.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private PostMapper postMapper;

    // 1. 跳转到后台首页（展示所有数据）
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("users", userMapper.selectList(null));
        model.addAttribute("products", productMapper.selectList(null));
        model.addAttribute("posts", postMapper.selectList(null));
        return "admin-dashboard";
    }

    // 2. 封禁/解封用户
    @PostMapping("/user/toggle/{id}")
    @ResponseBody
    public Result<String> toggleUserStatus(@PathVariable Long id) {
        User user = userMapper.selectById(id);
        if (user != null) {
            // 如果是1（正常）改成0（封禁），如果是0改成1
            user.setStatus(user.getStatus() == 1 ? 0 : 1);
            userMapper.updateById(user);
            return Result.success("操作成功");
        }
        return Result.error("用户不存在");
    }

    // 3. 下架商品
    @PostMapping("/product/off-shelf/{id}")
    @ResponseBody
    public Result<String> offShelfProduct(@PathVariable Long id) {
        Product product = productMapper.selectById(id);
        if (product != null) {
            product.setStatus(0); // 0 代表已下架
            productMapper.updateById(product);
            return Result.success("商品已下架");
        }
        return Result.error("商品不存在");
    }

    // 4. 删除违规帖子
    @PostMapping("/post/delete/{id}")
    @ResponseBody
    public Result<String> deletePost(@PathVariable Long id) {
        postMapper.deleteById(id);
        return Result.success("帖子已删除");
    }
}
