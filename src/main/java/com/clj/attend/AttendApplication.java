package com.clj.attend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.clj.attend.mapper")

public class AttendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AttendApplication.class, args);
    }

}
