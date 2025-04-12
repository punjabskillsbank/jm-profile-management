package com.jobmatrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.jobmatrix", "com.common.entity"})
public class JmProfileManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(JmProfileManagementApplication.class, args);
	}

}