ComputerEnthusiasts - 二手电脑配件交易与装机交流平台

项目简介
这是一个基于 Spring Boot 3开发的全栈Web应用，旨在为装机爱好者提供一个二手配件交易与交流的社区。项目包含完整的电商交易闭环（发布、搜索、购物车、下单）以及社区互动模块（论坛、评论、点赞），并配备完善的后台管理权限系统。

技术栈
后端：Java 17,Spring Boot 3.2.5,MyBatis-Plus
前端：Thymeleaf,HTML5,CSS3,Bootstrap 5,JavaScript (Fetch API)
数据库：MySQL 8.0
安全与权限：Spring Security Crypto (BCrypt 加密),Session + Interceptor 拦截器机制
开发工具：Maven,Git,IntelliJ IDEA

核心功能与亮点
1.用户认证与安全：实现基于 Session 的登录状态保持，密码使用 BCrypt 加盐哈希存储，防止明文泄露；自定义 `LoginInterceptor` 拦截未授权访问。
2.完整电商闭环：支持多图上传、商品分类筛选与关键字模糊搜索。实现购物车增删查，以及订单生成。
3.并发控制与交易快照：下单时通过数据库乐观锁（条件更新）防止商品超卖；订单明细表保存购买时的单价，实现交易快照。
4.社区论坛互动：支持发帖、评论、点赞。点赞功能通过数据库联合唯一索引防止并发重复点赞，浏览量基于页面请求实时自增。
5.RBAC 权限管理：实现基于角色（Role）的权限控制。管理员专属后台支持用户封禁（状态流转）、商品强制下架与违规帖子删除。

快速启动指南
1.克隆项目到本地。
2.在 MySQL 中创建 `pc_trade` 数据库，并导入建表 SQL。
3.修改 `src/main/resources/application.yml` 中的数据库用户名和密码。
4.运行 `PcTradeApplication`。
5.访问 `http://localhost:8080` 即可体验。