package com.vinogradov.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class MetricAspect {

    private final MetricsStorage metricsStorage;

    @Pointcut("@annotation(com.vinogradov.annotation.Metric)")
    public void metricPointcut() {
    }

    @AfterReturning(pointcut = "metricPointcut()")
    public void afterReturning(JoinPoint joinPoint) {
        metricsStorage.incrementSuccess(joinPoint.getSignature().toShortString());
    }

    @AfterThrowing(pointcut = "metricPointcut()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Throwable ex) {
        metricsStorage.incrementFailure(joinPoint.getSignature().toShortString());
    }
}
