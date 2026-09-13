package com.resumematch.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("application")
public class Application {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;
    private Long jobId;
    private Long resumeId;

    /** 1=待查看 2=已查看 3=面试通知 4=已拒绝 5=已录取 */
    private Integer status;

    /** 匹配分数 */
    private Integer matchScore;

    /** 管理员备注 */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
