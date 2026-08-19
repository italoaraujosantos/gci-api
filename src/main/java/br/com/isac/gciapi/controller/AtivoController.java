package br.com.isac.gciapi.controller;

import br.com.isac.gciapi.entity.Ativo;
import br.com.isac.gciapi.repository.AtivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller
@RequestMapping("/api/ativos")
@CrossOrigin(origins = "*")
public class AtivoController {

    @Autowired
    private AtivoRepository ativoRepository;

    @GetMapping
    public Iterable<Ativo> listarAtivos() {
        return ativoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Ativo buscarAtivoPorId(@PathVariable Long id) {
        return ativoRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Ativo não encontrado com o ID: " +id)
        );
    }

    @GetMapping("/ticker/{ticker}")
    public Ativo buscarAtivoPorTicker(@PathVariable String ticker) {
        return ativoRepository.findByTicker(ticker).orElseThrow(
                () -> new RuntimeException("Ativo não encontrado com o Ticker: " + ticker)
        );
    }

    @GetMapping("/{ticker}/cotacao")
    public BigDecimal buscarCotacao(@PathVariable String ticker) {
        return ativoRepository.findByTicker(ticker)
                .map(Ativo::getPrecoAtual)
                .orElseThrow(() -> new RuntimeException("Ativo não encontrado com o Ticker: " + ticker));
    }

    @PostMapping
    public Ativo criarAtivo(Ativo ativo) {
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
        ativoExistente.setPrecoAtual(ativoAtualizado.getPrecoAtual());
        ativoExistente.setValorInvestido(ativoAtualizado.getValorInvestido());
        ativoExistente.setValorAtual(ativoAtualizado.getValorAtual());

        return ativoRepository.save(ativoExistente);
    }

    @DeleteMapping("/{id}")
    public void deletarAtivo(@PathVariable Long id) {
        Ativo ativoExistente = ativoRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Ativo não encontrado com o ID: " +id)
        );
        ativoRepository.delete(ativoExistente);
    }
}
