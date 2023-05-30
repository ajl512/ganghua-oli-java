package com.gangyunshihua.interceptor;

import com.gangyunshihua.jedis.JedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyInterceptor implements HandlerInterceptor {

    @Autowired
    private JedisClient jedisClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        String requestURL = request.getRequestURL().toString();
//        if (requestURL.endsWith("/user/findHideCheck")) return true;//查询是否需要隐藏审核
//        if (requestURL.endsWith("/user/login")) return true;//登陆
//        if (requestURL.endsWith("/diesel/findDiesel")) return true;//查询柴油价格
//        String userId = request.getParameter("userId");
//        String token = request.getParameter("token");
//        if (userId != null && token != null && token.equals(jedisClient.get(userId + "_gyToken"))) {
//            jedisClient.expire(userId + "_gyToken", 36000);
//            return true;
//        } else {
//            try {
//                response.getWriter().println(JSON.toJSONString(new GyResult().code(0)));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return false;
//        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object
            handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object
            handler, Exception ex) throws Exception {

    }
}
