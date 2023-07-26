package com.tmartins.minhasfinancas.service;

import com.tmartins.minhasfinancas.domain.Lancamento;
import com.tmartins.minhasfinancas.dto.AtualizaStatusDTO;
import com.tmartins.minhasfinancas.enumeration.StatusLancamento;
import com.tmartins.minhasfinancas.enumeration.TipoLancamento;
import com.tmartins.minhasfinancas.repository.LancamentoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LancamentoServiceTest {

    @Autowired
   private LancamentoService service;

    @Autowired
   private LancamentoRepository repository;

    @Test
    public void obterSaldoUsuario(){
        var usuarioId = 1L;

        var result = service.obterSaldoPorUsuario(usuarioId);

        Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void atualizaStatus(){
    AtualizaStatusDTO status = new AtualizaStatusDTO("QUITADO");
        Lancamento lancamento = Lancamento.builder().ano(2023).valor(BigDecimal.valueOf(100)).tipoLancamento(TipoLancamento.RECEITA).statusLancamento(StatusLancamento.PENDENTE).build();

        repository.save(lancamento);
        var result = service.atualizarStatus(lancamento.getId(), status);

        Assertions.assertThat(result.getStatusLancamento()).isEqualByComparingTo(StatusLancamento.QUITADO);
    }
}
