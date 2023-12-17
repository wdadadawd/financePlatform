package com.shxt.financePlatform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@MapperScan("com.shxt.financePlatform.mapper")
@SpringBootApplication
public class FinancePlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinancePlatformApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
