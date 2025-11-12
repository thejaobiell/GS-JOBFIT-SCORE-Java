package com.gs.fiap.jobfitscore.controller;

import com.gs.fiap.jobfitscore.domain.vaga.VagaDTO;
import com.gs.fiap.jobfitscore.domain.vaga.VagaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vagas")
public class VagaController {
	
	private final VagaService vS;
	
	public VagaController(VagaService vS) {
		this.vS = vS;
	}
	
	@GetMapping("/listar")
	public List<VagaDTO> listar() {
		return vS.listarVagas();
	}
	
	@GetMapping("/buscar-por-id/{id}")
	public VagaDTO buscar(@PathVariable Long id) {
		return vS.buscarVagaPorId(id);
	}
	
	@PostMapping("/cadastrar")
	public VagaDTO criar(@Valid @RequestBody VagaDTO vagaDTO) {
		return vS.cadastrarVaga(vagaDTO);
	}
	
	@PutMapping("/atualizar/{id}")
	public VagaDTO atualizar(@PathVariable Long id, @Valid @RequestBody VagaDTO vagaDTO) {
		return vS.atualizarVaga(id, vagaDTO);
	}
	
	@DeleteMapping("/deletar/{id}")
	public void deletar(@PathVariable Long id) {
		vS.deletarVaga(id);
	}
}
