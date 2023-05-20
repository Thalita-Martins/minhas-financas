package com.tmartins.minhasfinancas.controller;

import java.util.ArrayList;
import java.util.List;

import com.tmartins.minhasfinancas.domain.Lancamento;
import com.tmartins.minhasfinancas.dto.AtualizaStatusDTO;
import com.tmartins.minhasfinancas.dto.LancamentoDTO;
import com.tmartins.minhasfinancas.exception.RegraNegocioException;
import com.tmartins.minhasfinancas.service.LancamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoController {

    private final LancamentoService lancamentoService;

    @GetMapping
    public ResponseEntity findAllLancamento() {
        try{
            List<Lancamento> lancamentos = lancamentoService.findAll();
            return new ResponseEntity(lancamentos, HttpStatus.OK);
        }catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/usuario/{usuarioId}")
        public ResponseEntity findLancamentoByUsuarioId(@PathVariable("usuarioId") Long usuarioId) {
        try {
            List<Lancamento> lancamentos = lancamentoService.findByUsuarioId(usuarioId);
            List<LancamentoDTO> lancamentosDTO = new ArrayList<>();
            for(Lancamento lancamento : lancamentos){
                lancamentosDTO.add(new LancamentoDTO(lancamento.getId(), lancamento.getDescricao(), lancamento.getMes(),
                        lancamento.getAno(), lancamento.getValor(), lancamento.getTipoLancamento().name()));
            }
            return ResponseEntity.ok().body(lancamentosDTO);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity findLancamentoById(@PathVariable("id") Long lancamentoId) {
        try {
            var lancamento = lancamentoService.findById(lancamentoId);
            return ResponseEntity.ok(lancamento);
        }catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/cadastrar")
    public ResponseEntity createLancamento(@RequestBody LancamentoDTO lancamentoDTO) {
        try {
            var lancamento = lancamentoService.create(lancamentoDTO);
            return ResponseEntity.ok(lancamento);
        }catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/atualizar")
    public ResponseEntity updateLancamento(@RequestBody LancamentoDTO lancamentoDTO) {
        try{
            var lancamento = lancamentoService.update(lancamentoDTO);
            return ResponseEntity.ok(lancamento);
        }catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/atualiza-status")
    public ResponseEntity atualizarStatusLancamentoId(@PathVariable("lancamentoId") Long id,@RequestBody AtualizaStatusDTO atualizaStatusDTO) {
        try{
            var lancamento = lancamentoService.atualizarStatus(id, atualizaStatusDTO);
            return ResponseEntity.ok(lancamento);
        }catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/deletar")
    public void deletarLancamento(@PathVariable Long id) {
       lancamentoService.deletar(id);
    }
}
