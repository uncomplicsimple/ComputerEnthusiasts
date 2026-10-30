package edu.gdlgxy.pctrade.controller;

import edu.gdlgxy.pctrade.common.Result;
import edu.gdlgxy.pctrade.entity.CartItemVO;
import edu.gdlgxy.pctrade.entity.Post;
import edu.gdlgxy.pctrade.entity.Product;
import edu.gdlgxy.pctrade.entity.User;
import edu.gdlgxy.pctrade.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PageController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    //访问首页
    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "1") int page,
                        @RequestParam(required = false) String category,
                        @RequestParam(required = false) String keyword,
                        Model model) {

        //        //先查第一页的 9 条数据
//        Result<List<Product>> result = productService.getList(1,9,null,null);
//        //把数据放入 model，Thymeleaf 就能拿到
//        model.addAttribute("products",result.getData());
//        //返回 templates/index.html
//        return "index";

            // 调用新的搜索服务
            Result<List<Product>> result = productService.getList(page, 9, category, keyword);
            model.addAttribute("products", result.getData());

            // 把搜索条件再传回前端，让搜索框保留刚才输入的文字
            model.addAttribute("currentCategory", category);
            model.addAttribute("currentKeyword", keyword);

            return "index";
        }


        //跳转到登录页面
        @GetMapping("/user/login-page")
        public String loginPage () {
            return "login";
        }

        //跳转到注册页面
        @GetMapping("/user/register-page")
        public String registerPage () {
            return "register";
        }

        //跳转到商品详情页
        @GetMapping("/product/detail/{id}")
        public String detailPage (@PathVariable("id") Long id, Model model){
            Product product = productService.getById(id);
            if (product == null) {
                return "redirect:/"; // 商品不存在，回首页
            }
            model.addAttribute("product", product);
            return "detail";
        }

        // 跳转到购物车列表页面
        @GetMapping("/cart/page")
        public String cartPage (HttpSession session, Model model){
            User loginUser = (User) session.getAttribute("loginUser");
            if (loginUser == null) {
                return "redirect:/user/login-page";
            }
            // 获取购物车数据
            Result<List<CartItemVO>> result = cartService.getMyCart(loginUser.getId());
            model.addAttribute("cartItems", result.getData());
            return "cart";
        }

        @GetMapping("/order/page")
        public String orderPage (HttpSession session, Model model){
            User loginUser = (User) session.getAttribute("loginUser");
            if (loginUser == null) return "redirect:/user/login-page";
            model.addAttribute("orders", orderService.getMyOrders(loginUser.getId()).getData());
            return "order";
        }

        @Autowired
        private PostService postService;

        // 跳转发帖页面
        @GetMapping("/post/publish-page")
        public String postPublishPage () {
            return "post-publish";
        }

        // 跳转论坛列表页
        @GetMapping("/post/forum-page")
        public String forumPage (Model model){
            Result<List<Post>> result = postService.getPostList(1, 10);
            model.addAttribute("posts", result.getData());
            return "forum";
        }

        // 跳转帖子详情页
        @GetMapping("/post/detail/{id}")
        public String postDetailPage (@PathVariable("id") Long id, Model model){
            Post post = postService.getPostDetail(id);
            if (post == null) {
                return "redirect:/post/forum-page"; // 帖子不存在，回论坛列表
            }

            // 查出发帖人信息
            User author = userService.getById(post.getUserId());

            // 获取该帖子的所有评论
            model.addAttribute("comments", commentService.getCommentsByPostId(id).getData());

            model.addAttribute("post", post);
            model.addAttribute("author", author);
            return "post-detail";
        }
    }
