package edu.gdlgxy.pctrade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.gdlgxy.pctrade.common.Result;
import edu.gdlgxy.pctrade.entity.Post;
import edu.gdlgxy.pctrade.entity.PostLike;
import edu.gdlgxy.pctrade.mapper.PostLikeMapper;
import edu.gdlgxy.pctrade.mapper.PostMapper;
import edu.gdlgxy.pctrade.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostServiceImpl implements PostService{
    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostLikeMapper postLikeMapper;

    @Override
    public Result<String> publishPost(Post post, Long userId) {
        post.setUserId(userId);
        post.setViewCount(0);
        post.setLikeCount(0);
        postMapper.insert(post);
        return Result.success("发帖成功");
    }
    @Override
    public Result<List<Post>> getPostList(int page, int size) {
        Page<Post> postPage = new Page<>(page, size);
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        //按发帖时间倒序排列
        wrapper.orderByDesc(Post::getCreateTime);
        Page<Post> resultPage=postMapper.selectPage(postPage,wrapper);
        return Result.success(resultPage.getRecords());
    }
    @Override
    public Post getPostDetail(Long id) {
        Post post=postMapper.selectById(id);
        if(post!=null){
            //增加浏览量
            post.setViewCount(post.getViewCount()+1);
            postMapper.updateById(post);
        }
        return post;
    }

    @Override
    public Result<String> toggleLike(Long postId, Long userId) {
        // 1. 检查是否点过赞
        LambdaQueryWrapper<PostLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PostLike::getPostId, postId).eq(PostLike::getUserId, userId);
        PostLike existingLike = postLikeMapper.selectOne(wrapper);

        Post post = postMapper.selectById(postId);
        if (post == null) return Result.error("帖子不存在");

        if (existingLike != null) {
            // 2. 如果点过赞，说明是“取消点赞”
            postLikeMapper.deleteById(existingLike.getId());
            // 点赞数 -1（防止出现负数）
            if (post.getLikeCount() > 0) {
                post.setLikeCount(post.getLikeCount() - 1);
                postMapper.updateById(post);
            }
            return Result.success("已取消点赞");
        } else {
            // 3. 如果没点过，说明是“点赞”
            PostLike newLike = new PostLike();
            newLike.setPostId(postId);
            newLike.setUserId(userId);
            newLike.setCreateTime(LocalDateTime.now());
            postLikeMapper.insert(newLike);

            post.setLikeCount(post.getLikeCount() + 1);
            postMapper.updateById(post);
            return Result.success("点赞成功");
        }
    }
}
