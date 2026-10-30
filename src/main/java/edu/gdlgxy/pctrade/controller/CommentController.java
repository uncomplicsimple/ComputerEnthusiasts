package edu.gdlgxy.pctrade.controller;

import edu.gdlgxy.pctrade.common.Result;
import edu.gdlgxy.pctrade.entity.Comment;
import edu.gdlgxy.pctrade.entity.User;
import edu.gdlgxy.pctrade.service.CommentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    // 提交评论 (AJAX)
    @PostMapping("/add")
    public Result<String> add(@RequestBody Comment comment, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return Result.error("请先登录再评论");
        }
        comment.setUserId(loginUser.getId());
        return commentService.addComment(comment);
    }
}
