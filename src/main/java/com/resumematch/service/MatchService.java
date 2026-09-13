package com.resumematch.service;

import com.resumematch.common.R;
import com.resumematch.dto.MatchRequestDTO;

public interface MatchService {

    R matchResumeToJob(MatchRequestDTO matchRequestDTO);

    R batchMatch(Long resumeId);

    R getMatchHistory(Long userId, Integer page, Integer size);

    R getMatchDetail(Long matchId);
}
