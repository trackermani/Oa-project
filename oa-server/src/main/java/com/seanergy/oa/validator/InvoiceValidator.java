package com.seanergy.oa.validator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.seanergy.oa.domain.*;
import com.seanergy.oa.repository.ManagedCompanyRepository;
import com.seanergy.oa.repository.OrderInfoRepository;
import com.seanergy.oa.repository.OrderOilRepository;
import com.seanergy.oa.repository.OrderSettlementRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 인보이스 검증기
 * 검증 항목: 수량(vs BDR, vs DB), 가격, 은행 계좌번호
 * 인보이스는 내부에서 만드는 문서이므로 발행 전 최종 검증 용도
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InvoiceValidator implements DocumentValidator {

    private final OrderInfoRepository orderInfoRepo;
    private final OrderOilRepository orderOilRepo;
    private final OrderSettlementRepository settlementRepo;
    private final ManagedCompanyRepository companyRepo;

    @Override
    public ValidationResult validate(ExtractedDocument document, Long orderInfoId) {
        List<ValidationItem> items = new ArrayList<>();

        OrderInfo order = orderInfoRepo.findById(orderInfoId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다: " + orderInfoId));

        List<OrderOil> oils = orderOilRepo.findByOrderInfoIdAndIsActivatedTrue(orderInfoId);

        // 1) 수량 검증 (인보이스 vs DB)
        String docQty = document.getField("quantity").replaceAll("[^0-9.]", "");
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

        // 2) 가격 검증
        String docPrice = document.getField("unit_price").replaceAll("[^0-9.]", "");
        String dbPrice = oils.stream()
                .map(oil -> oil.getSellPrice() != null ? oil.getSellPrice().toPlainString() : "")
                .findFirst()
                .orElse("");
        items.add(ValidationItem.builder()
                .field("가격")
                .documentValue(docPrice)
                .dbValue(dbPrice)
                .matched(docPrice.equals(dbPrice))
                .build());

        // 3) 은행 계좌번호 검증
        String docAccount = document.getField("account_number").replaceAll("[\\s-]", "");
        // 우리 회사(RelatedAdminCompanyID) 계좌 조회
        ManagedCompany company = companyRepo
                .findByManagedCompanyIdAndIsActivatedTrue(order.getRelatedAdminCompanyId())
                .orElse(null);
        String dbAccount = "";
        if (company != null && company.getBankAccountNum() != null) {
            dbAccount = company.getBankAccountNum().replaceAll("[\\s-]", "");
        }
        items.add(ValidationItem.builder()
                .field("은행 계좌번호")
                .documentValue(docAccount)
                .dbValue(dbAccount)
                .matched(docAccount.equals(dbAccount))
                .build());

        boolean passed = items.stream().allMatch(ValidationItem::isMatched);

        log.info("[인보이스 검증] 주문: {}, 결과: {}", orderInfoId, passed ? "통과" : "불일치 발견");

        return ValidationResult.builder()
                .documentType(DocumentType.INVOICE)
                .orderId(orderInfoId.toString())
                .passed(passed)
                .items(items)
                .validatedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public DocumentType supportedType() {
        return DocumentType.INVOICE;
    }
}
