package com.microservices.registry_service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@EnableEurekaServer
public class RegistryServiceApplication {

    @Value("${server.port}")
    private String port;

    public static void main(String[] args) {
        SpringApplication.run(RegistryServiceApplication.class, args);
    }

        @EventListener(ApplicationReadyEvent.class)
        public void applicationReady() {
            System.out.println("Listening on port http://localhost:" + port);
        }
	}

