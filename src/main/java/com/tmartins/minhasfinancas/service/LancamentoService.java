package com.tmartins.minhasfinancas.service;

import com.tmartins.minhasfinancas.domain.Lancamento;
import com.tmartins.minhasfinancas.domain.Usuario;
import com.tmartins.minhasfinancas.dto.AtualizaStatusDTO;
import com.tmartins.minhasfinancas.dto.LancamentoDTO;
import com.tmartins.minhasfinancas.enumeration.StatusLancamento;
import com.tmartins.minhasfinancas.enumeration.TipoLancamento;
import com.tmartins.minhasfinancas.exception.RegraNegocioException;
import com.tmartins.minhasfinancas.repository.LancamentoRepository;
import com.tmartins.minhasfinancas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Objects.isNull;


@Service
public class LancamentoService {

    private final LancamentoRepository lancamentoRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public LancamentoService(LancamentoRepository repository, UsuarioRepository usuarioRepository) {
        this.lancamentoRepository = repository;
        this.usuarioRepository = usuarioRepository;
    }


    public List<Lancamento> findAll() {
        return lancamentoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Lancamento findByUsuarioId(Long usuarioId) {
        var usuario = usuarioRepository.findById(usuarioId).orElse(null);
        return lancamentoRepository.findByUsuario(usuario);
    }

    public Lancamento atualizarStatus(Long id, AtualizaStatusDTO atualizaStatusDTO) {
        var lancamento = lancamentoRepository.findById(id).orElseThrow(() -> new RegraNegocioException("Lancamento " +
                "não encontrado"));
        lancamento.setStatusLancamento(StatusLancamento.valueOf(atualizaStatusDTO.getStatus()));
        return lancamento;
    }

    public List<Lancamento> findById(Long id) {
        return lancamentoRepository.findById(id).stream()
                .filter(lancamento -> lancamento.getAtivo().equals(true))
                .toList();
    }

    @Transactional(readOnly = true)
    public BigDecimal obterSaldoPorUsuario(Long usuarioId) {

        var receitas = lancamentoRepository.obterSaldoPorTipoLancamentoEUsuarioEStatus(usuarioId,
                TipoLancamento.RECEITA, StatusLancamento.EFETIVADO);
        var despesas = lancamentoRepository.obterSaldoPorTipoLancamentoEUsuarioEStatus(usuarioId,
                TipoLancamento.DESPESA, StatusLancamento.EFETIVADO);

        if (isNull(receitas)) {
            receitas = BigDecimal.ZERO;
        }

        if (isNull(despesas)) {
            despesas = BigDecimal.ZERO;
        }

        return receitas.subtract(despesas);
    }

    public Lancamento create(LancamentoDTO lancamentoDTO) {
        var lancamento = converter(lancamentoDTO);
        lancamento.setDataCadastro(LocalDate.now());

        return lancamentoRepository.save(lancamento);
    }

    @Transactional
    public Lancamento update(LancamentoDTO lancamentoDTO) {
        if (isNull(lancamentoDTO.getId())) {
            create(lancamentoDTO);
        }
       var lancamento = lancamentoRepository.findById(lancamentoDTO.getId()).orElseThrow(() -> new RegraNegocioException("Lançamento não encontrado"));
        lancamento.update(lancamentoDTO);
        return lancamentoRepository.save(lancamento);
    }

    @Transactional
    public void deletar(Long id) {
        var lancamento = lancamentoRepository.findById(id).orElseThrow(() -> new RegraNegocioException("Lancamento " +
                "não encontrado"));
        lancamento.setAtivo(false);
    }

    public Lancamento converter(LancamentoDTO lancamentoDTO) {
        Lancamento lancamento = Lancamento.builder()
                .descricao(lancamentoDTO.getDescricao())
                .mes(lancamentoDTO.getMes())
                .ano(lancamentoDTO.getAno())
                .valor(lancamentoDTO.getValor())
                .tipoLancamento(TipoLancamento.valueOf(lancamentoDTO.getTipo()))
                .statusLancamento(StatusLancamento.EFETIVADO)
                .ativo(lancamentoDTO.getAtivo())
                .usuario(lancamentoDTO.getUsuario())
                .build();
        return lancamento;
    }
}
