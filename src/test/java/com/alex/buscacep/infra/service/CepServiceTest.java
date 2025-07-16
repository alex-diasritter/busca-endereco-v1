package com.alex.buscacep.infra.service;
import com.alex.buscacep.domain.dtos.request.EnderecoRequestDTO;
import com.alex.buscacep.infra.client.ViaCepClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Habilita suporte ao Mockito
class CepServiceTest {

    @Mock
    private ViaCepClient viaCepClient; // Mock do cliente Feign

    @InjectMocks
    private CepService cepService; // Injeta o mock no serviço testado

    @Test
    @DisplayName("Deve retornar endereço válido quando CEP existir")
    void buscarEnderecoPorCep_ComCepValido_RetornaEndereco() {
        // Arrange
        String cep = "24900435";
        EnderecoRequestDTO mockResponse = new EnderecoRequestDTO();
        mockResponse.setCep("24900-435");

        // Configura o comportamento do mock
        when(viaCepClient.buscarEnderecoPorCep(cep))
                .thenReturn(mockResponse);

        // Act
        EnderecoRequestDTO resultado = cepService.consultarCep(cep);

        // Assert
        assertThat(resultado.getCep()).isEqualTo("24900-435");
    }

    @Test
    @DisplayName("Deve retornar objeto vazio quando CEP não existir")
    void buscarEnderecoPorCep_ComCepInvalido_RetornaVazio() {
        // Arrange
        String cep = "00000000";

        // Simula resposta de CEP não encontrado
        when(viaCepClient.buscarEnderecoPorCep(cep))
                .thenReturn(new EnderecoRequestDTO()); // Retorna objeto vazio

        // Act
        EnderecoRequestDTO resultado = cepService.consultarCep(cep);

        // Assert
        assertThat(resultado.getCep()).isNull();
    }
}