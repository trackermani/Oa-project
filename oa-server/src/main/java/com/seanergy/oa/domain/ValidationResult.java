package com.seanergy.oa.domain;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ValidationResult {

    private final DocumentType documentType;
    private final String orderId;
    private final boolean passed;
    private final List<ValidationItem> items;
    private final LocalDateTime validatedAt;

    public boolean hasMismatch() {
        return items.stream().anyMatch(item -> !item.isMatched());
    }
}
