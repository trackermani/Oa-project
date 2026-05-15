package com.seanergy.oa.validator;

import com.seanergy.oa.domain.DocumentType;
import com.seanergy.oa.domain.ExtractedDocument;
import com.seanergy.oa.domain.ValidationResult;

public interface DocumentValidator {

    ValidationResult validate(ExtractedDocument document, Long orderInfoId);

    DocumentType supportedType();
}
