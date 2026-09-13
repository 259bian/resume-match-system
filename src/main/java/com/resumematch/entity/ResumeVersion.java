package com.resumematch.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("resume_version")
public class ResumeVersion {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long resumeId;
    private Integer version;
    private String fileName;
    private String fileUrl;
    private String content;

    /** 1=当前版本 0=历史版本 */
    private Integer isCurrent;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableLogic
    private Integer deleted;
}
