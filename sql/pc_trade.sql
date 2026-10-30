-- ==========================================================
-- ComputerEnthusiasts 二手交易与装机论坛平台 数据库初始化脚本
-- 数据库: MySQL 8.0
-- ==========================================================

CREATE DATABASE IF NOT EXISTS pc_trade DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE pc_trade;

-- ----------------------------
-- 1. 用户表
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
                        `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
                        `username`    VARCHAR(50)  NOT NULL COMMENT '用户名',
                        `password`    VARCHAR(100) NOT NULL COMMENT '密码(BCrypt加密后)',
                        `nickname`    VARCHAR(50)  DEFAULT NULL COMMENT '昵称',
                        `avatar`      VARCHAR(255) DEFAULT NULL COMMENT '头像',
                        `email`       VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
                        `credit`      INT          DEFAULT 100 COMMENT '信用分',
                        `status`      TINYINT      DEFAULT 1 COMMENT '状态 1正常 0封禁',
                        `role`        INT          DEFAULT 0 COMMENT '角色 0普通用户 1管理员',
                        `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP,
                        `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- 2. 二手配件商品表
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
                           `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
                           `seller_id`   BIGINT       NOT NULL COMMENT '卖家ID(关联user表)',
                           `title`       VARCHAR(100) NOT NULL COMMENT '商品标题',
                           `description` TEXT         COMMENT '详细描述',
                           `price`       DECIMAL(10,2) NOT NULL COMMENT '价格',
                           `category`    VARCHAR(50)  DEFAULT NULL COMMENT '分类(如:CPU,显卡)',
                           `image_url`   VARCHAR(255) DEFAULT NULL COMMENT '图片链接',
                           `status`      TINYINT      DEFAULT 1 COMMENT '状态 1在售 2已售出 0下架',
                           `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP,
                           `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           PRIMARY KEY (`id`),
                           KEY `idx_seller` (`seller_id`),
                           KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='二手配件表';

-- ----------------------------
-- 3. 购物车表
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart` (
                        `id`          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
                        `user_id`     BIGINT      NOT NULL COMMENT '买家ID',
                        `product_id`  BIGINT      NOT NULL COMMENT '商品ID',
                        `quantity`    INT         DEFAULT 1 COMMENT '数量',
                        `create_time` DATETIME    DEFAULT CURRENT_TIMESTAMP,
                        `update_time` DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `uk_user_product` (`user_id`, `product_id`) -- 防重复加购
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- ----------------------------
-- 4. 订单主表
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
                          `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
                          `order_no`    VARCHAR(50)  NOT NULL COMMENT '订单号(唯一)',
                          `user_id`     BIGINT       NOT NULL COMMENT '买家ID',
                          `total_price` DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
                          `status`      TINYINT      DEFAULT 0 COMMENT '状态 0待支付 1已支付 2已取消',
                          `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP,
                          `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          PRIMARY KEY (`id`),
                          UNIQUE KEY `uk_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单主表';

-- ----------------------------
-- 5. 订单明细表 (交易快照)
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
                              `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
                              `order_no`    VARCHAR(50)  NOT NULL COMMENT '订单号',
                              `product_id`  BIGINT       NOT NULL COMMENT '商品ID',
                              `price`       DECIMAL(10,2) NOT NULL COMMENT '购买时的单价(交易快照)',
                              `quantity`    INT          DEFAULT 1 COMMENT '购买数量',
                              `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP,
                              PRIMARY KEY (`id`),
                              KEY `idx_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- ----------------------------
-- 6. 论坛帖子表
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
                        `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
                        `user_id`     BIGINT       NOT NULL COMMENT '发帖人ID',
                        `title`       VARCHAR(100) NOT NULL COMMENT '帖子标题',
                        `content`     TEXT         NOT NULL COMMENT '帖子内容',
                        `view_count`  INT          DEFAULT 0 COMMENT '浏览量',
                        `like_count`  INT          DEFAULT 0 COMMENT '点赞数',
                        `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP,
                        `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        PRIMARY KEY (`id`),
                        KEY `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='论坛帖子表';

-- ----------------------------
-- 7. 评论表
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
                           `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
                           `post_id`     BIGINT       NOT NULL COMMENT '帖子ID',
                           `user_id`     BIGINT       NOT NULL COMMENT '评论人ID',
                           `content`     TEXT         NOT NULL COMMENT '评论内容',
                           `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP,
                           PRIMARY KEY (`id`),
                           KEY `idx_post` (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- ----------------------------
-- 8. 帖子点赞记录表
-- ----------------------------
DROP TABLE IF EXISTS `post_like`;
CREATE TABLE `post_like` (
                             `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
                             `post_id`     BIGINT       NOT NULL COMMENT '帖子ID',
                             `user_id`     BIGINT       NOT NULL COMMENT '点赞人ID',
                             `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP,
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `uk_post_user` (`post_id`, `user_id`) -- 防重复点赞
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点赞记录表';

-- ==========================================================
-- 初始化数据
-- ==========================================================

-- 初始化一个超级管理员账号 (用户名: admin, 密码: 123456)
-- 注意：密码为 BCrypt 加密后的密文，对应明文是 123456
INSERT INTO `user` (`username`, `password`, `nickname`, `role`, `status`)
VALUES ('admin', '$2a$10$RA.3wjBdqZuU/g7b5DfdHepAEsCHhTRXTDoXlMqSL3RtDOtz06jmG', '超级管理员', 1, 1);