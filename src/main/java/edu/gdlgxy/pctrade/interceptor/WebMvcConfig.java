package edu.gdlgxy.pctrade.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration//配置类
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new LoginInterceptor())
//                .addPathPatterns("/**")//拦截所有请求
//                .excludePathPatterns(//但是要放行以下请求：
//                        "/user/login",//登录接口不能拦
//                        "/user/register",//注册接口不能拦
//                        "/hello"//之前的测试接口不能拦
//
//                );

        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/user/login",          // 接口测试登录
                        "/user/login-html",     // 补上这个：HTML表单登录
                        "/user/register",       // 接口测试注册
                        "/user/register-html",  // 补上这个：HTML表单注册
                        "/user/login-page",     // 页面放行
                        "/user/register-page",  // 页面放行
                        "/hello",
                        "/product/list",
                        "/",
                        "/error",
                        "/uploads/**",
                        "/product/detail/**",
                        "/cart/page",
                        "/order/page",
                        "/post/forum-page",// 论坛列表页面
                        "/post/publish-page",// 发帖页面
                        "/post/publish",// 处理发帖的接口
                        "/post/detail/**",// 帖子详情页面
                        "/comment/add",// 添加评论的接口
                        "/post/like/**",
                        "/admin/**"
                );
        // 注册管理员拦截器，只拦截 /admin/ 下的所有请求
        registry.addInterceptor(new AdminInterceptor())
                .addPathPatterns("/admin/**");
    }


}