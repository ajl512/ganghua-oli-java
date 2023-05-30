package com.gangyunshihua.utils;

import com.gangyunshihua.exception.GlobalExceptionResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class LogUtil {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);

    public static void printLog(HttpServletRequest request, Exception exception) {
        String sOut = "\r\n" + "出错的请求地址是:" + request.getRequestURL().toString() + "\r\n";
        sOut += "出错的请求参数是:\r\n";
        for (Object key : request.getParameterMap().keySet())
            if (!"key".equals(key.toString())) sOut += key + " : " + request.getParameter(key.toString()) + "\r\n";
        sOut += exception.toString() + "\r\n";
        for (StackTraceElement s : exception.getStackTrace()) sOut += "\tat " + s + "\r\n";
        logger.error(sOut);
    }
}
