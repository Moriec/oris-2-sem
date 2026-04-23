package com.vinogradov.controller;

import com.vinogradov.aop.MetricsStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/admin/metrics")
@RequiredArgsConstructor
public class MetricsController {

    private final MetricsStorage metricsStorage;

    @GetMapping
    public Map<String, Map<String, Long>> getMetrics() {
        return metricsStorage.getAllMetrics();
    }
}
