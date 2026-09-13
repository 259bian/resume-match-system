package com.resumematch.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ApplicationVO {

    private Long id;
    private Long userId;
    private String userName;
    private Long jobId;
    private Long resumeId;

    private String jobTitle;
    private String jobName;
    private String companyName;
    private String company;
    private String resumeTitle;
    private String resumeName;

    private Integer status;
    private Integer matchScore;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime applyTime;

    @JsonProperty("jobName")
    public String getJobName() {
        return jobName != null ? jobName : jobTitle;
    }

    @JsonProperty("company")
    public String getCompany() {
        return company != null ? company : companyName;
    }

    @JsonProperty("resumeName")
    public String getResumeName() {
        return resumeName != null ? resumeName : resumeTitle;
    }

    @JsonProperty("applyTime")
    public LocalDateTime getApplyTime() {
        return applyTime != null ? applyTime : createTime;
    }
}
