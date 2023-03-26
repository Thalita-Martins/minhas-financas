package com.tmartins.minhasfinancas.api.resource;

import java.util.List;

import com.tmartins.minhasfinancas.domain.Lancamento;
import com.tmartins.minhasfinancas.dto.AtualizaStatusDTO;
import com.tmartins.minhasfinancas.dto.LancamentoDTO;
import com.tmartins.minhasfinancas.exception.RegraNegocioException;
import com.tmartins.minhasfinancas.service.LancamentoService;
import com.tmartins.minhasfinancas.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoResource {

    private final LancamentoService lancamentoService;
    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity findAllLancamento() {
        List<Lancamento> lancamentos = lancamentoService.findAll();
        return ResponseEntity.ok(lancamentos);
    }

    @GetMapping("/usuario")
        public ResponseEntity findLancamentoByUsuarioId(@RequestParam("usuarioId") Long usuarioId){
        List<Lancamento> lancamentos = lancamentoService.findByUsuarioId(usuarioId);
           return ResponseEntity.ok(lancamentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity findLancamentoById(@PathVariable Long lancamentoId) {
        var lancamento = lancamentoService.findById(lancamentoId);
        try {
            return ResponseEntity.ok(lancamento);
        }catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity createLancamento(@RequestBody LancamentoDTO lancamentoDTO) {
        var lancamento = lancamentoService.create(lancamentoDTO);
        try {
            return ResponseEntity.ok(lancamento);
        }catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updateLancamento(@PathVariable Long id, @RequestBody LancamentoDTO lancamentoDTO) {
        var lancamento = lancamentoService.update(lancamentoDTO);
        try{
            return ResponseEntity.ok(lancamento);
        }catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/atualiza-status")
    public ResponseEntity atualizarStatusLancamentoId(@PathVariable("lancamentoId") Long id,
                                                    @RequestBody AtualizaStatusDTO atualizaStatusDTO) {
        var lancamento = lancamentoService.atualizarStatus(id, atualizaStatusDTO);
        try{
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
