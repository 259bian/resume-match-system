package com.resumematch.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class JobVO {

    private Long id;
    private String title;
    private String company;
    private String location;
    private String salary;
    private String experience;
    private String education;
    private String description;
    private String requirements;
    private String skills;
    private String category;
    private Integer status;
    private Integer viewCount;
    private Boolean isFavorite;
    private LocalDateTime createTime;
}
