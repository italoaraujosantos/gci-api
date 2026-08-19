package br.com.isac.gciapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

@Entity
@Table(name = "ativo")
public class Ativo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O ticker é obrigatório.")
    @Column(name = "ticker", length = 10)
    private String ticker;
    @NotBlank(message = "O nome é obrigatório.")
    @Column(name = "nome", length = 30)
    private String nome;

    @NotBlank(message = "A quantidade é obrigatório.")
    @Min(value = 100, message = "A quantidade deve ser multiplos de 100.")
    @Column(name = "quantidade", nullable = true)
    private Integer quantidade;

    @NotBlank(message = "O preço de compra é obrigatório.")
    @Column(name = "preco_compra", precision = 19, scale = 2)
    private BigDecimal precoCompra;

    @Column(name = "preco_atual", precision = 19, scale = 2)
    private BigDecimal precoAtual;

    @Column(name = "valor_investido", precision = 19, scale = 2)
    private BigDecimal valorInvestido;

    @Column(name = "valor_atual", precision = 19, scale = 2)
    private BigDecimal valorAtual;

    public Ativo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoCompra() {
        return precoCompra;
    }

    public void setPrecoCompra(BigDecimal precoCompra) {
        this.precoCompra = precoCompra;
    }

    public BigDecimal getPrecoAtual() {
        return precoAtual;
    }

    public void setPrecoAtual(BigDecimal precoAtual) {
        this.precoAtual = precoAtual;
    }

    public BigDecimal getValorInvestido() {
        return valorInvestido;
    }

    public void setValorInvestido(BigDecimal valorInvestido) {
        this.valorInvestido = valorInvestido;
    }

    public BigDecimal getValorAtual() {
        return valorAtual;
    }

    public void setValorAtual(BigDecimal valorAtual) {
        this.valorAtual = valorAtual;
    }

    public BigDecimal calcularValorInvestido() {
        if (quantidade != null && precoCompra != null) {
            return precoCompra.multiply(BigDecimal.valueOf(quantidade));
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal calcularValorAtual() {
        if (quantidade != null && precoAtual != null) {
            return precoAtual.multiply(BigDecimal.valueOf(quantidade));
        } else {
            return BigDecimal.ZERO;
        }
    }

}
