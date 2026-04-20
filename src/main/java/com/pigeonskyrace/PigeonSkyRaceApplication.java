package com.pigeonskyrace;

import com.pigeonskyrace.security.CorsProperties;
import com.pigeonskyrace.security.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = "com.pigeonskyrace")
@EnableConfigurationProperties({JwtProperties.class, CorsProperties.class})
public class PigeonSkyRaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PigeonSkyRaceApplication.class, args);
	}
}
