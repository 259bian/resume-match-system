package com.resumematch.service;

import com.resumematch.common.R;
import com.resumematch.dto.ResumeDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ResumeService {

    R uploadResume(Long userId, MultipartFile file, String title);

    R listMyResumes(Long userId, Integer page, Integer size);

    R getResumeDetail(Long resumeId);

    R updateResume(ResumeDTO resumeDTO);

    R deleteResume(Long resumeId);

    R uploadNewVersion(Long resumeId, MultipartFile file);

    R saveOptimizedVersion(Long resumeId, String content);

    R getVersions(Long resumeId);

    R switchVersion(Long resumeId, Integer version);

    R checkSimilarity(Long resumeId);

    R listAllResumes(Integer page, Integer size);

    R exportResumes(HttpServletResponse response);
}
