package com.alex.buscacep;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;

@OpenAPIDefinition(
		info = @Info(
				title = "API BuscaEndereco",
				version = "1.0",
				description = "Faça login ou registre-se para acessar qualquer endereço do Brasil.",
				contact = @Contact(name = "Alex Ritter", email = "ritter.alex@hotmail.com")
		),
		security = @SecurityRequirement(name = "bearerAuth") // Adicione esta linha
)
@SecurityScheme(
		name = "bearerAuth",
		type = SecuritySchemeType.HTTP,
		scheme = "bearer",
		bearerFormat = "JWT", // opcional, apenas para informar o formato do token
		in = SecuritySchemeIn.HEADER
)
@SpringBootApplication
@EnableFeignClients
public class BuscacepApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuscacepApplication.class, args);
	}

}
