package com.seanergy.oa.notification;

import org.springframework.stereotype.Service;

import com.seanergy.oa.domain.ValidationResult;

import lombok.extern.slf4j.Slf4j;

/**
 * 검증 실패 시 담당자에게 알림을 발송한다.
 * Phase 1: 로그 출력
 * Phase 2: 슬랙/카톡/이메일 등 실제 알림 채널 연동
 */
@Slf4j
@Service
public class NotificationService {

    public void notifyMismatch(ValidationResult result) {
        log.warn("══════════════════════════════════════");
        log.warn("  [알림] 서류 검증 실패 - 발송 차단됨");
        log.warn("  문서 유형: {}", result.getDocumentType());
        log.warn("  주문 번호: {}", result.getOrderId());
        log.warn("  검증 시간: {}", result.getValidatedAt());
        log.warn("──────────────────────────────────────");

        result.getItems().stream()
                .filter(item -> !item.isMatched())
                .forEach(item -> log.warn("  ❌ {}", item.toAlertMessage()));

        result.getItems().stream()
                .filter(item -> item.isMatched())
                .forEach(item -> log.info("  ✅ {}", item.toAlertMessage()));

        log.warn("══════════════════════════════════════");

        // TODO Phase 2: 실제 알림 발송
        // - 슬랙 웹훅
        // - 카카오톡 알림톡
        // - 이메일 발송
    }
}
