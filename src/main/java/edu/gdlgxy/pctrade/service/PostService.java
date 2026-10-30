package edu.gdlgxy.pctrade.service;

import edu.gdlgxy.pctrade.common.Result;
import edu.gdlgxy.pctrade.entity.Post;
import edu.gdlgxy.pctrade.entity.Product;

import java.util.List;

public interface PostService {
    // 发帖
    Result<String> publishPost(Post post, Long userId);
    // 获取帖子列表
    Result<List<Post>> getPostList(int page, int size);
    // 获取帖子详情
    Post getPostDetail(Long id);
    // 点赞/取消点赞
    Result<String> toggleLike(Long postId, Long userId);

}
