package com.alex.buscacep.infra.security;
import com.alex.buscacep.infra.repositories.UserRepository;
import com.alex.buscacep.infra.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TokenService service;

    @Autowired
    UserRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        log.info("Recebida requisição para o endpoint: {}", requestURI);

        // Lista de endpoints públicos que não precisam de validação de token
        List<String> publicEndpoints = Arrays.asList("/auth/login", "/auth/register");

        // Se a requisição for para um endpoint público, não validamos o token
        // e simplesmente continuamos a cadeia de filtros.
        if (publicEndpoints.contains(requestURI)) {
            filterChain.doFilter(request, response);
            return; // Essencial para parar a execução do filtro aqui.
        }

        // só será executado para endpoints protegidos.
        var token = this.recoverToken(request);
        log.info("Token recebido para validação: {}", token);

        if (token != null) {
            String username = service.validateToken(token);
            log.info("Validando usuário: {}, no banco de dados.", username);
            UserDetails user = repository.findByUsername(username);

            if (user != null) {
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Usuário {} autenticado com sucesso.", username);
            }
        }
        
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}