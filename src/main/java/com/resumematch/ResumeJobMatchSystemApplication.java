package com.resumematch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.resumematch.mapper")
public class ResumeJobMatchSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResumeJobMatchSystemApplication.class, args);
        System.out.println("========================================");
        System.out.println("  简历优化与岗位智能匹配系统 启动成功！");
        System.out.println("  访问地址: http://localhost:8080");
        System.out.println("========================================");
    }
}
