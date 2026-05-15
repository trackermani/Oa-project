package com.seanergy.oa.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import com.seanergy.oa.ai.ClaudeResponseParser;
import com.seanergy.oa.ai.ClaudeService;
import com.seanergy.oa.domain.ExtractedDocument;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PdfParser implements DocumentParser {

    private final ClaudeService claudeService;

    @Override
    public ExtractedDocument parse(byte[] fileData, String fileName, String promptTemplate) {
        // 1) PDF에서 텍스트 추출
        String rawText = extractText(fileData);
        log.info("[PDF] 텍스트 추출 완료 - 파일: {}, 길이: {}자", fileName, rawText.length());

        // 2) Claude에 전달하여 구조화된 데이터 추출
        String claudeResponse = claudeService.extractFields(rawText, promptTemplate);

        // 3) 응답에서 필드 Map 추출
        Map<String, String> fields = ClaudeResponseParser.parseToMap(claudeResponse);

        return ExtractedDocument.builder()
                .fileName(fileName)
                .extractedFields(fields)
                .build();
    }

    @Override
    public boolean supports(String fileName) {
        return fileName.toLowerCase().endsWith(".pdf");
    }

    private String extractText(byte[] fileData) {
        try (PDDocument document = Loader.loadPDF(new ByteArrayInputStream(fileData).readAllBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (IOException e) {
            log.error("[PDF] 텍스트 추출 실패: {}", e.getMessage());
            throw new RuntimeException("PDF 파싱 실패", e);
        }
    }
}
