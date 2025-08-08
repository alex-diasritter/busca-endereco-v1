package com.alex.buscacep.controller;
import com.alex.buscacep.infra.service.LogReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/report")
@Tag(
        name = "Relatório de Logs",
        description = "Operações para geração de relatórios analíticos a partir dos logs da aplicação"
)
public class LogReportController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final LogReportService logService;

    public LogReportController(LogReportService logService) {
        this.logService = logService;
    }

    @Operation(
            summary = "Gerar relatório de logs",
            description = "Gera um relatório analítico com estatísticas sobre os logs da aplicação",
            parameters = {
                    @Parameter(
                            name = "date",
                            description = "Data para filtrar os logs (formato ISO: AAAA-MM-DD). Se não informado, retorna todos os logs.",
                            example = "2023-05-22"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Relatório gerado com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "exemplo-sucesso",
                                    value = "{\"logFile\":\"logs/application.log\",\"totalEntries\":1834,\"levels\":{\"INFO\":1250,\"ERROR\":42,\"WARN\":38,\"DEBUG\":504},\"dailyEntries\":{\"2023-05-20\":420,\"2023-05-21\":650,\"2023-05-22\":764},\"lastErrors\":[\"2023-05-22 14:32:10.123 [http-nio-8080-exec-5] ERROR c.e.s.PaymentService - Transaction failed\"],\"generatedAt\":\"2023-05-22T15:30:45.123Z\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Data inválida",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "exemplo-data-invalida",
                                    value = "{\"status\":\"error\",\"message\":\"Formato de data inválido. Use o formato AAAA-MM-DD\",\"timestamp\":\"2023-05-22\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno ao processar relatório",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "exemplo-erro",
                                    value = "{\"status\":\"error\",\"message\":\"Falha ao gerar relatório: Arquivo de log não encontrado\",\"timestamp\":\"2023-05-22\"}"
                            )
                    )
            )
    })
    @GetMapping(value = "/logs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> generateLogReport(
            @Parameter(
                    description = "Data para filtro (opcional)",
                    example = "2023-05-22"
            )
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        try {
            Map<String, Object> report = logService.generateLogReport(date);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of(
                            "status", "error",
                            "message", "Falha ao gerar relatório: " + e.getMessage(),
                            "timestamp", LocalDate.now()
                    )
            );
        }
    }
}