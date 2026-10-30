package edu.gdlgxy.pctrade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.math.BigDecimal;

@Data
@TableName("product")
public class Product {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long sellerId;
    private String title;
    private String description;
    private BigDecimal price;//金融/价格必须用 BigDecimal，不能用 double
    private String category;
    private String imageUrl;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
