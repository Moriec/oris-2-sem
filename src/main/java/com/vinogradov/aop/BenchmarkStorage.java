package com.vinogradov.aop;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class BenchmarkStorage {

    private final Map<String, List<Long>> executionTimes = new ConcurrentHashMap<>();

    public void addTime(String methodName, long timeMs) {
        executionTimes.computeIfAbsent(methodName, k -> Collections.synchronizedList(new ArrayList<>())).add(timeMs);
    }

    public Map<String, List<Long>> getAllTimes() {
        return executionTimes;
    }

    public Double calculatePercentile(String methodName, double n) {
        List<Long> times = executionTimes.get(methodName);
        if (times == null || times.isEmpty()) {
            return null;
        }

        List<Long> sortedTimes;
        synchronized (times) {
            sortedTimes = new ArrayList<>(times);
        }
        Collections.sort(sortedTimes);

        int index = (int) Math.ceil(n / 100.0 * sortedTimes.size()) - 1;
        return (double) sortedTimes.get(Math.max(0, index));
    }

    public Map<String, Double> getAverageTimes() {
        return executionTimes.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream().mapToLong(Long::longValue).average().orElse(0.0)
                ));
    }
}
