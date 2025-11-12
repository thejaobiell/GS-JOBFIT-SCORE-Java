package com.gs.fiap.jobfitscore.domain.usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
	
	private Long id;
	private String nome;
	private String email;
	private String refreshToken;
	private LocalDateTime expiracaoRefreshToken;
	
	public static UsuarioDTO fromEntity(Usuario usuario) {
		return new UsuarioDTO(
				usuario.getId(),
				usuario.getNome(),
				usuario.getEmail(),
				usuario.getRefreshToken(),
				usuario.getExpiracaoRefreshToken()
		);
	}
}
