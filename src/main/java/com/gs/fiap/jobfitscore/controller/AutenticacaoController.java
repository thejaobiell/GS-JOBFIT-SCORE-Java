package com.gs.fiap.jobfitscore.controller;

import com.gs.fiap.jobfitscore.domain.autenticacao.DadosLogin;
import com.gs.fiap.jobfitscore.domain.autenticacao.DadosToken;
import com.gs.fiap.jobfitscore.domain.autenticacao.TokenService;
import com.gs.fiap.jobfitscore.domain.empresa.Empresa;
import com.gs.fiap.jobfitscore.domain.empresa.EmpresaService;
import com.gs.fiap.jobfitscore.domain.usuario.Usuario;
import com.gs.fiap.jobfitscore.domain.usuario.UsuarioService;
import com.gs.fiap.jobfitscore.infra.exception.RegraDeNegocioException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/autenticacao")
public class AutenticacaoController {
	
	private static final Logger log = LoggerFactory.getLogger(AutenticacaoController.class);
	
	private final AuthenticationManager aM;
	private final TokenService tS;
	private final UsuarioService uS;
	private final EmpresaService eS;
	
	public AutenticacaoController( AuthenticationManager aM, TokenService tS,
	                               UsuarioService uS, EmpresaService eS) {
		this.aM = aM;
		this.tS = tS;
		this.uS = uS;
		this.eS = eS;
	}
	
	@PostMapping("/login")
	public ResponseEntity<DadosToken> efetuarLogin(@Valid @RequestBody DadosLogin dados) {
		try {
			// Autentica o usuário
			var authToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
			Authentication authentication = aM.authenticate(authToken);
			
			Object principal = authentication.getPrincipal();
			
			String tokenAcesso;
			String refreshToken;
			LocalDateTime expiracaoRefresh = LocalDateTime.now().plusMinutes(10080);
			
			if (principal instanceof Usuario usuario) {
				tokenAcesso = tS.gerarToken(usuario);
				refreshToken = tS.gerarRefreshToken(usuario);
				uS.atualizarRefreshTokenUsuario(usuario, refreshToken, expiracaoRefresh);
				
			} else if (principal instanceof Empresa empresa) {
				tokenAcesso = tS.gerarToken(empresa);
				refreshToken = tS.gerarRefreshToken(empresa);
				eS.atualizarRefreshTokenEmpresa(empresa, refreshToken, expiracaoRefresh);
				
			} else {
				throw new RegraDeNegocioException("Tipo de autenticação desconhecido");
			}
			
			return ResponseEntity.ok(new DadosToken(tokenAcesso, refreshToken, expiracaoRefresh));
			
		} catch (Exception e) {
			log.error("Erro ao efetuar login: {}", e.getMessage());
			throw e;
		}
	}
}