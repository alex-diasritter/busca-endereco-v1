package com.alex.buscacep.controller;
import com.alex.buscacep.domain.dtos.response.BuscaEnderecoResponseDTO;
import com.alex.buscacep.domain.dtos.response.BuscaResponseDTO;
import com.alex.buscacep.domain.models.User;
import com.alex.buscacep.infra.service.BuscaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping(value = "/buscas")
public class BuscaController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BuscaService service;


    @Operation(summary = "Rota responsável por acessar o db e retornar o histórico de buscas realizadas")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Busca encontrado com sucesso",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BuscaEnderecoResponseDTO.class)
                            )
                    }
            )
    })
    @GetMapping
    public ResponseEntity<List<BuscaResponseDTO>> enderecosBuscados(Authentication authentication){
        log.info("Requisição para listar histórico de buscas registradas no db com token para autenticar usuário.");
        User usuario = (User) authentication.getPrincipal();
        log.info("Usuário autenticado com sucesso.");
        return ResponseEntity.ok(service.findAll(usuario));
    }
}
