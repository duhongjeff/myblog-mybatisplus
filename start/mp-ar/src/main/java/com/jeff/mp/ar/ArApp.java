package com.jeff.mp.ar;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.jeff.mp.ar.dao")
public class ArApp {
    public static void main(String[] args) {
        SpringApplication.run(ArApp.class, args);
    }
}
