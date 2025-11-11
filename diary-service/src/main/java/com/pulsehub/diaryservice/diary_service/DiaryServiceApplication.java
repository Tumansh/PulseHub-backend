package com.pulsehub.diaryservice.diary_service;

import com.pulsehub.commonlib.common_lib.security.JwtUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(JwtUtil.class)
public class DiaryServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(DiaryServiceApplication.class, args);
	}
}
