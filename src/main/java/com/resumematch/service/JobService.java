package com.resumematch.service;

import com.resumematch.common.R;
import com.resumematch.dto.JobDTO;

public interface JobService {
    R createJob(JobDTO jobDTO);
    R updateJob(Long id, JobDTO jobDTO);
    R deleteJob(Long id);
    R getJobDetail(Long jobId, Long userId);
    R listJobs(Integer page, Integer size, String keyword, String category, String location);
    R getAllJobs();
}