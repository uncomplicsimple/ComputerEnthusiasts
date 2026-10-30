package edu.gdlgxy.pctrade.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1. 获取 Session
        HttpSession session = request.getSession();
        //2. 尝试获取里面存的用户信息
        Object user = session.getAttribute("loginUser");
        //3. 判断
        if (user == null){
             //没有登录,返回 401 状态码（未授权）
            response.setStatus(401);
            return false;//拦截，不让往下走了
        }
        //登录了，放行
        return true;
    }
}