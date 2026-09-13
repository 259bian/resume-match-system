package com.resumematch.dto;

import lombok.Data;

@Data
public class ApplicationDTO {

    private Long jobId;
    private Long resumeId;
    private Integer status;
    private String remark;
}
