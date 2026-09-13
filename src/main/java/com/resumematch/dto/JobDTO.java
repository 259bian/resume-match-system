package com.resumematch.dto;

import lombok.Data;

@Data
public class JobDTO {

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
}
