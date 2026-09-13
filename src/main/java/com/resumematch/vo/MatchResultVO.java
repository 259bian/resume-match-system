package com.resumematch.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MatchResultVO {

    private Long id;
    private Long resumeId;
    private Long jobId;
    private String jobTitle;
    private String jobName;
    private String company;
    private String companyName;
    private String location;
    private String salary;
    private String salaryDisplay;
    private Integer score;
    private Integer matchScore;
    private String detail;
    private String suggestion;
    private List<String> skillMatch;
    private JobVO job;
    private LocalDateTime createTime;

    public Integer getMatchScore() {
        return matchScore != null ? matchScore : score;
    }

    public String getCompanyName() {
        return companyName != null ? companyName : company;
    }

    public String getJobName() {
        return jobName != null ? jobName : jobTitle;
    }

    public String getSalaryDisplay() {
        return salaryDisplay != null ? salaryDisplay : salary;
    }
}
