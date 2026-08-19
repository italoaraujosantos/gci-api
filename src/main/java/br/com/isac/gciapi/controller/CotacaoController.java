package br.com.isac.gciapi.controller;

import br.com.isac.gciapi.service.CotacaoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.JsonNode;

@RestController
@RequestMapping("/cotacoes")
public class CotacaoController {

    private final CotacaoService cotacaoService;

    public CotacaoController(CotacaoService cotacaoService) {
        this.cotacaoService = cotacaoService;
    }

    @GetMapping("/{ticker}")
    public JsonNode buscarCotacao(@PathVariable String ticker) {
        return cotacaoService.buscarCotacao(ticker);
    }
}
