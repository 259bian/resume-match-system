package com.resumematch.common;

public class Constants {

    /** 用户角色 */
    public static final int ROLE_USER = 0;
    public static final int ROLE_ADMIN = 1;

    /** 投递状态 */
    public static final int APPLICATION_PENDING = 1;
    public static final int APPLICATION_VIEWED = 2;
    public static final int APPLICATION_INTERVIEW = 3;
    public static final int APPLICATION_REJECTED = 4;
    public static final int APPLICATION_ACCEPTED = 5;

    /** 消息类型 */
    public static final int MSG_APPLY_SUCCESS = 1;
    public static final int MSG_INTERVIEW_NOTICE = 2;
    public static final int MSG_REJECTED = 3;
    public static final int MSG_SYSTEM = 4;

    /** 消息状态 */
    public static final int MSG_UNREAD = 0;
    public static final int MSG_READ = 1;

    /** 简历版本状态 */
    public static final int VERSION_CURRENT = 1;
    public static final int VERSION_HISTORY = 0;

    /** 公开/非公开 */
    public static final int PUBLIC = 1;
    public static final int PRIVATE = 0;

    /** JWT请求头 */
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    /** 文件类型 */
    public static final String FILE_TYPE_PDF = "pdf";
    public static final String FILE_TYPE_DOC = "doc";
    public static final String FILE_TYPE_DOCX = "docx";
    public static final String FILE_TYPE_IMAGE = "image";
}
