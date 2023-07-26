package com.tmartins.minhasfinancas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tmartins.minhasfinancas.dto.LancamentoDTO;
import com.tmartins.minhasfinancas.enumeration.StatusLancamento;
import com.tmartins.minhasfinancas.enumeration.TipoLancamento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lancamento")
public class Lancamento implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final String SEQ_GENERATOR = "lancamento_id_seq_gen";
    private static final String SEQ_NAME = "lancamento_id_seq";

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_GENERATOR)
    @SequenceGenerator(name = SEQ_GENERATOR, sequenceName = SEQ_NAME, allocationSize = 1)
    private Long id;

    @Column(name = "mes")
    private Integer mes;

    @Column(name = "ano")
    private Integer ano;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonIgnoreProperties("lancamento")
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

    public void update (LancamentoDTO lancamentoDTO){
        this.setDescricao(lancamentoDTO.getDescricao());
        this.setMes(lancamentoDTO.getMes());
        this.setAno(lancamentoDTO.getAno());
        this.setValor(lancamentoDTO.getValor());
        this.setTipoLancamento(TipoLancamento.valueOf(lancamentoDTO.getTipoLancamento()));
        this.setStatusLancamento(StatusLancamento.valueOf(lancamentoDTO.getStatusLancamento()));
        this.setUsuario(lancamentoDTO.getUsuario());
    }
}
