package com.seanergy.oa.ai;

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Claude API 응답에서 JSON 데이터를 추출하는 유틸.
 *
 * Claude 응답 구조:
 * {
 *   "content": [
 *     { "type": "text", "text": "{ ... 실제 JSON ... }" }
 *   ]
 * }
 */
@Slf4j
public class ClaudeResponseParser {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Claude API 응답 전체 JSON에서 content[0].text 안의 JSON을 Map으로 파싱한다.
     */
    public static Map<String, String> parseToMap(String claudeResponse) {
        try {
            // 1) Claude 응답에서 content[0].text 추출
            JsonNode root = mapper.readTree(claudeResponse);
            String text = root.path("content").get(0).path("text").asText();

            // 2) text 안에 있는 JSON 추출 (```json ... ``` 감싸져 있을 수 있음)
            String json = extractJson(text);

            // 3) Map으로 변환
            return mapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            log.error("[Claude] 응답 파싱 실패: {}", e.getMessage());
            return Map.of();
        }
    }

    /**
     * 텍스트에서 JSON 부분만 추출한다.
     * Claude가 ```json ... ``` 으로 감싸서 보내는 경우 처리.
     */
    private static String extractJson(String text) {
        // ```json ... ``` 패턴 제거
        if (text.contains("```json")) {
            int start = text.indexOf("```json") + 7;
            int end = text.indexOf("```", start);
            if (end > start) {
                return text.substring(start, end).trim();
            }
        }
        // ``` ... ``` 패턴 제거
        if (text.contains("```")) {
            int start = text.indexOf("```") + 3;
            int end = text.indexOf("```", start);
            if (end > start) {
                return text.substring(start, end).trim();
            }
        }
        // JSON 그대로인 경우
        return text.trim();
    }
}
