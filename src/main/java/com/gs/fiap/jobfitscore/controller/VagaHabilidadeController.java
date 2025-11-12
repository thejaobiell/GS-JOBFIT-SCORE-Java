package com.gs.fiap.jobfitscore.controller;

import com.gs.fiap.jobfitscore.domain.vagahabilidade.VagaHabilidadeDTO;
import com.gs.fiap.jobfitscore.domain.vagahabilidade.VagaHabilidadeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vaga-habilidade")
public class VagaHabilidadeController {
	
	private final VagaHabilidadeService vhS;
	
	public VagaHabilidadeController(VagaHabilidadeService vhS) {
		this.vhS = vhS;
	}
	
	@PostMapping("/cadastrar")
	public VagaHabilidadeDTO cadastrar(@RequestBody VagaHabilidadeDTO dto) {
		return vhS.cadastrar(dto);
	}
	
	@GetMapping("/listar")
	public List<VagaHabilidadeDTO> listar() {
		return vhS.listar();
	}
	
	@GetMapping("/buscar-por-vaga")
	public List<VagaHabilidadeDTO> buscarPorVaga(@RequestParam Long vagaId) {
		return vhS.buscarPorVaga(vagaId);
	}
	
	@GetMapping("/buscar-por-habilidade")
	public List<VagaHabilidadeDTO> buscarPorHabilidade(@RequestParam Long habilidadeId) {
		return vhS.buscarPorHabilidade(habilidadeId);
	}
	
	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		vhS.deletar(id);
		return ResponseEntity.noContent().build();
	}
}
