package edu.gdlgxy.pctrade.controller;

import edu.gdlgxy.pctrade.common.Result;
import edu.gdlgxy.pctrade.dto.UserLoginDTO;
import edu.gdlgxy.pctrade.dto.UserRegisterDTO;
import edu.gdlgxy.pctrade.entity.User;
import edu.gdlgxy.pctrade.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller; // 注意：改用 Controller
import org.springframework.ui.Model; // 注意：导包改成了 springframework 的 Model
import org.springframework.web.bind.annotation.*;

@Controller // 核心修改：不能是 @RestController，否则无法返回 HTML 页面
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // ============ 接口测试 (返回 JSON) ============

    @PostMapping("/register")
    @ResponseBody // 加上这个，这个方法才返回 JSON
    public Result<String> register(@RequestBody UserRegisterDTO dto){
        return userService.register(dto);
    }

    @PostMapping("/login")
    @ResponseBody // 加上这个，这个方法才返回 JSON
    public Result<User> login(@RequestBody UserLoginDTO dto, HttpSession session){
        Result<User> result = userService.login(dto);
        if(result.getCode() == 200){
            session.setAttribute("loginUser", result.getData());
        }
        return result;
    }

    // ============ 页面表单提交 (返回 HTML 视图) ============

    // 处理 HTML 登录表单
    @PostMapping("/login-html")
    public String loginHtml(UserLoginDTO dto, HttpSession session, Model model){
        Result<User> result = userService.login(dto);
        if(result.getCode() == 200){
            session.setAttribute("loginUser", result.getData());
            return "redirect:/"; // 登录成功，重定向回首页
        }else{
            model.addAttribute("errorMsg", result.getMsg()); // 现在有 addAttribute 方法了
            return "login"; // 登录失败，带着错误信息回登录页
        }
    }

    // 处理 HTML 注册表单
    @PostMapping("/register-html")
    public String registerHtml(UserRegisterDTO dto, Model model) {
        Result<String> result = userService.register(dto);
        if (result.getCode() == 200) {
            return "redirect:/user/login-page"; // 注册成功去登录
        } else {
            model.addAttribute("errorMsg", result.getMsg());
            return "register"; // 注册失败，带着错误信息回注册页
        }
    }

    //退出登录（GET 请求）
    @GetMapping("/logout")
    public String logout(HttpSession session){
        //清空 Session 里的用户信息
        session.invalidate();
        //重定向回首页
        return "redirect:/";
    }
}