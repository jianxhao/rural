package com.rural;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.rural.mapper")
public class WxRuralApplication {

	public static void main(String[] args) {
		SpringApplication.run(WxRuralApplication.class, args);
	}

}
