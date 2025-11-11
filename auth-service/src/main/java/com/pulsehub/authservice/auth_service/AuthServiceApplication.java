package com.pulsehub.authservice.auth_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import com.pulsehub.commonlib.common_lib.security.JwtUtil;

@SpringBootApplication
@Import(JwtUtil.class)
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

}
