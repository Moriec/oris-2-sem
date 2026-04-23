package com.vinogradov.controller;

import com.vinogradov.aop.BenchmarkStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/benchmark")
@RequiredArgsConstructor
public class BenchmarkController {

    private final BenchmarkStorage benchmarkStorage;

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("середняя", benchmarkStorage.getAverageTimes());
        stats.put("за все время", benchmarkStorage.getAllTimes());
        return stats;
    }

    @GetMapping("/percentile")
    public Map<String, Object> getPercentile(
            @RequestParam String methodName,
            @RequestParam double n) {
        
        Double percentileValue = benchmarkStorage.calculatePercentile(methodName, n);
        
        Map<String, Object> result = new HashMap<>();
        result.put("имя метода", methodName);
        result.put("персентиль", n);
        result.put("значение", percentileValue != null ? percentileValue : "нет инфы");
        
        return result;
    }
}
