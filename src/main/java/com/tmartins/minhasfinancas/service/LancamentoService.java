package com.tmartins.minhasfinancas.service;

import com.tmartins.minhasfinancas.domain.Lancamento;
import com.tmartins.minhasfinancas.dto.AtualizaStatusDTO;
import com.tmartins.minhasfinancas.dto.LancamentoDTO;
import com.tmartins.minhasfinancas.enumeration.StatusLancamento;
import com.tmartins.minhasfinancas.enumeration.TipoLancamento;
import com.tmartins.minhasfinancas.exception.RegraNegocioException;
import com.tmartins.minhasfinancas.repository.LancamentoRepository;
import com.tmartins.minhasfinancas.repository.UsuarioRepository;
import com.tmartins.minhasfinancas.specification.LancamentoSpecification;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;


@Service
public class LancamentoService {

    private final LancamentoRepository lancamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LancamentoSpecification lancamentoSpecification;

    @Autowired
    public LancamentoService(LancamentoRepository repository, UsuarioRepository usuarioRepository,
                             LancamentoSpecification lancamentoSpecification) {
        this.lancamentoRepository = repository;
        this.usuarioRepository = usuarioRepository;
        this.lancamentoSpecification = lancamentoSpecification;
    }

    public List<Lancamento> findAll(String descricao, String tipo, Integer mes, Integer ano, Long usuarioId) {
        TipoLancamento tipoLancamento = null;
        if (nonNull(tipo)){
            tipoLancamento = TipoLancamento.valueOf(tipo);
        }
        return lancamentoRepository.findAll(lancamentoSpecification.filters(usuarioId, descricao,ano, mes, tipoLancamento));
    }

    @Transactional(readOnly = true)
    public List<Lancamento> findByUsuarioId(Long usuarioId) {
        var usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado"));
        return lancamentoRepository.findByUsuario(usuario);
    }

    public Lancamento atualizarStatus(Long id, AtualizaStatusDTO atualizaStatusDTO) {
        var lancamento = lancamentoRepository.findById(id).orElseThrow(() -> new RegraNegocioException("Lancamento não encontrado"));
        lancamento.setStatusLancamento(StatusLancamento.valueOf(atualizaStatusDTO.getStatus()));
        return lancamento;
    }

    public Lancamento findById(Long id) {
        return lancamentoRepository.findById(id).orElseThrow(() -> new RegraNegocioException("Lancamento não encontrado"));
    }

    @Transactional(readOnly = true)
    public BigDecimal obterSaldoPorUsuario(Long usuarioId) {

        var receitas = lancamentoRepository.obterSaldoPorTipoLancamentoEUsuarioEStatus(usuarioId,
                TipoLancamento.RECEITA, StatusLancamento.CREDITO);
        var despesas = lancamentoRepository.obterSaldoPorTipoLancamentoEUsuarioEStatus(usuarioId,
                TipoLancamento.DESPESA, StatusLancamento.QUITADO);

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
        lancamentoRepository.save(lancamento);
    }

    public Lancamento converter(LancamentoDTO lancamentoDTO) {
        Lancamento lancamento = Lancamento.builder()
                .descricao(lancamentoDTO.getDescricao())
                .mes(lancamentoDTO.getMes())
                .ano(lancamentoDTO.getAno())
                .valor(lancamentoDTO.getValor())
                .tipoLancamento(TipoLancamento.valueOf(lancamentoDTO.getTipoLancamento()))
                .statusLancamento(StatusLancamento.valueOf(lancamentoDTO.getStatusLancamento()))
                .usuario(lancamentoDTO.getUsuario())
                .build();
        return lancamento;
    }

    public BigDecimal totalValorByTipoDespesa(Long usuarioId, String tipoDespesa, String statusDespesa) {
        if (statusDespesa.equals("")){
            statusDespesa = "QUITADO";
        }
        var total = lancamentoRepository.obterSaldoPorTipoLancamentoEUsuarioEStatus(usuarioId, TipoLancamento.valueOf(tipoDespesa), StatusLancamento.valueOf(statusDespesa));
        if(isNull(total)){
            total = BigDecimal.ZERO;
        }
        return total;
    }
}
