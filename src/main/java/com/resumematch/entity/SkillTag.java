package com.resumematch.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("skill_tag")
public class SkillTag {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    private String category;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
