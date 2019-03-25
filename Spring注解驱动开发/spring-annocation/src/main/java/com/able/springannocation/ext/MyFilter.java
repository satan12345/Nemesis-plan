package com.able.springannocation.ext;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author jipeng
 * @date 2019-03-18 10:15
 * @description
 */
@Slf4j
public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("MyFilter.init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("MyFilter.doFilter");
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {
        log.info("Myfilter.destroy");
    }
}

