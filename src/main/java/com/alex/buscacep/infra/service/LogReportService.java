package com.alex.buscacep.infra.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LogReportService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${logging.file.name:logs/app.log}")
    private String logFilePath;

    public Map<String, Object> generateLogReport(LocalDate filterDate) throws IOException {
        Path path = Paths.get(logFilePath);

        if (!Files.exists(path)) {
            throw new IOException("Arquivo de log não encontrado: " + logFilePath);
        }

        try (Stream<String> lines = Files.lines(path)) {
            List<String> logLines = lines.collect(Collectors.toList());

            Map<String, Long> levelCount = new HashMap<>();
            Map<String, Long> dailyCount = new TreeMap<>(); // Ordena por data
            List<String> errors = new ArrayList<>();

            for (String line : logLines) {
                if (line.contains(" ERROR ")) {
                    levelCount.put("ERROR", levelCount.getOrDefault("ERROR", 0L) + 1);
                } else if (line.contains(" WARN ")) {
                    levelCount.put("WARN", levelCount.getOrDefault("WARN", 0L) + 1);
                } else if (line.contains(" INFO ")) {
                    levelCount.put("INFO", levelCount.getOrDefault("INFO", 0L) + 1);
                } else if (line.contains(" DEBUG ")) {
                    levelCount.put("DEBUG", levelCount.getOrDefault("DEBUG", 0L) + 1);
                } else if (line.contains(" TRACE ")) {
                    levelCount.put("TRACE", levelCount.getOrDefault("TRACE", 0L) + 1);
                }

                // 8. Processamento de datas e erros (só para linhas com formato válido)
                if (line.length() > 23) {
                    String date = line.substring(0, 10);

                    dailyCount.put(date, dailyCount.getOrDefault(date, 0L) + 1);

                    if (line.contains(" ERROR ") && errors.size() < 20) {
                        errors.add(line);
                    }
                }
            }

            log.info("Relatório gerado com sucesso");

            return Map.of(
                    "logFile", logFilePath,
                    "totalEntries", logLines.size(),
                    "levels", levelCount,
                    "dailyEntries", dailyCount,
                    "lastErrors", errors,
                    "generatedAt", new Date()
            );
        }
    }
}