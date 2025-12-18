package com.xbk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 启动类
 */
@SpringBootApplication
@MapperScan("com.xbk.mapper")
public class LibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("图书管理系统启动成功!");
        System.out.println("API 文档地址: http://localhost:8080/api/swagger-ui.html");
        System.out.println("数据库类型: MySQL 8.0");
        System.out.println("========================================\n");
    }
}
