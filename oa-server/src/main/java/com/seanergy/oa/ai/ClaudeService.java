package com.seanergy.oa.ai;

import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClaudeService {

    private final WebClient claudeWebClient;

    @Value("${claude.model}")
    private String model;

    /**
     * 텍스트를 Claude에 전달하여 구조화된 데이터를 추출한다. (PDF용)
     */
    public String extractFields(String rawText, String promptTemplate) {
        String prompt = promptTemplate.replace("{document_text}", rawText);

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "max_tokens", 1024,
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                )
        );

        log.info("[Claude] 텍스트 추출 요청 - 모델: {}, 길이: {}자", model, rawText.length());

        String response = claudeWebClient.post()
                .uri("/messages")
                .bodyValue(requestBody)
                .exchangeToMono(r -> {
                    log.info("[Claude] 응답 상태: {}", r.statusCode());
                    return r.bodyToMono(String.class);
                })
                .block();

        log.info("[Claude] 응답 수신 완료");
        return response;
    }

    /**
     * 이미지를 Claude Vision에 직접 전달하여 구조화된 데이터를 추출한다. (JPG/PNG용)
     * OCR 없이 Claude가 이미지를 직접 읽는다.
     */
    public String extractFieldsFromImage(byte[] imageData, String mediaType, String promptTemplate) {
        String base64Image = Base64.getEncoder().encodeToString(imageData);
        String prompt = promptTemplate.replace("{document_text}", "(이미지로 전달됨)");

        // Claude Vision API: 이미지 + 텍스트를 함께 전달
        Map<String, Object> requestBody = Map.of(
                "model", model,
                "max_tokens", 1024,
                "messages", List.of(
                        Map.of("role", "user", "content", List.of(
                                Map.of(
                                        "type", "image",
                                        "source", Map.of(
                                                "type", "base64",
                                                "media_type", mediaType,
                                                "data", base64Image
                                        )
                                ),
                                Map.of(
                                        "type", "text",
                                        "text", prompt
                                )
                        ))
                )
        );

        log.info("[Claude Vision] 이미지 추출 요청 - 모델: {}, 크기: {}KB", model, imageData.length / 1024);

        String response = claudeWebClient.post()
                .uri("/messages")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.info("[Claude Vision] 응답 수신 완료");
        return response;
    }
}
