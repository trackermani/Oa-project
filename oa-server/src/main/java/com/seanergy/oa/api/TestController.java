package com.seanergy.oa.api;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seanergy.oa.ai.ClaudeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final ClaudeService claudeService;

    @GetMapping("/claude")
    public Map<String, Object> testClaude(@RequestParam("message") String message) {
        try {
            String response = claudeService.extractFields(message, "{document_text}");
            return Map.of(
                    "request", message,
                    "response", response
            );
        } catch (Exception e) {
            return Map.of(
                    "request", message,
                    "error", e.getClass().getSimpleName() + ": " + e.getMessage()
            );
        }
    }
}
