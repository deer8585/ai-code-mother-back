package com.wyb.aicodemotherback;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wyb.aicodemotherback.mapper")
public class AiCodeMotherBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiCodeMotherBackApplication.class, args);
    }

}
