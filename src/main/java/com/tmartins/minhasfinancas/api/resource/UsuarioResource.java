package com.tmartins.minhasfinancas.api.resource;

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

import static java.util.Objects.isNull;

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

        if (isNull(saldo)) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(saldo);
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
    public void deletarUsuarioId(@PathVariable("usuarioId") Long usuarioId) {
        usuarioService.delete(usuarioId);
    }
}
