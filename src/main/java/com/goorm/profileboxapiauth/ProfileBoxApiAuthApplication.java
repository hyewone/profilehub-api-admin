package com.goorm.profileboxapiauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ComponentScan("com.goorm")
@SpringBootApplication
public class ProfileBoxApiAuthApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProfileBoxApiAuthApplication.class, args);
	}

}
