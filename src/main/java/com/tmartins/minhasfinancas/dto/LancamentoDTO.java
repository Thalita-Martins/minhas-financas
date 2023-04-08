package com.tmartins.minhasfinancas.dto;

import java.math.BigDecimal;

import com.tmartins.minhasfinancas.domain.Usuario;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LancamentoDTO {

    private Long id;
    private String descricao;
    private Integer mes;
    private Integer ano;
    private BigDecimal valor;
    private Usuario usuario;
    private String tipo;
    private String status;
    private Boolean ativo = true;

    public LancamentoDTO(Long id, String descricao, Integer mes, Integer ano, BigDecimal valor, String tipo) {
        this.id = id;
        this.descricao = descricao;
        this.mes = mes;
        this.ano = ano;
        this.valor = valor;
        this.tipo = tipo;
    }
}
