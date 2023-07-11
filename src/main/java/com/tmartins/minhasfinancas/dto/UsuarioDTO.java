package com.tmartins.minhasfinancas.dto;

import com.tmartins.minhasfinancas.domain.Lancamento;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Long id;
    private String email;
    private String nome;
    private String senha;
    private Boolean ativo = true;
    List<Lancamento> lancamento = new ArrayList<>();
}
