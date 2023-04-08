package com.tmartins.minhasfinancas.controller;

import java.util.List;

import com.tmartins.minhasfinancas.domain.Usuario;
import com.tmartins.minhasfinancas.dto.UsuarioDTO;
import com.tmartins.minhasfinancas.exception.ErroAutenticacao;
import com.tmartins.minhasfinancas.exception.RegraNegocioException;
import com.tmartins.minhasfinancas.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioResource {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity findAllUsuario() {
        try {
            List<Usuario> usuarios = usuarioService.findAll();
            return new ResponseEntity(usuarios, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity findByUsuarioId(@PathVariable("usuarioId") Long usuarioId) {
        try {
            Usuario usuario = usuarioService.findById(usuarioId);
            return new ResponseEntity(usuario, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{usuarioId}/saldo")
    public ResponseEntity obterSaldoByUsuarioId(@PathVariable("usuarioId") Long usuarioId) {
        var saldo = usuarioService.findBySaldoByUsuarioId(usuarioId);

        try{
            return ResponseEntity.ok(saldo);
        } catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/obterTotal")
    public ResponseEntity obterTotalByTipoDespesa(@RequestParam(value = "usuarioId") Long usuarioId, @RequestParam(value = "tipoDespesa") String tipoDespesa) {
        var saldo = usuarioService.findByTotalDespesa(usuarioId, tipoDespesa, "");

        try {
            return ResponseEntity.ok(saldo);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{usuarioId}/totalDespesa")
    public ResponseEntity obterTotalByTipoDespesaStatus(@PathVariable("usuarioId") Long usuarioId, @RequestParam(value = "tipoDespesa") String tipoDespesa, @RequestParam(value = "statusDespesa") String statusDespesa) {
        var saldo = usuarioService.findByTotalDespesa(usuarioId, tipoDespesa, statusDespesa);

        try {
            return ResponseEntity.ok(saldo);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/autenticar")
    public ResponseEntity<?> autenticar(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            Usuario usuarioAutenticado = usuarioService.autenticar(usuarioDTO.getEmail(), usuarioDTO.getSenha());

            return ResponseEntity.ok(usuarioAutenticado);
        } catch (ErroAutenticacao e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/cadastrar")
    public ResponseEntity createUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            Usuario usuario = usuarioService.create(usuarioDTO);
            return new ResponseEntity(usuario, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/atualizar")
    public ResponseEntity updateUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            Usuario usuario = usuarioService.update(usuarioDTO);
            return new ResponseEntity(usuario, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{usuarioId}/deletar")
    public ResponseEntity deletarUsuarioId(@PathVariable("usuarioId") Long usuarioId) {
        try {
            Boolean usuario = usuarioService.delete(usuarioId);
            return new ResponseEntity(usuario, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
