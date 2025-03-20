package com.alex.buscacep.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(c -> c.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                //aqui definimos que tipo de requisições cada tipo de usuário pode fazer
                .authorizeHttpRequests(ahr -> ahr.requestMatchers(HttpMethod.DELETE,"/buscacep/{cep}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/buscacep").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/buscacep/{cep}").hasRole("USER")
                        .anyRequest().authenticated())
                .build();

    }

}
