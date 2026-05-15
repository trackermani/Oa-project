package com.seanergy.oa.domain;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExtractedDocument {

    private final DocumentType type;
    private final String fileName;
    private final Map<String, String> extractedFields;

    public String getField(String key) {
        return extractedFields.getOrDefault(key, "");
    }
}
