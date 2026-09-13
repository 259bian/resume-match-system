package com.resumematch.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("resume")
public class Resume {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;
    private String title;
    private String fileName;
    private String fileUrl;
    private Long fileSize;
    private String fileType;

    /** 解析后的文本内容 */
    private String content;

    /** 当前版本号 */
    private Integer currentVersion;

    /** 是否公开 */
    private Integer isPublic;

    /** 匹配分数 */
    private Integer matchScore;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
