package edu.gdlgxy.pctrade.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return " 二手配件交易平台启动成功!";
    }
}
