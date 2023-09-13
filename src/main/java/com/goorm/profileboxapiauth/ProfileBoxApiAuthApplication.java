package com.goorm.profileboxapiauth;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan("com.goorm")
public class ProfileBoxApiAuthApplication {

	@Bean
	public RestTemplate getRestTemplate(){
		return new RestTemplate();
	}

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
//				.components(components)
//				.addSecurityItem(securityRequirement)
				.info(new Info()
						.title("ProfileHub Auth API")
						.version("1.0.0")
						.description("ProfileHub API")
						.contact(new Contact().name("hyewone").email("hyeneeOh@gmail.com")));
	}

	public static void main(String[] args) {
		SpringApplication.run(ProfileBoxApiAuthApplication.class, args);
	}

}
