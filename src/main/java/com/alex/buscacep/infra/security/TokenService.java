package com.alex.buscacep.infra.security;

import com.alex.buscacep.domain.models.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            log.info("generateToken acionado para o user: {}", user.getUsername());
            String token = JWT.create()
                    .withIssuer("buscacep")
                    .withSubject(user.getUsername())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
            log.info("TokenJWT criado.");
            return token;
        } catch (JWTCreationException e) {
            log.error("Erro ao gerar token");
            throw new RuntimeException("erro ao gerar token", e);
        }
    }

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            log.info("validateToken acionado.");
            return JWT.require(algorithm)
                    .withIssuer("buscacep")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e){
            return "";
        }
    }

    private Instant genExpirationDate(){
        log.info("Instância de genExpirationDate criada.");
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-3"));
    }
}
