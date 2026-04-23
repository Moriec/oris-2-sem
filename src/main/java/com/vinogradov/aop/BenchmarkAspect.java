package com.vinogradov.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class BenchmarkAspect {

    private final BenchmarkStorage benchmarkStorage;

    @Pointcut("@annotation(com.vinogradov.annotation.Benchmark)")
    public void benchmarkPointcut() {
    }

    @Around("benchmarkPointcut()")
    public Object measureTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long duration = System.currentTimeMillis() - start;
            benchmarkStorage.addTime(joinPoint.getSignature().toShortString(), duration);
        }
    }
}
