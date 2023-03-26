package com.tmartins.minhasfinancas.domain;

import com.tmartins.minhasfinancas.dto.UsuarioDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuario")
public class Usuario {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "email")
    private String email;

    @Column(name = "senha")
    private String senha;

    @Column(name = "ativo")
    private Boolean ativo = true;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    List<Lancamento> lancamento = new ArrayList<>();

}