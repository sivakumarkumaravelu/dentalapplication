package com.ib.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DentalApplication {
	private static final Logger logger = LoggerFactory.getLogger(DentalApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DentalApplication.class, args);
        logger.info("DentalApplication.main(): Dental application started successfully");
	}

}
