package com.alex.buscacep;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "API BuscaEndereco",
				version = "1.0",
				description = "Faça login ou registre-se para acessar qualquer endereço do Brasil.",
				contact = @Contact(name = "Alex Ritter", email = "ritter.alex@hotmail.com")
		)
)
@SpringBootApplication
public class BuscacepApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuscacepApplication.class, args);
	}

}
