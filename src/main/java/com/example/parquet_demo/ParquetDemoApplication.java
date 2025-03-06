package com.example.parquet_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ParquetDemoApplication {

	public static void main(String[] args) {
		System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");
		SpringApplication.run(ParquetDemoApplication.class, args);
	}

}
