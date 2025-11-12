package com.gs.fiap.jobfitscore.controller;

import com.gs.fiap.jobfitscore.domain.habilidade.HabilidadeDTO;
import com.gs.fiap.jobfitscore.domain.habilidade.HabilidadeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habilidades")
public class HabilidadeController {
	
	private final HabilidadeService hS;
	
	public HabilidadeController(HabilidadeService hS) {
		this.hS = hS;
	}
	
	@GetMapping("/listar")
	public List<HabilidadeDTO> listar() {
		return hS.listarHabilidades();
	}
	
	@GetMapping("/buscar-por-id/{id}")
	public HabilidadeDTO buscar(@PathVariable Long id) {
		return hS.buscarHabilidadePorId(id);
	}
	
	@PostMapping("/cadastrar")
	public HabilidadeDTO criar(@Valid @RequestBody HabilidadeDTO dto) {
		return hS.cadastrarHabilidade(dto);
	}
	
	@PutMapping("/atualizar/{id}")
	public HabilidadeDTO atualizar(@PathVariable Long id, @Valid @RequestBody HabilidadeDTO dto) {
		return hS.atualizarHabilidade(id, dto);
	}
	
	@DeleteMapping("/deletar/{id}")
	public void deletar(@PathVariable Long id) {
		hS.deletarHabilidade(id);
	}
}
