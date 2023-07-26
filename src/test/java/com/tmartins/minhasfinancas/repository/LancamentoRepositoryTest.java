package com.tmartins.minhasfinancas.repository;

import com.tmartins.minhasfinancas.domain.Lancamento;
import com.tmartins.minhasfinancas.enumeration.StatusLancamento;
import com.tmartins.minhasfinancas.enumeration.TipoLancamento;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LancamentoRepositoryTest {

    @Autowired
    LancamentoRepository repository;

    @Test
    @Order(1)
    public void salvarLancamento() {
        Lancamento lancamento = criarLancamento();
        repository.save(lancamento);
        Assertions.assertNotNull(lancamento.getId());
    }

    @Test
    @Order(2)
    public void findByLancamentoId() {
        Lancamento lancamento = criarLancamento();
        repository.save(lancamento);
        repository.findByUsuario(lancamento.getUsuario());
    }

    public static Lancamento criarLancamento() {
        return Lancamento.builder()
                .id(25L)
                .ano(2023)
                .mes(1)
                .descricao("teste")
                .valor(BigDecimal.valueOf(10))
                .tipoLancamento(TipoLancamento.RECEITA)
                .statusLancamento(StatusLancamento.PENDENTE)
                .dataCadastro(LocalDate.now())
                .build();
    }
}
