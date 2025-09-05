package com.jobmatrix;

import com.common.config.AwsConfig;
import com.common.config.CorsConfig;
import com.common.config.S3Config;
import com.common.util.S3PresignedURLUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({CorsConfig.class, AwsConfig.class, S3Config.class, S3PresignedURLUtil.class})
@EntityScan(basePackages = {"com.common.entity"})
public class JmProfileManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(JmProfileManagementApplication.class, args);
	}

}