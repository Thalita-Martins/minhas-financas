package com.tmartins.minhasfinancas.domain;

import com.tmartins.minhasfinancas.enumeration.StatusLancamento;
import com.tmartins.minhasfinancas.enumeration.TipoLancamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lancamento")
public class Lancamento {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mes")
    private Integer mes;

    @Column(name = "ano")
    private Integer ano;

    @ManyToOne
    @JoinColumn(name = "usuario_id",nullable = false)
    private Usuario usuario;

    @Column(name = "descricao",nullable = false)
    private String descricao;

    @Column(name = "valor",nullable = false)
    private BigDecimal valor;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    @Column(name = "tipo", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TipoLancamento tipoLancamento;

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StatusLancamento statusLancamento;

    @Column(name = "ativo")
    private Boolean ativo = true;

}
