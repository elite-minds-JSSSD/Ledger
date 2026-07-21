package com.techwing.ledger.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiClientService {

    private final RestTemplate restTemplate;

    @Value("${ai.service.url}")
    private String aiServiceUrl;

    @Value("${ai.service.reconcile-path}")
    private String reconcilePath;

    @Value("${ai.service.discrepancy-path}")
    private String discrepancyPath;

    @Value("${ai.service.report-summary-path}")
    private String reportSummaryPath;

    private HttpHeaders jsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    /**
     * Call python-ai service for smart reconciliation matching
     */
    public Object callAiReconcile(Object payload) {
        try {
            HttpEntity<Object> entity = new HttpEntity<>(payload, jsonHeaders());
            ResponseEntity<Object> response = restTemplate.postForEntity(
                    aiServiceUrl + reconcilePath, entity, Object.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("AI reconcile service unavailable: {}", e.getMessage());
            return Map.of("error", "AI service unavailable", "fallback", true);
        }
    }

    /**
     * Call python-ai for discrepancy pattern analysis
     */
    public Object callAiDiscrepancyAnalysis(Object payload) {
        try {
            HttpEntity<Object> entity = new HttpEntity<>(payload, jsonHeaders());
            ResponseEntity<Object> response = restTemplate.postForEntity(
                    aiServiceUrl + discrepancyPath, entity, Object.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("AI discrepancy service unavailable: {}", e.getMessage());
            return Map.of("error", "AI service unavailable", "fallback", true);
        }
    }

    /**
     * Call python-ai to generate natural language report summary
     */
    public Object callAiReportSummary(Object payload) {
        try {
            HttpEntity<Object> entity = new HttpEntity<>(payload, jsonHeaders());
            ResponseEntity<Object> response = restTemplate.postForEntity(
                    aiServiceUrl + reportSummaryPath, entity, Object.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("AI report summary service unavailable: {}", e.getMessage());
            return Map.of("error", "AI service unavailable", "fallback", true);
        }
    }
}
