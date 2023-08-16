package com.goorm.profileboxapiadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ComponentScan("com.goorm")
@SpringBootApplication
public class ProfileBoxApiAdminApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProfileBoxApiAdminApplication.class, args);
	}

}
