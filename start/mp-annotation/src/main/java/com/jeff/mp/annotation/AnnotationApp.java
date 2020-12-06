package com.jeff.mp.annotation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.jeff.mp.annotation.dao")
public class AnnotationApp {
    public static void main(String[] args) {
        SpringApplication.run(AnnotationApp.class, args);
    }
}
