package com.gs.fiap.jobfitscore.domain.usuario;

import com.gs.fiap.jobfitscore.infra.exception.RegraDeNegocioException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioService implements UserDetailsService {
	
	private final UsuarioRepository repository;
	private final PasswordEncoder encoder;
	
	public UsuarioService( UsuarioRepository repository, PasswordEncoder encoder ) {
		this.repository = repository;
		this.encoder = encoder;
	}
	
	public List<UsuarioDTO> listarUsuariosDTO() {
		return repository.findAll().stream().map( UsuarioDTO::fromEntity ).toList();
	}
	
	public UsuarioDTO buscarUsuarioPorIdDTO( Long id ) {
		Usuario usuario = repository.findById( id ).orElseThrow( () -> new RegraDeNegocioException( "Usuário de ID: " + id + " não encontrado." ) );
		return UsuarioDTO.fromEntity( usuario );
	}
	
	@Transactional
	public Usuario salvarUsuario( Usuario usuario ) {
		usuario.setSenha( encoder.encode( usuario.getSenha() ) );
		return repository.save( usuario );
	}
	
	@Transactional
	public Usuario atualizarUsuario( Long id, Usuario usuarioAtualizado ) {
		Usuario usuario = repository.findById( id ).orElseThrow( () -> new RegraDeNegocioException( "Usuário de ID: " + id + " não encontrado." ) );
		
		if ( usuarioAtualizado.getNome() != null && !usuarioAtualizado.getNome().isBlank() ) {
			usuario.setNome( usuarioAtualizado.getNome() );
		}
		
		if ( usuarioAtualizado.getEmail() != null && !usuarioAtualizado.getEmail().isBlank() ) {
			usuario.setEmail( usuarioAtualizado.getEmail() );
		}
		
		if ( usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isBlank() ) {
			usuario.setSenha( encoder.encode( usuarioAtualizado.getSenha() ) );
		}
		return repository.save( usuario );
	}
	
	@Transactional
	public void deletarUsuario( Long id ) {
		repository.deleteById( id );
	}
	
	@Override
	public UserDetails loadUserByUsername( String email ) throws UsernameNotFoundException {
		return repository.findByEmailIgnoreCase( email ).orElseThrow( () -> new UsernameNotFoundException( "Usuário não encontrado!" ) );
	}
	
	@Transactional
	public void atualizarRefreshTokenUsuario( Usuario usuario, String refreshToken, LocalDateTime expiracao ) {
		usuario.setRefreshToken( refreshToken );
		usuario.setExpiracaoRefreshToken( expiracao );
		repository.save( usuario );
	}
}
