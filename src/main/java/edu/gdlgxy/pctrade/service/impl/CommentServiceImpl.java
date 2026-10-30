// CommentServiceImpl.java
package edu.gdlgxy.pctrade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.gdlgxy.pctrade.common.Result;
import edu.gdlgxy.pctrade.entity.Comment;
import edu.gdlgxy.pctrade.entity.CommentVO;
import edu.gdlgxy.pctrade.entity.User;
import edu.gdlgxy.pctrade.mapper.CommentMapper;
import edu.gdlgxy.pctrade.mapper.UserMapper;
import edu.gdlgxy.pctrade.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<String> addComment(Comment comment) {
        comment.setCreateTime(LocalDateTime.now());
        commentMapper.insert(comment);
        return Result.success("评论成功");
    }

    @Override
    public Result<List<CommentVO>> getCommentsByPostId(Long postId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getPostId, postId).orderByDesc(Comment::getCreateTime);
        List<Comment> comments = commentMapper.selectList(wrapper);

        List<CommentVO> voList = new ArrayList<>();
        for (Comment c : comments) {
            CommentVO vo = new CommentVO();
            vo.setId(c.getId());
            vo.setContent(c.getContent());
            vo.setCreateTime(c.getCreateTime());

            User user = userMapper.selectById(c.getUserId());
            vo.setNickname(user != null ? user.getNickname() : "已注销用户");
            voList.add(vo);
        }
        return Result.success(voList);
    }
}