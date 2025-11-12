package com.gs.fiap.jobfitscore.controller;

import com.gs.fiap.jobfitscore.domain.autenticacao.DadosLogin;
import com.gs.fiap.jobfitscore.domain.autenticacao.DadosToken;
import com.gs.fiap.jobfitscore.domain.autenticacao.TokenService;
import com.gs.fiap.jobfitscore.domain.usuario.Usuario;
import com.gs.fiap.jobfitscore.domain.usuario.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping( "/api/autenticacao" )
public class AutenticacaoController {
	
	private final AuthenticationManager aM;
	private final TokenService tS;
	private final UsuarioService uS;
	
	public AutenticacaoController( AuthenticationManager aM, TokenService tS, UsuarioService uS ) {
		this.aM = aM;
		this.tS = tS;
		this.uS = uS;
	}
	
	@PostMapping( "/login" )
	public ResponseEntity<DadosToken> efetuarLogin ( @Valid @RequestBody DadosLogin dados ) {
		var authToken = new UsernamePasswordAuthenticationToken( dados.email(), dados.senha() );
		var authentication = aM.authenticate( authToken );
		
		var usuario = (Usuario) authentication.getPrincipal();
		
		String tokenAcesso = tS.gerarToken( usuario );
		String refreshToken = tS.gerarRefreshToken( usuario );
		LocalDateTime expiracaoRefresh = LocalDateTime.now().plusMinutes( 10080 );
		
		uS.atualizarRefreshTokenUsuario( usuario, refreshToken, expiracaoRefresh );
		
		return ResponseEntity.ok( new DadosToken( tokenAcesso, refreshToken, expiracaoRefresh ) );
	}
}
