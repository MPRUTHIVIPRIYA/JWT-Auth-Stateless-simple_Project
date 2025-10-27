package com.example.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect 
@Component 
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String servicePointcut = "execution(public * com.example.service.*.*(..))";

    @Before(servicePointcut)
    public void logBeforeServiceCall(JoinPoint joinPoint) {
        log.info(">>>> [AOP] Entering Service Method: {}", joinPoint.getSignature().toShortString());
    }

    /**
     * Advice: Logs AFTER the service method returns successfully.
     */
    @AfterReturning(pointcut = servicePointcut, returning = "result")
    public void logAfterServiceReturn(JoinPoint joinPoint, Object result) {
        log.info("<<<< [AOP] Exiting Service Method: {}", joinPoint.getSignature().toShortString());
    }
}