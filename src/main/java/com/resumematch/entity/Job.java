package com.resumematch.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("job")
public class Job {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;
    private String company;
    private String location;
    private String salary;
    private String experience;
    private String education;

    /** 岗位描述 */
    private String description;

    /** 任职要求 */
    private String requirements;

    /** 所需技能，逗号分隔 */
    private String skills;

    private String category;

    /** 1=在招 0=停招 */
    private Integer status;

    /** 浏览次数 */
    private Integer viewCount;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
