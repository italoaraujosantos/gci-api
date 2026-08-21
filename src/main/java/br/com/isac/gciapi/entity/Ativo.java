package br.com.isac.gciapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Entity
@Table(name = "ativos")
public class Ativo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O ticker é obrigatório.")
    @Column(length = 10)
    private String ticker;

    @NotBlank(message = "O nome é obrigatório.")
    @Column(length = 30)
    private String nome;

    @Positive(message = "A quantidade é obrigatória e deve ser maior que zero.")
    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @DecimalMin(value = "0.01", message = "Campo obrigatório. O preço de compra deve ser maior que zero.")
    @Column(precision = 19, scale = 2)
    private BigDecimal precoCompra;

    @Column(precision = 19, scale = 2)
    private BigDecimal precoAtual;

    @Column(precision = 19, scale = 2)
    private BigDecimal valorInvestido;

    @Column(precision = 19, scale = 2)
    private BigDecimal valorAtual;

    @Column(precision = 19, scale = 2)
    private BigDecimal resultado;
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

    public BigDecimal getResultado() {
        return resultado;
    }

    public void setResultado(BigDecimal resultado) {
        this.resultado = resultado;
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

    public BigDecimal calcularResultado() {
        if (valorAtual != null && valorInvestido != null) {
            return valorAtual.subtract(valorInvestido);
        } else {
            return BigDecimal.ZERO;
        }
    }


}
