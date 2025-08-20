package com.alex.service_discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/*
Essa aplicação simples contendo a dependencia Eureka Server  obe o servidor Spring Eureka.
As outras apliações usando depen. Eureka Client.
O Eureka fará a roteamento entre o Gateway e os microsserviços.
gateway -> eureka -> serviços
*/

@SpringBootApplication
@EnableEurekaServer
public class ServiceDiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceDiscoveryApplication.class, args);
	}

}
