package com.musinsa.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class ClaimApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClaimApplication.class, args);
	}

}
