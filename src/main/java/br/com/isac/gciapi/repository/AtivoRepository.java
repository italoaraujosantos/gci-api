package br.com.isac.gciapi.repository;

import br.com.isac.gciapi.entity.Ativo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AtivoRepository extends JpaRepository <Ativo, Long> {
    Optional<Ativo> findByTicker(String ticker);
}
