package com.resumematch.service;

import com.resumematch.common.R;

public interface AiService {

    R optimizeResume(Long resumeId);

    R polishText(String text);

    R extractKeywords(Long resumeId);

    R extractCategorizedSkills(Long resumeId);

    R checkSimilarity(Long resumeId);
}
