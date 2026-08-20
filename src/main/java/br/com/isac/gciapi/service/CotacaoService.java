package br.com.isac.gciapi.service;

import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Service
public class CotacaoService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;

    public CotacaoService(
            RestClient.Builder restClientBuilder,
            ObjectMapper objectMapper,
            @Value("${hgbrasil.api.url}") String apiUrl,
            @Value("${hgbrasil.api.key}") String apiKey) {

        this.restClient = restClientBuilder.baseUrl(apiUrl).build();
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
    }

    public BigDecimal buscarCotacao(String ticker) {
        String json = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/finance/quotes")
                        .queryParam("tickers", "B3:" + ticker)
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .body(String.class);

        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode resultado = root.path("results").path(ticker).path("price");

            if (resultado.isMissingNode() || !resultado.isNumber()) {
                throw new RuntimeException("Cotação não encontrada para o ticker: " + ticker);
            }

            return resultado.decimalValue();
        } catch (Exception e) {
            if (e instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            throw new RuntimeException("Erro ao processar cotação de " + ticker, e);
        }
    }
}
