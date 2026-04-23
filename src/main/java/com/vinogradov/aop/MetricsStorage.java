package com.vinogradov.aop;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class MetricsStorage {

    private final Map<String, MethodMetrics> metrics = new ConcurrentHashMap<>();

    public void incrementSuccess(String methodName) {
        getMetrics(methodName).successCount.incrementAndGet();
    }

    public void incrementFailure(String methodName) {
        getMetrics(methodName).failureCount.incrementAndGet();
    }

    private MethodMetrics getMetrics(String methodName) {
        return metrics.computeIfAbsent(methodName, k -> new MethodMetrics());
    }

    public Map<String, Map<String, Long>> getAllMetrics() {
        Map<String, Map<String, Long>> result = new ConcurrentHashMap<>();
        metrics.forEach((methodName, m) -> {
            Map<String, Long> counts = new ConcurrentHashMap<>();
            counts.put("успешно", m.successCount.get());
            counts.put("ошибочков", m.failureCount.get());
            result.put(methodName, counts);
        });
        return result;
    }

    private static class MethodMetrics {
        final AtomicLong successCount = new AtomicLong(0);
        final AtomicLong failureCount = new AtomicLong(0);
    }
}
