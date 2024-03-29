package com.devz.ventlon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableMongoRepositories
@EnableWebFlux
public class VentlonApplication {

	public static void main(String[] args) {
		SpringApplication.run(VentlonApplication.class, args);
	}

}
