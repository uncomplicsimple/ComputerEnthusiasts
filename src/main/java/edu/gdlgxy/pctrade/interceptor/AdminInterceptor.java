package edu.gdlgxy.pctrade.interceptor;

import edu.gdlgxy.pctrade.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminInterceptor implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginUser");

        // 1. 没登录，返回 401 未授权
        if (user == null) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\": 401, \"msg\": \"请先登录\"}");
            return false;
        }

        // 2. 不是管理员，返回 403 禁止访问
        if (user.getRole() == null || user.getRole() != 1) {
            response.setStatus(403);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\": 403, \"msg\": \"你没有管理员权限\"}");
            return false;
        }

        return true;
    }
}
