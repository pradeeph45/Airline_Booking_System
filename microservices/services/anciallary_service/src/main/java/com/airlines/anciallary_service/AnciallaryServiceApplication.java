package com.airlines.anciallary_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AnciallaryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnciallaryServiceApplication.class, args);
	}

}
