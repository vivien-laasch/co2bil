package de.vlaasch.co2bil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("de.vlaasch")
public class Co2Bil {

	public static void main(String[] args) {
		SpringApplication.run(Co2Bil.class, args);
	}

}
