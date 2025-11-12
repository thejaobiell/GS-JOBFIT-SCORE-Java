package com.gs.fiap.jobfitscore.controller;
import com.gs.fiap.jobfitscore.domain.usuario.Usuario;
import com.gs.fiap.jobfitscore.domain.usuario.UsuarioDTO;
import com.gs.fiap.jobfitscore.domain.usuario.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
	
	private final UsuarioService uS;
	
	public UsuarioController( UsuarioService uS ) {
		this.uS = uS;
	}
	
	@GetMapping( "/listar" )
	public List<UsuarioDTO> listar() {
		return uS.listarUsuarios();
	}
	
	@GetMapping("/buscar-por-id/{id}")
	public UsuarioDTO buscar(@PathVariable Long id) {
		return uS.buscarUsuarioPorId(id);
	}
	
	@PostMapping("/cadastrar")
	public UsuarioDTO criar(@RequestBody Usuario usuario) {
		return uS.salvarUsuario(usuario);
	}
	
	@PutMapping("/atualizar/{id}")
	public UsuarioDTO atualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
		return uS.atualizarUsuario(id, usuario);
	}
	
	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<Void> excluir( @PathVariable Long id) {
		uS.deletarUsuario(id);
		return ResponseEntity.noContent().build();
	}
}
