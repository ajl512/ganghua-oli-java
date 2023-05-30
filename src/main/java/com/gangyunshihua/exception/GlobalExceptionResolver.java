package com.gangyunshihua.exception;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.gangyunshihua.utils.LogUtil;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class GlobalExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //处理异常
        ex.printStackTrace();
        LogUtil.printLog(request, ex);
        //使用FastJson提供的FastJsonJsonView视图返回，不需要捕获异常
        FastJsonJsonView view = new FastJsonJsonView();
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("code", 2);
        attributes.put("msg", "网络异常，请稍后再试！");
        view.setAttributesMap(attributes);
        //返回统一错误信息
        ModelAndView mv = new ModelAndView();
        mv.setView(view);
        return mv;
    }
}
