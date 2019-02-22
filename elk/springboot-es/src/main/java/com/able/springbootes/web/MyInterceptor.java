package com.able.springbootes.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author jipeng
 * @date 2019-02-22 15:48
 * @description
 */
@Slf4j
public class MyInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, String[]> parameterMap = request.getParameterMap();
        parameterMap.entrySet().forEach(x->{
           log.info("key={},value={}",x.getKey(),x.getValue());
        });
        log.info("myInterceptor-preHander: 在请求处理之前进行调用（Controller方法调用之前）");
        // 需要返回true，否则请求不会被控制器处理
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("myInterceptor-postHandle:请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后），如果异常发生，则该方法不会被调用");
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        log.info("controller object is {}", handlerMethod.getBean().getClass().getName());
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("myInterceptor-afterCompletion:在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）");
        super.afterCompletion(request, response, handler, ex);
    }
}

