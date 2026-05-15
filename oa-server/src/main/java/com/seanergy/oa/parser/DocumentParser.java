package com.seanergy.oa.parser;

import com.seanergy.oa.domain.ExtractedDocument;

public interface DocumentParser {

    ExtractedDocument parse(byte[] fileData, String fileName, String promptTemplate);

    boolean supports(String fileName);
}
