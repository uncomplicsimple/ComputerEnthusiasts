// CommentService.java
package edu.gdlgxy.pctrade.service;
import edu.gdlgxy.pctrade.common.Result;
import edu.gdlgxy.pctrade.entity.Comment;
import edu.gdlgxy.pctrade.entity.CommentVO;
import java.util.List;

public interface CommentService {
    Result<String> addComment(Comment comment);
    Result<List<CommentVO>> getCommentsByPostId(Long postId);
}