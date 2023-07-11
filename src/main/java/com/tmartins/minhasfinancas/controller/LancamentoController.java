package com.tmartins.minhasfinancas.controller;

import java.util.List;

import com.tmartins.minhasfinancas.domain.Lancamento;
import com.tmartins.minhasfinancas.dto.AtualizaStatusDTO;
import com.tmartins.minhasfinancas.dto.LancamentoDTO;
import com.tmartins.minhasfinancas.exception.RegraNegocioException;
import com.tmartins.minhasfinancas.service.LancamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoController {

    private final LancamentoService lancamentoService;

    @GetMapping
    public ResponseEntity findAllLancamento(@RequestParam(value ="descricao" , required = false) String descricao,
                                            @RequestParam(value ="tipo" , required = false) String tipo,
                                            @RequestParam(value = "mes", required = false) Integer mes,
                                            @RequestParam(value = "ano", required = false) Integer ano,
                                            @RequestParam("usuario") Long usuarioId) {
        try{
            List<Lancamento> lancamentos = lancamentoService.findAll(descricao, tipo, mes, ano, usuarioId);
            return new ResponseEntity(lancamentos, HttpStatus.OK);
        }catch (RegraNegocioException e) {
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
    public ResponseEntity atualizarStatusLancamentoId(@PathVariable("id") Long id,@RequestBody AtualizaStatusDTO atualizaStatusDTO) {
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
