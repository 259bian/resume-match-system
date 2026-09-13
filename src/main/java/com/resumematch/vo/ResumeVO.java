package com.resumematch.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ResumeVO {

    private Long id;
    private Long userId;
    private String userName;
    private String title;
    private String fileName;
    private String fileUrl;
    private Long fileSize;
    private String fileType;
    private String content;
    private Integer currentVersion;
    private Integer isPublic;
    private Integer matchScore;
    private List<String> skills;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @JsonProperty("version")
    public Integer getVersion() {
        return currentVersion;
    }

    @JsonProperty("uploadTime")
    public LocalDateTime getUploadTime() {
        return createTime;
    }
}
