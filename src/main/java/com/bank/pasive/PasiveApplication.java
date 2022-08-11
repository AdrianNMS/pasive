package com.bank.pasive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class PasiveApplication {
	public static void main(String[] args) {
		SpringApplication.run(PasiveApplication.class, args);
	}

}
