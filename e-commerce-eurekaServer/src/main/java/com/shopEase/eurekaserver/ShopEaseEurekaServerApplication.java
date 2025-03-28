package com.shopEase.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ShopEaseEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopEaseEurekaServerApplication.class, args);
		System.out.println("Eureka server running.......");
	}

}
