package com.bank.pasive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import javax.annotation.PostConstruct;
import java.util.TimeZone;
@EnableEurekaClient
@SpringBootApplication
public class PasiveApplication {
	public static void main(String[] args) {
		SpringApplication.run(PasiveApplication.class, args);
	}

}
