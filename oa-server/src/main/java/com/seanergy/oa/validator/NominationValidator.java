package com.seanergy.oa.validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.seanergy.oa.domain.*;
import com.seanergy.oa.repository.OrderInfoRepository;
import com.seanergy.oa.repository.OrderOilRepository;
import com.seanergy.oa.repository.ShipRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 노미네이션 컨펌 검증기
 * 검증 항목: 선명, 포트, 공급일정, 수량
 * 비교 대상: DB 주문등록 데이터 (100% 신뢰)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NominationValidator implements DocumentValidator {

    private final OrderInfoRepository orderInfoRepo;
    private final OrderOilRepository orderOilRepo;
    private final ShipRepository shipRepo;

    @Override
    public ValidationResult validate(ExtractedDocument document, Long orderInfoId) {
        List<ValidationItem> items = new ArrayList<>();

        // DB에서 주문 데이터 조회
        OrderInfo order = orderInfoRepo.findById(orderInfoId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다: " + orderInfoId));

        // 1) 선명 검증
        if (order.getShipId() != null) {
            Ship ship = shipRepo.findById(order.getShipId()).orElse(null);
            if (ship != null) {
                String docVessel = document.getField("vessel_name").trim();
                String dbVessel = ship.getShipName().trim();
                items.add(ValidationItem.builder()
                        .field("선명")
                        .documentValue(docVessel)
                        .dbValue(dbVessel)
                        .matched(docVessel.equalsIgnoreCase(dbVessel))
                        .build());
            }
        }

        // 2) 포트 검증
        String docPort = document.getField("port").trim();
        String dbPort = order.getPortName().trim();
        items.add(ValidationItem.builder()
                .field("포트")
                .documentValue(docPort)
                .dbValue(dbPort)
                .matched(docPort.equalsIgnoreCase(dbPort))
                .build());

        // 3) 공급일정 검증
        String docDate = document.getField("supply_date").trim();
        String dbDate = formatDate(order.getEstSupplyStartDate());
        items.add(ValidationItem.builder()
                .field("공급일정")
                .documentValue(docDate)
                .dbValue(dbDate)
                .matched(docDate.equals(dbDate))
                .build());

        // 4) 수량 검증
        List<OrderOil> oils = orderOilRepo.findByOrderInfoIdAndIsActivatedTrue(orderInfoId);
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

        boolean passed = items.stream().allMatch(ValidationItem::isMatched);

        log.info("[노미컨펌 검증] 주문: {}, 결과: {}", orderInfoId, passed ? "통과" : "불일치 발견");

        return ValidationResult.builder()
                .documentType(DocumentType.NOMINATION)
                .orderId(orderInfoId.toString())
                .passed(passed)
                .items(items)
                .validatedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public DocumentType supportedType() {
        return DocumentType.NOMINATION;
    }

    private String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
