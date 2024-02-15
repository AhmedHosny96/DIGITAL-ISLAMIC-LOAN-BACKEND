package com.sahay;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

import java.time.LocalTime;

@SpringBootApplication
public class DigitalLoanApplication {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

//    @Bean
//    public JdbcTemplate jdbcTemplate() {
//        return new JdbcTemplate();
//    }

    public static void main(String[] args) {
        SpringApplication.run(DigitalLoanApplication.class, args);


        LocalTime currentTime = LocalTime.now();

        // Print the current time in default format
        System.out.println("Current time (default format): " + currentTime);

        // Print only the hour and minute components

    }

}
