package edu.gdlgxy.pctrade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("order_item")
public class OrderItem {
    @TableId(type=IdType.AUTO)
    private Long id;
    private String orderNo;
    private Long productId;
    private BigDecimal price;
    private Integer quantity;
    private LocalDateTime createTime;
}
