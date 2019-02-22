package com.able.springbootes.web;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jipeng
 * @date 2019-02-22 17:00
 * @description
 */
@Aspect
@Slf4j
@Component
public class TimeAspect {

    @Pointcut(value = "execution(* com.able.springbootes.controller.MyController.*(..))")
    public void pc(){

    }
    @Before(value = "pc()")
    public void before(JoinPoint joinPoint){
        log.info("time aspect is before.");
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        //url
        log.info("url={}",request.getRequestURL());
        //请求方法类型 GET POST
        log.info("method={}",request.getMethod());
        //ip
        log.info("remoteAddress={},remoteHost={},remoteUser={},remotePort={}",request.getRemoteAddr(),
                request.getRemoteHost(),request.getRemoteUser(),request.getRemotePort());

        //方法名
        log.info("method:{}",joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName());
        //参数
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {

            log.info("args={}",args[i]);
        }

    }
    @After("pc()")
    public  void after(){
        log.info("time aspect is after.");
    }
    @AfterReturning(pointcut = "pc()",returning = "object")
    public void  afterReturing(Object object){
        log.info("afterreturning---response={}",object);
    }
    @AfterThrowing(pointcut = "pc()",throwing = "exception")
    public void afterThrowing(Exception exception){
        log.info("afterThrowing",exception);
    }

    @Around("pc()")
    public Object handleTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
       log.info("time aspect is Around-start.");
        for (Object object : proceedingJoinPoint.getArgs()) {
            log.info("---"+object.toString());
        }
        long startTime = System.currentTimeMillis();
        Object obj = proceedingJoinPoint.proceed();
        log.info("time aspect is Around-after 耗时：" + (System.currentTimeMillis() - startTime));

        return obj;
    }
}


