package com.seanergy.oa.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.seanergy.oa.domain.DocumentType;
import com.seanergy.oa.domain.ValidationResult;
import com.seanergy.oa.pipeline.DocumentPipeline;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentPipeline pipeline;

    /**
     * 서류 파일을 업로드하여 검증을 요청한다.
     *
     * POST /api/documents/validate
     * - file: PDF 또는 이미지 파일
     * - type: NOMINATION / INVOICE / BDR
     * - orderId: 주문 ID (OrderInfoID)
     *
     * 검증 통과 시 → passed: true (자동 발송 가능)
     * 검증 실패 시 → passed: false + 불일치 항목 + 담당자 알림
     */
    @PostMapping("/validate")
    public ResponseEntity<ValidationResult> validateDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") DocumentType documentType,
            @RequestParam("orderId") Long orderId) throws Exception {

        ValidationResult result = pipeline.process(
                file.getBytes(),
                file.getOriginalFilename(),
                documentType,
                orderId
        );

        return ResponseEntity.ok(result);
    }
}
