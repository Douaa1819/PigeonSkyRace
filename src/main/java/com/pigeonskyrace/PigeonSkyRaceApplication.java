package com.pigeonskyrace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.pigeonskyrace")
public class PigeonSkyRaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PigeonSkyRaceApplication.class, args);
	}
}
