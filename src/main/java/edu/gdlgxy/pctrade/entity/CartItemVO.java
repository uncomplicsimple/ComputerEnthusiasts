package edu.gdlgxy.pctrade.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItemVO {
    private Long cartId; // 购物车记录ID（删除时用）
    private Long productId; // 商品ID
    private String title;// 商品标题
    private String description; // 商品描述
    private BigDecimal price; // 单价
    private String imageUrl;// 图片

}
