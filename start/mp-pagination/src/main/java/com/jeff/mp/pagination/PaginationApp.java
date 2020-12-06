package com.jeff.mp.pagination;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.jeff.mp.pagination.dao")
public class PaginationApp {
    public static void main(String[] args) {
        SpringApplication.run(PaginationApp.class, args);
    }
}
