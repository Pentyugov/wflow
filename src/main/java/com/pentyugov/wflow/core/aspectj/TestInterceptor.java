package com.pentyugov.wflow.core.aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TestInterceptor {

    private final Logger logger = LoggerFactory.getLogger(TestInterceptor.class);

    @Pointcut("execution(* com.pentyugov.wflow.web.rest.TestController.ping())")
    public void test() {

    }

    @Before("test()")
    public void before(JoinPoint joinPoint) {
        logger.warn("awdawdawdawdawdawdawdawdawdawdawdawdawdawdawdawdawdawdawdawdawdawd");
    }
}
