package com.everis.debitcardafiliation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
@Slf4j
public class DebitCardAfiliationApplication {

	public static void main(String[] args) {
		SpringApplication.run(DebitCardAfiliationApplication.class, args);
		log.info("Servicio de tarjetas de débito activado.");
	}

}
