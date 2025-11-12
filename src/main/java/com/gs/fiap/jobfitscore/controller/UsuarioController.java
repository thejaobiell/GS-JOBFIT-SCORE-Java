package com.gs.fiap.jobfitscore.controller;
import com.gs.fiap.jobfitscore.domain.usuario.Usuario;
import com.gs.fiap.jobfitscore.domain.usuario.UsuarioDTO;
import com.gs.fiap.jobfitscore.domain.usuario.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
	
	private final UsuarioService uS;
	
	public UsuarioController( UsuarioService uS ) {
		this.uS = uS;
	}
	
	@GetMapping("/listar")
	public ResponseEntity<Map<String, Object>> listarUsuarios(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id") String sortBy) {
		
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
		Page<UsuarioDTO> pageUsuarios = uS.listarUsuarios(pageable);
		
		Map<String, Object> response = new HashMap<>();
		response.put("content", pageUsuarios.getContent());
		response.put("currentPage", pageUsuarios.getNumber());
		response.put("totalItems", pageUsuarios.getTotalElements());
		response.put("totalPages", pageUsuarios.getTotalPages());
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/buscar-por-id/{id}")
	public UsuarioDTO buscarPorID(@PathVariable Long id) {
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
	public ResponseEntity<Void> deletar( @PathVariable Long id) {
		uS.deletarUsuario(id);
		return ResponseEntity.noContent().build();
	}
}
