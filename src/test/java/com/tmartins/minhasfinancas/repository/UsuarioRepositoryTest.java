package com.tmartins.minhasfinancas.repository;

import com.tmartins.minhasfinancas.domain.Usuario;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Test
    @Order(1)
    public void criarUsuario(){
        var usuario = Usuario
                .builder()
                .nome("usuario")
                .email("usuario_teste@teste.com.br")
                .senha("senha")
                .build();
        usuarioRepository.save(usuario);
        boolean result = usuarioRepository.existsById(usuario.getId());

        Assertions.assertTrue(result);
    }

    @Test
    @Order(2)
    public void verificarAExistenciaDeUmEmail(){
        boolean result = usuarioRepository.existsByEmail("usuario_teste@teste.com.br");

        Assertions.assertTrue(result);
    }

    @Test
    @Order(3)
    public void findByEmail(){
        boolean result = usuarioRepository.findByEmail("usuario_teste@teste.com.br").isPresent();

        Assertions.assertTrue(result);
    }

    @Test
    @Order(4)
    public void deletarUsuario(){
        var usuario = usuarioRepository.findByEmail("usuario_teste@teste.com.br").get();
        usuarioRepository.delete(usuario);
        boolean result = usuarioRepository.findByEmail("usuario_teste@teste.com.br").isPresent();

        Assertions.assertFalse(result);
    }
}
