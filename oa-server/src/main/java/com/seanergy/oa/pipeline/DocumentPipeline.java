package com.seanergy.oa.pipeline;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.seanergy.oa.domain.DocumentType;
import com.seanergy.oa.domain.ExtractedDocument;
import com.seanergy.oa.domain.ValidationResult;
import com.seanergy.oa.notification.NotificationService;
import com.seanergy.oa.parser.DocumentParser;
import com.seanergy.oa.validator.DocumentValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 전체 파이프라인을 조립하는 핵심 서비스.
 *
 * 흐름: 파일 → 파서 → Claude 추출 → DB 대조 검증 → 통과/차단
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentPipeline {

    private final List<DocumentParser> parsers;
    private final List<DocumentValidator> validators;
    private final NotificationService notificationService;

    /**
     * 문서를 검증하고 결과를 반환한다.
     *
     * @param fileData    파일 바이너리
     * @param fileName    파일명 (확장자로 파서 결정)
     * @param docType     문서 유형 (NOMINATION, INVOICE, BDR)
     * @param orderInfoId 주문 ID
     * @return 검증 결과
     */
    public ValidationResult process(byte[] fileData, String fileName, DocumentType docType, Long orderInfoId) {

        log.info("========================================");
        log.info("[파이프라인 시작] 문서: {}, 유형: {}, 주문: {}", fileName, docType, orderInfoId);

        // 1단계: 파서 선택
        DocumentParser parser = parsers.stream()
                .filter(p -> p.supports(fileName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("지원하지 않는 파일 형식: " + fileName));

        // 2단계: 프롬프트 로드
        String prompt = loadPrompt(docType);

        // 3단계: 파싱 + Claude 추출
        log.info("[2/4] 문서 파싱 및 AI 추출 중...");
        ExtractedDocument extracted = parser.parse(fileData, fileName, prompt);
        log.info("[2/4] 추출 완료 - 필드: {}", extracted.getExtractedFields());

        // 4단계: 검증
        log.info("[3/4] DB 대조 검증 중...");
        DocumentValidator validator = validators.stream()
                .filter(v -> v.supportedType() == docType)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("지원하지 않는 문서 유형: " + docType));

        ValidationResult result = validator.validate(extracted, orderInfoId);

        // 5단계: 결과 처리
        if (result.isPassed()) {
            log.info("[4/4] ✅ 검증 통과 → 자동 발송 허용");
        } else {
            log.warn("[4/4] ❌ 검증 실패 → 발송 차단, 담당자 알림");
            notificationService.notifyMismatch(result);
        }

        log.info("[파이프라인 종료]");
        log.info("========================================");

        return result;
    }

    /**
     * 문서 유형에 맞는 프롬프트 템플릿을 로드한다.
     */
    private String loadPrompt(DocumentType docType) {
        String path = switch (docType) {
            case NOMINATION -> "prompts/nomination-extract.txt";
            case INVOICE -> "prompts/invoice-extract.txt";
            case BDR -> "prompts/bdr-extract.txt";
        };

        try {
            ClassPathResource resource = new ClassPathResource(path);
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("프롬프트 로드 실패: " + path, e);
        }
    }
}
