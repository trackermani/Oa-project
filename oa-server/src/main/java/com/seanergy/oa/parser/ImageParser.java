package com.seanergy.oa.parser;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.seanergy.oa.ai.ClaudeResponseParser;
import com.seanergy.oa.ai.ClaudeService;
import com.seanergy.oa.domain.ExtractedDocument;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 이미지 파서 - OCR 없이 Claude Vision에 이미지를 직접 보내서 읽는다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ImageParser implements DocumentParser {

    private final ClaudeService claudeService;

    @Override
    public ExtractedDocument parse(byte[] fileData, String fileName, String promptTemplate) {
        // 1) 파일 확장자로 media type 결정
        String mediaType = getMediaType(fileName);
        log.info("[ImageParser] Claude Vision으로 직접 전송 - 파일: {}, 크기: {}KB", fileName, fileData.length / 1024);

        // 2) Claude Vision에 이미지 직접 전달 (OCR 없음)
        String claudeResponse = claudeService.extractFieldsFromImage(fileData, mediaType, promptTemplate);

        // 3) 응답에서 필드 Map 추출
        Map<String, String> fields = ClaudeResponseParser.parseToMap(claudeResponse);

        return ExtractedDocument.builder()
                .fileName(fileName)
                .extractedFields(fields)
                .build();
    }

    @Override
    public boolean supports(String fileName) {
        String lower = fileName.toLowerCase();
        return lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png");
    }

    private String getMediaType(String fileName) {
        String lower = fileName.toLowerCase();
        if (lower.endsWith(".png")) return "image/png";
        return "image/jpeg";
    }
}
