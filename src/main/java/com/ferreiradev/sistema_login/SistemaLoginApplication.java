package com.ferreiradev.sistema_login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SistemaLoginApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaLoginApplication.class, args);
	}

}
