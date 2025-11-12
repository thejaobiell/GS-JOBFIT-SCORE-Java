package com.gs.fiap.jobfitscore.domain.usuario;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Long id;
	
	@Column(nullable = false, length = 100)
	private String nome;
	
	@Column(nullable = false, unique = true, length = 100)
	private String email;
	
	@Column(nullable = false, length = 200)
	private String senha;
	
	@Column(name = "refresh_token", nullable = true)
	private String refreshToken;
	
	@Column(name = "expira_refresh_token", nullable = true)
	private LocalDateTime expiracaoRefreshToken;
	
	public void setId(Long id) { this.id = id; }
	
	public void setNome(String nome) { this.nome = nome; }
	
	public void setEmail(String email) { this.email = email; }
	
	public void setSenha(String senha) { this.senha = senha; }
	
	public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
	
	public void setExpiracaoRefreshToken(LocalDateTime expiracaoRefreshToken) { this.expiracaoRefreshToken = expiracaoRefreshToken; }
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of();
	}
	
	@Override
	public String getPassword() {
		return senha;
	}
	
	@Override
	public String getUsername() {
		return email;
	}
	
	public String novoRefreshToken() {
		this.refreshToken = UUID.randomUUID().toString();
		this.expiracaoRefreshToken = LocalDateTime.now().plusMinutes( 120 );
		return refreshToken;
	}
	
	public boolean refreshTokenExpirado() {
		return expiracaoRefreshToken.isBefore( LocalDateTime.now() );
	}
}

