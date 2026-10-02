package com.microservices.quiz_service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@EnableFeignClients
public class QuizServiceApplication {

    @Value("${server.port}")
    private String port;

	public static void main(String[] args) {

		SpringApplication.run(QuizServiceApplication.class, args);
	}
    @EventListener(ApplicationReadyEvent.class)
    public void applicationReady() {
        System.out.println("Listening on port http://localhost:" + port);
    }

}
