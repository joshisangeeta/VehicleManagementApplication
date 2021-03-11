package com.project.vm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VmApplication {

	static final Logger log = LoggerFactory.getLogger(VmApplication.class);
	
	public static void main(String[] args) {
		log.info("Starting application");
		SpringApplication.run(VmApplication.class, args);
	}

}
