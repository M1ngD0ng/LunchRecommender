package com.sparta.lunchrecommender.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Pointcut("execution(* com.sparta.lunchrecommender.domain..*.controller.*.*(..))")
    public void controllerPointcut() {
    }

    @Around("controllerPointcut()")
    public Object requestLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        final ContentCachingRequestWrapper cachingRequest = ((ContentCachingRequestWrapper) request);

        Object output = joinPoint.proceed(joinPoint.getArgs());

        log.info("----------- New Request -----------");
        log.info("Request URL : " + request.getRequestURL());
        log.info("HTTP Method : " + request.getMethod());
        log.info("--------------- End ---------------");

        return output;
    }
}
