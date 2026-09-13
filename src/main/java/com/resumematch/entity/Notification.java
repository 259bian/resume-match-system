package com.resumematch.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("notification")
public class Notification {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;
    private String title;
    private String content;

    /** 1=投递成功 2=面试通知 3=已拒绝 4=系统消息 */
    private Integer type;

    /** 0=未读 1=已读 */
    private Integer isRead;

    private Long relatedId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableLogic
    private Integer deleted;
}
