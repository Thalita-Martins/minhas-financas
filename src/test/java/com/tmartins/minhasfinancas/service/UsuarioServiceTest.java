package com.tmartins.minhasfinancas.service;

import com.tmartins.minhasfinancas.domain.Usuario;
import com.tmartins.minhasfinancas.repository.UsuarioRepository;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioServiceTest {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private UsuarioService service;

    @Test
    @Order(1)
    public void autenticarUsuario(){
        String email = "fulano@teste.com.br";
        String senha = "123";

        Usuario result = service.autenticar(email, senha);
        Assertions.assertNotNull(result);
    }

    @Test
    @Order(2)
    public void validarEmail(){
        Usuario usuario = Usuario.builder().email("teste@teste.com").senha("123").build();
        var result = repository.existsByEmail(usuario.getEmail());

        Assertions.assertTrue(result);
    }
}
