package com.alex.buscacep.infra.service;
import com.alex.buscacep.domain.dtos.request.EnderecoRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
import static org.assertj.core.api.Assertions.assertThat;

class CepServiceTest {

    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    @DisplayName("Should get endereco successfully from ViaCep")
    void getViaCepCase1() {
        String url = "https://viacep.com.br/ws/24900435/json/";
        var endereco = (restTemplate.getForObject(url, EnderecoRequestDTO.class));
        assertThat(endereco.getCep()).isEqualTo("24900-435");
    }

    @Test
    @DisplayName("Should not get endereço from ViaCep")
    void getViaCepCase2() {
        String url = "https://viacep.com.br/ws/00000000/json/";
        var result = (restTemplate.getForObject(url, EnderecoRequestDTO.class));
        assertThat(result.getCep()).isEqualTo(null);
    }
}