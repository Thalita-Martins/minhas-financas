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
import java.util.List;
import java.util.Optional;

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

    @Transactional(readOnly = true)
    public List<Lancamento> findAll() {
        return lancamentoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Lancamento> findByUsuarioId(Long usuarioId){
        usuarioRepository.findById(usuarioId).orElseThrow(() -> new RegraNegocioException("Usuario não encontrado"));
        return lancamentoRepository.findByUsuarioId(usuarioId);
    }

    public Lancamento atualizarStatus(Long id, AtualizaStatusDTO atualizaStatusDTO) {
        var lancamento = lancamentoRepository.findById(id).orElseThrow(() -> new RegraNegocioException("Lancamento não encontrado"));
        lancamento.setStatusLancamento(StatusLancamento.valueOf(atualizaStatusDTO.getStatus()));
        return lancamento;
    }

    public Optional<Lancamento> findById(Long id) {
        return lancamentoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public BigDecimal obterSaldoPorUsuario(Long usuarioId) {

        var receitas = lancamentoRepository.obterSaldoPorTipoLancamentoEUsuarioEStatus(usuarioId, TipoLancamento.RECEITA, StatusLancamento.EFETIVADO);
        var despesas = lancamentoRepository.obterSaldoPorTipoLancamentoEUsuarioEStatus(usuarioId, TipoLancamento.DESPESA, StatusLancamento.EFETIVADO);

        if(isNull(receitas)) {
            receitas = BigDecimal.ZERO;
        }

        if(isNull(despesas)) {
            despesas = BigDecimal.ZERO;
        }

        return receitas.subtract(despesas);
    }

    public Lancamento create(LancamentoDTO lancamentoDTO) {
        var lancamento = converter(lancamentoDTO);

        return lancamentoRepository.save(lancamento);
    }

    @Transactional
    public Lancamento update(LancamentoDTO lancamentoDTO) {
        if (isNull(lancamentoDTO.getId())){
            create(lancamentoDTO);
        }
        var lancamento =  converter(lancamentoDTO);
        return lancamentoRepository.save(lancamento);
    }

    @Transactional
    public void deletar(Long id) {
        var lancamento = lancamentoRepository.findById(id).orElseThrow(() -> new RegraNegocioException("Lancamento não encontrado"));
        lancamento.setAtivo(false);
    }

    public Lancamento converter(LancamentoDTO lancamentoDTO){
        var lancamento = new Lancamento();

        lancamento.setDescricao(lancamentoDTO.getDescricao());
        lancamento.setAno(lancamentoDTO.getAno());
        lancamento.setMes(lancamentoDTO.getMes());
        lancamento.setValor(lancamentoDTO.getValor());
        lancamento.setStatusLancamento(StatusLancamento.valueOf(lancamentoDTO.getStatus()));
        lancamento.setAtivo(lancamentoDTO.getAtivo());

        Usuario usuario = usuarioRepository
                .findById(lancamentoDTO.getUsuario())
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado para o Id informado."));

        lancamento.setUsuario(usuario);
        return lancamento;
    }
}
