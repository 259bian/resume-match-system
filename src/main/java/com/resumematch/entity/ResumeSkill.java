package com.resumematch.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("resume_skill")
public class ResumeSkill {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long resumeId;
    private Long skillId;
    private String skillName;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
