package com.tmartins.minhasfinancas.specification;

import com.tmartins.minhasfinancas.domain.Lancamento;
import com.tmartins.minhasfinancas.enumeration.TipoLancamento;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LancamentoSpecification implements SpecificationDefault<Lancamento> {

    private Specification<Lancamento> usuario(Long id){
        return (root, query, cb) -> cb.equal(root.get("usuario"), id);
    }

    private Specification<Lancamento> ano(Integer ano){
        return  (root, query, cb) -> cb.equal(root.get("ano"), ano);
    }

    private Specification<Lancamento> mes(Integer mes){
        return  (root, query, cb) -> cb.equal(root.get("mes"), mes);
    }

    private Specification<Lancamento> descricao(String descricao){
        return (root, query, cb) -> cb.like(root.get("descricao"), "%" + descricao + "%");
    }

    private Specification<Lancamento> tipo(TipoLancamento tipo){
        return (root, query, cb) -> cb.equal(root.get("tipoLancamento"), tipo);
    }

    private Specification<Lancamento> isAtivo(){
        return (root, query, cb) -> cb.isTrue(root.get("ativo"));
    }

    public Specification<Lancamento> filters(Long usuarioId, String descricao, Integer ano, Integer mes, TipoLancamento tipo){
        var builder = this.builder();

        builder.and(usuario(usuarioId));
        builder.and(isAtivo());

        Optional.ofNullable(descricao).map(this::descricao).ifPresent(builder::and);
        Optional.ofNullable(ano).map(this::ano).ifPresent(builder::and);
        Optional.ofNullable(mes).map(this::mes).ifPresent(builder::and);
        Optional.ofNullable(tipo).map(this::tipo).ifPresent(builder::and);

        return builder.build();
    }
}
