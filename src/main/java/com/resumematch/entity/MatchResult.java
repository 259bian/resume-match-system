package com.resumematch.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("match_result")
public class MatchResult {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long resumeId;
    private Long jobId;
    private Long userId;

    /** 匹配分数 0-100 */
    private Integer score;

    /** 匹配详情 JSON */
    private String detail;

    /** AI分析建议 */
    private String suggestion;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
