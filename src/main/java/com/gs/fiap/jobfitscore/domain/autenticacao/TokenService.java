package com.gs.fiap.jobfitscore.domain.autenticacao;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gs.fiap.jobfitscore.domain.usuario.Usuario;
import com.gs.fiap.jobfitscore.infra.exception.RegraDeNegocioException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
	
	public String gerarToken( Usuario usuario ) {
		try {
			Algorithm algorithm = Algorithm.HMAC256( "joao-gabriel-lucas-leal-leo-mota" );
			return JWT.create()
					.withIssuer( "JobFit-Score" )
					.withSubject( usuario.getUsername() )
					.withExpiresAt( expiracao( 120 ) )
					.sign( algorithm );
		} catch ( JWTCreationException exception ) {
			throw new RegraDeNegocioException( "Erro ao gerar o token JWT de acesso" );
		}
	}
	
	public String gerarRefreshToken( Usuario usuario ) {
		try {
			Algorithm algorithm = Algorithm.HMAC256( "joao-gabriel-lucas-leal-leo-mota" );
			return JWT.create()
					.withIssuer( "JobFit-Score" )
					.withSubject( usuario.getId().toString() )
					.withExpiresAt( expiracao( 10080 ) )
					.sign( algorithm );
		} catch ( JWTCreationException exception ) {
			throw new RegraDeNegocioException( "Erro ao gerar o token Refresh Token" );
		}
	}
	
	public String getSubject( String token ) {
		DecodedJWT decodedJWT;
		try {
			Algorithm algorithm = Algorithm.HMAC256( "joao-gabriel-lucas-leal-leo-mota" );
			JWTVerifier verifier = JWT.require( algorithm )
					.withIssuer( "JobFit-Score" )
					.build();
			
			decodedJWT = verifier.verify( token );
			return decodedJWT.getSubject();
		} catch ( JWTVerificationException exception ) {
			throw new RegraDeNegocioException( "Erro ao verificar o token JWT de acesso" );
		}
	}
	
	public boolean isJwtExpired( String tokenAcesso ) {
		try {
			Algorithm algorithm = Algorithm.HMAC256("joao-gabriel-lucas-leal-leo-mota");
			JWTVerifier verifier = JWT.require( algorithm )
					.withIssuer( "JobFit-Score" )
					.build();
			
			DecodedJWT decodedJWT = verifier.verify( tokenAcesso );
			Instant exp = decodedJWT.getExpiresAt().toInstant();
			Instant now = Instant.now();
			
			return now.isAfter( exp );
		} catch ( JWTVerificationException e ) {
			return true;
		}
	}
	
	private Instant expiracao( int minutos ) {
		return LocalDateTime.now().plusMinutes( minutos ).toInstant( ZoneOffset.of( "-03:00" ) );
	}
}
