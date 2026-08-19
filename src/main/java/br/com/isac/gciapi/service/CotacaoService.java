package br.com.isac.gciapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.databind.JsonNode;

@Service
public class CotacaoService {

    @Value("${hgbrasil.api.url}")
    private String apiUrl;

    @Value("${hgbrasil.api.key}")
    private String apiKey;

    private final RestClient restClient;

    public CotacaoService(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    public JsonNode buscarCotacao(String ticker) {

        String url = UriComponentsBuilder
                .fromUriString(apiUrl)
                .path("/v2/finance/quotes")
                .queryParam("tickers", "B3:" + ticker)
                .queryParam("key", apiKey)
                .toUriString();

        return restClient
                .get()
                .uri(url)
                .retrieve()
                .body(JsonNode.class);
    }
}
