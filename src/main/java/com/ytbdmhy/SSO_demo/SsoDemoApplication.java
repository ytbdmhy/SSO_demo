package com.ytbdmhy.SSO_demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ytbdmhy.SSO_demo.demo.dao")
public class SsoDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SsoDemoApplication.class, args);
	}

}
