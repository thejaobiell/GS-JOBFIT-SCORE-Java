package com.gs.fiap.jobfitscore.controller;

import com.gs.fiap.jobfitscore.domain.usuariohabilidade.UsuarioHabilidadeDTO;
import com.gs.fiap.jobfitscore.domain.usuariohabilidade.UsuarioHabilidadeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuario-habilidade")
public class UsuarioHabilidadeController {
	
	private final UsuarioHabilidadeService uhS;
	
	public UsuarioHabilidadeController(UsuarioHabilidadeService uhS) {
		this.uhS = uhS;
	}
	
	@GetMapping("/listar")
	public List<UsuarioHabilidadeDTO> listar() {
		return uhS.listar();
	}
	
	@GetMapping("/buscar-por-id/{id}")
	public UsuarioHabilidadeDTO buscar(@PathVariable Long id) {
		return uhS.buscarPorId(id);
	}
	
	@GetMapping("/buscar-por-usuario/{usuarioId}")
	public List<UsuarioHabilidadeDTO> buscarPorUsuario(@PathVariable Long usuarioId) {
		return uhS.buscarPorUsuario(usuarioId);
	}
	
	@PostMapping("/cadastrar")
	public UsuarioHabilidadeDTO cadastrar(@Valid @RequestBody UsuarioHabilidadeDTO dto) {
		return uhS.cadastrar(dto);
	}
	
	@DeleteMapping("/deletar/{id}")
	public void deletar(@PathVariable Long id) {
		uhS.deletar(id);
	}
}
