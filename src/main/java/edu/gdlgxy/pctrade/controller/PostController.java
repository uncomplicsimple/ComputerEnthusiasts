package edu.gdlgxy.pctrade.controller;

import edu.gdlgxy.pctrade.common.Result;
import edu.gdlgxy.pctrade.entity.Post;
import edu.gdlgxy.pctrade.entity.User;
import edu.gdlgxy.pctrade.service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService postService;

    //发帖接口 (表单提交)
    @PostMapping("/publish")
    public String publishPost(Post post, HttpSession session) {
        User loginUser=(User) session.getAttribute("loginUser");
        if(loginUser==null){
            return "redirect:/user/login-page";
        }
        postService.publishPost(post,loginUser.getId());
        return "redirect:/post/forum-page"; // 发帖成功跳转到论坛列表
    }
    // 获取帖子列表 (JSON接口)
    @GetMapping("/list")
    @ResponseBody
    public Result<List<Post>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return postService.getPostList(page, size);
    }

    // 点赞接口
    @PostMapping("/like/{postId}")
    @ResponseBody
    public Result<String> like(@PathVariable Long postId, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return Result.error("请先登录再点赞");
        }
        return postService.toggleLike(postId, loginUser.getId());
    }
}
