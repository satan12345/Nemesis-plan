package com.able.springbootes.web;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author jipeng
 * @date 2019-02-22 15:09
 * @description
 */
@Slf4j
public class LoggingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("filter-init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        log.info("start to doFilter");
        long startTime = System.currentTimeMillis();
        chain.doFilter(request, response);
        long endTime = System.currentTimeMillis();
        log.info("the request of {} consumes {}ms.", ((HttpServletRequest)request).getRequestURL().toString(), (endTime - startTime));
        log.info("end to doFilter");
    }

    @Override
    public void destroy() {
        log.info("filter-destroy");

    }
}

