package com.resumematch.vo;

import lombok.Data;
import java.util.Map;

@Data
public class StatisticsVO {

    private long totalUsers;
    private long totalResumes;
    private long totalJobs;
    private long totalApplications;
    private long todayApplications;
    private double matchSuccessRate;
    private Map<String, Long> applicationStatusDistribution;
    private Map<String, Long> jobCategoryDistribution;
    private Map<String, Long> dailyApplicationTrend;
}
