package org.kazak.loggingstarter.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.kazak.loggingstarter.annotation.LogExecutionTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Optional;

@Aspect
public class LogExecutionAspect {

    private static final Logger log = LoggerFactory.getLogger(LogExecutionAspect.class);

    @Around("@annotation(org.kazak.loggingstarter.annotation.LogExecutionTime)")
    public Object aroundLogExecutionMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        LogExecutionTime annotation = method.getAnnotation(LogExecutionTime.class);
        String annotatedMethodName = Optional.ofNullable(annotation)
                .map(LogExecutionTime::methodName)
                .filter(StringUtils::hasText)
                .orElse(method.getName());

        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            throw e.getCause();
        } finally {
            log.info("Время выполнения метода {} : {} ", annotatedMethodName, System.currentTimeMillis() - start);
        }
    }
}
