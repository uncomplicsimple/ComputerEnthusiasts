package edu.gdlgxy.pctrade.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        //将 /uploads/** 的请求，映射到本地硬盘的 D:/pc_trade_uploads/ 目录
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:D:/pc_trade_uploads/");
    }
}
