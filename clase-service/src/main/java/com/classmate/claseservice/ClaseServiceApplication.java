package com.classmate.claseservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ClaseServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClaseServiceApplication.class, args);
	}

}
