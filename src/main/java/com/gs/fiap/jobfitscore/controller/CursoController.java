package com.gs.fiap.jobfitscore.controller;

import com.gs.fiap.jobfitscore.domain.curso.CursoDTO;
import com.gs.fiap.jobfitscore.domain.curso.CursoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {
	
	private final CursoService cS;
	
	public CursoController(CursoService cS) {
		this.cS = cS;
	}
	
	@PostMapping("/cadastrar")
	public CursoDTO cadastrar(@Valid @RequestBody CursoDTO dto) {
		return cS.cadastrar(dto);
	}
	
	@GetMapping("/listar")
	public List<CursoDTO> listar() {
		return cS.listar();
	}
	
	@GetMapping("/buscar-por-id/{id}")
	public CursoDTO buscarPorId(@PathVariable Long id) {
		return cS.buscarPorId(id);
	}
	
	@GetMapping("/buscar-por-usuario/{usuarioId}")
	public List<CursoDTO> buscarPorUsuario(@PathVariable Long usuarioId) {
		return cS.buscarPorUsuario(usuarioId);
	}
	
	@PutMapping("/atualizar/{id}")
	public CursoDTO atualizar(@PathVariable Long id, @Valid @RequestBody CursoDTO dto) {
		return cS.atualizar(id, dto);
	}
	
	@DeleteMapping("/deletar/{id}")
	public void deletar(@PathVariable Long id) {
		cS.deletar(id);
	}
}
