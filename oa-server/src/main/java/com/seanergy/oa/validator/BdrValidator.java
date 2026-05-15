package com.seanergy.oa.validator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.seanergy.oa.domain.*;
import com.seanergy.oa.repository.OrderOilRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * BDR(Bunker Delivery Receipt) 검증기
 * 검증 항목: 수량
 * BDR은 공급사에서 보내주는 외부 문서 (0% 신뢰)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BdrValidator implements DocumentValidator {

    private final OrderOilRepository orderOilRepo;

    @Override
    public ValidationResult validate(ExtractedDocument document, Long orderInfoId) {
        List<ValidationItem> items = new ArrayList<>();

        List<OrderOil> oils = orderOilRepo.findByOrderInfoIdAndIsActivatedTrue(orderInfoId);

        // 수량 검증 (BDR 공급수량 vs DB 주문수량)
        String docQty = document.getField("quantity_delivered").replaceAll("[^0-9.]", "");
        String dbQty = oils.stream()
                .map(oil -> oil.getDecideAmount() != null ? oil.getDecideAmount().toPlainString() : "")
                .findFirst()
                .orElse("");
        items.add(ValidationItem.builder()
                .field("수량")
                .documentValue(docQty)
                .dbValue(dbQty)
                .matched(docQty.equals(dbQty))
                .build());

        boolean passed = items.stream().allMatch(ValidationItem::isMatched);

        log.info("[BDR 검증] 주문: {}, 결과: {}", orderInfoId, passed ? "통과" : "불일치 발견");

        return ValidationResult.builder()
                .documentType(DocumentType.BDR)
                .orderId(orderInfoId.toString())
                .passed(passed)
                .items(items)
                .validatedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public DocumentType supportedType() {
        return DocumentType.BDR;
    }
}
