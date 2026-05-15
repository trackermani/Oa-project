package com.seanergy.oa.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ValidationItem {

    private final String field;
    private final String documentValue;
    private final String dbValue;
    private final boolean matched;

    public String toAlertMessage() {
        if (matched) {
            return field + ": 일치 (" + documentValue + ")";
        }
        return field + " 불일치: 문서(" + documentValue + ") ≠ DB(" + dbValue + ")";
    }
}
