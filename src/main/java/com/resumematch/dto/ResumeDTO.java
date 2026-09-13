package com.resumematch.dto;

import lombok.Data;

@Data
public class ResumeDTO {

    private Long id;
    private String title;
    private String content;
    private Integer isPublic;
}
