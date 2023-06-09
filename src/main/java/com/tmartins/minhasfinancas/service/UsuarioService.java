package com.tmartins.minhasfinancas.service;

import com.tmartins.minhasfinancas.domain.Usuario;
import com.tmartins.minhasfinancas.dto.UsuarioDTO;
import com.tmartins.minhasfinancas.exception.ErroAutenticacao;
import com.tmartins.minhasfinancas.exception.RegraNegocioException;
import com.tmartins.minhasfinancas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final LancamentoService lancamentoService;
    private PasswordEncoder encoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, LancamentoService lancamentoService,
                          PasswordEncoder encoder) {
        this.usuarioRepository = usuarioRepository;
        this.lancamentoService = lancamentoService;
        this.encoder = encoder;
    }

    public List<Usuario> findAll(){
        return usuarioRepository.findAll()
                .stream().filter(usuario -> usuario.getAtivo().equals(true))
                .toList();
    }

    public Usuario findById(Long id) {
        return usuarioRepository.findById(id).stream()
                .filter(usuario -> usuario.getAtivo().equals(true))
                .findAny()
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado"));
    }

    public BigDecimal findBySaldoByUsuarioId(Long usuarioId){
        var usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new RegraNegocioException("Usuário não encontrado"));

        return lancamentoService.obterSaldoPorUsuario(usuario.getId());
    }

    public Usuario autenticar(String email, String senha) {
        var usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new ErroAutenticacao("Usuário não encontrado para o email informado."));

        var compararSenhas = encoder.matches(senha, usuario.getSenha());

        if (!compararSenhas) {
            throw new ErroAutenticacao("Senha inválida");
        }
        return usuario;
    }

    @Transactional
    public Usuario create(UsuarioDTO usuarioDTO) {
        if (nonNull(usuarioDTO.getId())){
            update(usuarioDTO);
        }
        var usuario = converter(usuarioDTO);
        validarEmail(usuario.getEmail());
        criptografarSenha(usuario);

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario update(UsuarioDTO usuarioDTO) {
        if (isNull(usuarioDTO.getId())){
            create(usuarioDTO);
        }
        var usuarioRecuperado = usuarioRepository.findById(usuarioDTO.getId()).orElseThrow(() -> new RegraNegocioException("Usuário não encontrado"));
       usuarioRecuperado.setNome(usuarioDTO.getNome());
       usuarioRecuperado.setEmail(usuarioDTO.getEmail());
       usuarioRecuperado.setSenha(usuarioDTO.getSenha());
       usuarioRecuperado.setLancamento(usuarioDTO.getLancamento());
       usuarioRecuperado.setAtivo(usuarioDTO.getAtivo());

        return usuarioRepository.save(usuarioRecuperado);
    }

    @Transactional
    public Boolean delete(Long usuarioId){
        var usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new RegraNegocioException("Usuário não encontrado"));
        usuario.setAtivo(false);

        return true;
    }

    public void validarEmail(String email) {
        var existe = usuarioRepository.existsByEmail(email);
        if(existe) {
            throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");
        }
    }

    public void criptografarSenha(Usuario usuario){
        var senhaCripto = encoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCripto);
    }

    public Usuario converter(UsuarioDTO usuarioDTO){
        Usuario usuario = Usuario.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                .ativo(usuarioDTO.getAtivo()).build();
        return usuario;
    }

    public BigDecimal findByTotalDespesa(Long usuarioId, String tipoDespesa,String statusDespesa) {
        var usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new RegraNegocioException("Usuário não encontrado"));

        return lancamentoService.totalValorByTipoDespesa(usuarioId,tipoDespesa,statusDespesa);
    }
}
