package com.everis.debitcardafiliation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class DebitCardAfiliationApplication {

	public static void main(String[] args) {
		SpringApplication.run(DebitCardAfiliationApplication.class, args);
	}

}
