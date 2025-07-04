package com.timelyMailer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class TimelyMailerApplication {

	public static void main(String[] args) {

		System.out.println("🟢 Starting TimelyMailerApplication");
		SpringApplication.run(TimelyMailerApplication.class, args);
	}

}
