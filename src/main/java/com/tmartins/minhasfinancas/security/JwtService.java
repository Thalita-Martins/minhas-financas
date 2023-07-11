package com.tmartins.minhasfinancas.security;

import com.tmartins.minhasfinancas.domain.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class JwtService {

    @Value("${jwt.expiracao}")
    private String expiracao;

    @Value("${jwt.chave-assinatura}")
    private String chaveAssinatura;

    public String gerarToken(Usuario usuario){
        long exp = Long.valueOf(expiracao);
        LocalDateTime dataExpiracao = LocalDateTime.now().plusMinutes(exp);
        Instant instant = dataExpiracao.atZone( ZoneId.systemDefault() ).toInstant();
        Date data = Date.from(instant);

        String expiracaoToken = dataExpiracao.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));

        String token = Jwts
                .builder()
                .setExpiration(data)
                .setSubject(usuario.getEmail())
				.claim("userid", usuario.getId())
                .claim("nome", usuario.getNome())
                .claim("horaExpiracao", expiracaoToken)
                .signWith( SignatureAlgorithm.HS512, chaveAssinatura)
                .compact();

        return token;
    }

    public Claims obterClaims(String token) throws ExpiredJwtException{
        return Jwts.parser()
                .setSigningKey(chaveAssinatura)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValido(String token){
        try {
            Claims claims = obterClaims(token);
            Date dataExp = claims.getExpiration();
            LocalDateTime dataExpiracao = dataExp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            var dataHoraAtualDataExpiracao = LocalDateTime.now().isAfter(dataExpiracao);

            return !dataHoraAtualDataExpiracao;
        }catch (ExpiredJwtException e){
            return false;
        }
    }

    public String obterLoginUsuario(String token){
        Claims claims = obterClaims(token);
        return claims.getSubject();
    }
}
