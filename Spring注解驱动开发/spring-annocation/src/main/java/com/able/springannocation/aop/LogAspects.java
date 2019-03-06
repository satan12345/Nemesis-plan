package com.able.springannocation.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.Arrays;

/**
 * @author jipeng
 * @date 2019-03-05 13:09
 * @description
 */
@Aspect
@Slf4j
public class LogAspects {
    /**
     * 抽取公共切入点表达式
     * 1 本类引用:只要方法名
     * 2 其他切面引用:需要指定全类名
     */
    @Pointcut("execution(* com.able.springannocation.aop.*.*(..))")
    public void pointCut(){

    }
    @Before("pointCut()")
    public void logStart(JoinPoint joinPoint){
        String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("logStart=================,类名为:{}方法名为:{},参数为:{}",declaringTypeName,methodName, Arrays.asList(args));

    }
    @After("pointCut()")
    public void  logAfter(JoinPoint joinPoint){
        log.info("logAfter=================");
    }
    @AfterReturning(value = "pointCut()",returning = "result")
    public void logReturn(Object result){
            log.info("接收到的返回值为:{}",result);
            log.info("logReturn=======================");
    }
    //JoinPoint 一定要出现在参数表的第一位
    @AfterThrowing(value = "pointCut()",throwing = "exception")
    public void logException(JoinPoint joinPoint,Exception exception){
        log.info("exception=",exception);
        log.info("logException===================================");
    }
}

