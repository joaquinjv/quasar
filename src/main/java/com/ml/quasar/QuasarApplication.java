package com.ml.quasar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@RestController
public class QuasarApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuasarApplication.class, args);
	}

	@GetMapping("/")
	public String hello() {
		return "Hello world!";
	}

}