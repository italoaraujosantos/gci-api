package br.com.isac.gciapi.controller;

import br.com.isac.gciapi.entity.Ativo;
import br.com.isac.gciapi.repository.AtivoRepository;
import br.com.isac.gciapi.service.CotacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/api/ativos")
@CrossOrigin(origins = "*")
public class AtivoController {


    private final AtivoRepository ativoRepository;
    private final CotacaoService cotacaoService;

    public AtivoController(AtivoRepository ativoRepository, CotacaoService cotacaoService) {
        this.ativoRepository = ativoRepository;
        this.cotacaoService = cotacaoService;
    }

    @GetMapping
    public List<Ativo> listarAtivos() {
        return ativoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Ativo buscarAtivoPorId(@PathVariable Long id) {
        return ativoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ativo não encontrado com o ID: " +id));
    }

    @GetMapping("/{ticker}/cotacao")
    public BigDecimal buscarCotacao(@PathVariable String ticker) {
        return  ativoRepository.findByTicker(ticker)
                .map(Ativo::getPrecoAtual)
                .orElseThrow(() -> new RuntimeException("Ativo não encontrado com o Ticker: " + ticker));
    }

    @PutMapping("/{ticker}/cotacao")
    public Ativo atualizarCotacao(@PathVariable String ticker) {
        Ativo ativo = buscarAtivoPorTicker(ticker);
        BigDecimal cotacao = cotacaoService.buscarCotacao(ticker);

        ativo.setPrecoAtual(cotacao);
        ativo.setValorInvestido(ativo.calcularValorInvestido());
        ativo.setValorAtual(ativo.calcularValorAtual());

        return ativoRepository.save(ativo);
    }

    @GetMapping("/ticker/{ticker}")
    public Ativo buscarAtivoPorTicker(@PathVariable String ticker) {
        return ativoRepository.findByTicker(ticker)
                .orElseThrow(() -> new RuntimeException("Ativo não encontrado com o Ticker: "+ ticker));
    }

    @PostMapping
    public Ativo salvarAtivo(@RequestBody Ativo ativo) {
        ativo.getPrecoCompra();
        ativo.calcularValorAtual();
        ativo.calcularValorInvestido();
        return ativoRepository.save(ativo);
    }

    @PutMapping("/{id}")
    public Ativo atualizarAtivo(@PathVariable Long id, @RequestBody Ativo ativoAtualizado) {
        Ativo ativoExistente = ativoRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Ativo não encontrado com o ID: " +id)
        );

        ativoExistente.setTicker(ativoAtualizado.getTicker());
        ativoExistente.setNome(ativoAtualizado.getNome());
        ativoExistente.setQuantidade(ativoAtualizado.getQuantidade());
        ativoExistente.setPrecoCompra(ativoAtualizado.getPrecoCompra());

        if(ativoAtualizado.getPrecoAtual() != null) {
           ativoExistente.setPrecoAtual(ativoAtualizado.getPrecoAtual());
        }

        ativoExistente.setValorInvestido(ativoAtualizado.getValorInvestido());
        ativoExistente.setValorAtual(ativoAtualizado.getValorAtual());

        return ativoRepository.save(ativoExistente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAtivo(@PathVariable Long id) {
        if(!ativoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        ativoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
