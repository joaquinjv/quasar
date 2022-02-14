package com.ml.quasar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class }, scanBasePackages="quasar")
public class QuasarApplication {
	
	public static void main(String[] args) {
		
		SpringApplication.run(QuasarApplication.class, args);
		
	}

}
 