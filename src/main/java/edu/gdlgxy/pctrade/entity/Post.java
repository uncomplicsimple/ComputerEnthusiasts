package edu.gdlgxy.pctrade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("post")
public class Post {
    @TableId(type=IdType.AUTO)
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private Integer viewCount;
    private Integer likeCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
