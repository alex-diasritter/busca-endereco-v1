package com.alex.buscacep.controller;

import com.alex.buscacep.domain.dtos.response.UserDTO;
import com.alex.buscacep.domain.models.User;
import com.alex.buscacep.domain.dtos.response.LoginResponseDTO;
import com.alex.buscacep.infra.security.TokenService;
import com.alex.buscacep.infra.service.AuthorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {

    @Autowired
    TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthorizationService authorizationService;

    @Operation(summary = "Rota responsável pelo login de usuários")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário logado com sucesso",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = User.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = {
                            @Content(
                                    mediaType = "application/json"
                            )
                    }
            )
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid User data){
        var login = new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword());
        var auth = this.authenticationManager.authenticate(login);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @Operation(summary = "Rota responsável pelo cadastro de usuários")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário cadastrado com sucesso",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = User.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Informação inválida",
                    content = {
                            @Content(
                                    mediaType = "application/json"
                            )
                    }
            )
    })
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid User data){
        var user = authorizationService.register(data);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Rota responsável por listar usuários do sistema")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de usuários retornada",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = User.class)
                            )
                    }
            )})
    @GetMapping("/users")
    public ResponseEntity<Page<UserDTO>> list(Pageable pageable){
        return ResponseEntity.ok(authorizationService.findAll(pageable));
    }

    /*Está retornando 403
    @DeleteMapping("/{username}")
    public ResponseEntity delete(@PathVariable String username){
        var result = authorizationService.delete(username);
        if (result) return ResponseEntity.ok().build();
        return ResponseEntity.noContent().build();
    }

     */

}