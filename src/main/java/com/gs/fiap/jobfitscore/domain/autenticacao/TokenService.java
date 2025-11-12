package com.gs.fiap.jobfitscore.domain.autenticacao;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gs.fiap.jobfitscore.domain.usuario.Usuario;
import com.gs.fiap.jobfitscore.domain.empresa.Empresa;
import com.gs.fiap.jobfitscore.infra.exception.RegraDeNegocioException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
	
	private static final String CHAVE = "joao-gabriel-lucas-leal-leo-mota";
	private static final String EMISSOR = "JobFit-Score";
	
	// ===== Usuário =====
	public String gerarToken(Usuario usuario) {
		return gerarTokenGenerico(usuario.getUsername());
	}
	
	public String gerarRefreshToken(Usuario usuario) {
		return gerarRefreshTokenGenerico(usuario.getId().toString());
	}
	
	// ===== Empresa =====
	public String gerarToken(Empresa empresa) {
		return gerarTokenGenerico(empresa.getEmail());
	}
	
	public String gerarRefreshToken(Empresa empresa) {
		return gerarRefreshTokenGenerico(empresa.getId().toString());
	}
	
	// ===== Genéricos =====
	private String gerarTokenGenerico(String subject) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(CHAVE);
			return JWT.create()
					.withIssuer(EMISSOR)
					.withSubject(subject)
					.withExpiresAt(expiracao(120))
					.sign(algorithm);
		} catch (JWTCreationException exception) {
			throw new RegraDeNegocioException("Erro ao gerar o token JWT de acesso");
		}
	}
	
	private String gerarRefreshTokenGenerico(String subject) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(CHAVE);
			return JWT.create()
					.withIssuer(EMISSOR)
					.withSubject(subject)
					.withExpiresAt(expiracao(10080))
					.sign(algorithm);
		} catch (JWTCreationException exception) {
			throw new RegraDeNegocioException("Erro ao gerar o token Refresh Token");
		}
	}
	
	public String getSubject(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(CHAVE);
			JWTVerifier verifier = JWT.require(algorithm)
					.withIssuer(EMISSOR)
					.build();
			DecodedJWT decodedJWT = verifier.verify(token);
			return decodedJWT.getSubject();
		} catch (JWTVerificationException exception) {
			throw new RegraDeNegocioException("Erro ao verificar o token JWT de acesso");
		}
	}
	
	public boolean isJwtExpired(String tokenAcesso) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(CHAVE);
			JWTVerifier verifier = JWT.require(algorithm)
					.withIssuer(EMISSOR)
					.build();
			
			DecodedJWT decodedJWT = verifier.verify(tokenAcesso);
			Instant exp = decodedJWT.getExpiresAt().toInstant();
			return Instant.now().isAfter(exp);
		} catch (JWTVerificationException e) {
			return true;
		}
	}
	
	private Instant expiracao(int minutos) {
		return LocalDateTime.now().plusMinutes(minutos).toInstant(ZoneOffset.of("-03:00"));
	}
}
