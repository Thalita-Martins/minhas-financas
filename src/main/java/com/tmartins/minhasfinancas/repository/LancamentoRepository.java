package com.tmartins.minhasfinancas.repository;

import com.tmartins.minhasfinancas.domain.Lancamento;
import com.tmartins.minhasfinancas.domain.Usuario;
import com.tmartins.minhasfinancas.enumeration.StatusLancamento;
import com.tmartins.minhasfinancas.enumeration.TipoLancamento;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, JpaSpecificationExecutor<Lancamento> {

    @Query(value = """
            select sum(l.valor) from Lancamento l join l.usuario u
            where u.id =:usuarioId 
            and l.tipoLancamento =:tipo
            and l.statusLancamento =:status
            group by u
            """)
    BigDecimal obterSaldoPorTipoLancamentoEUsuarioEStatus(@Param("usuarioId") Long usuarioId, @Param("tipo")TipoLancamento tipoLancamento,
                                                          @Param("status") StatusLancamento status);

    List<Lancamento> findByUsuario(Usuario usuario);
}
