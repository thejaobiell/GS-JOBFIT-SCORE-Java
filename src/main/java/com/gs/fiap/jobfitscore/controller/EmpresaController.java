package com.gs.fiap.jobfitscore.controller;

import com.gs.fiap.jobfitscore.domain.empresa.Empresa;
import com.gs.fiap.jobfitscore.domain.empresa.EmpresaDTO;
import com.gs.fiap.jobfitscore.domain.empresa.EmpresaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {
	
	private final EmpresaService eS;
	
	public EmpresaController(EmpresaService eS) {
		this.eS = eS;
	}
	
	@GetMapping("/listar")
	public List<EmpresaDTO> listar() {
		return eS.listarEmpresas();
	}
	
	@GetMapping("/buscar-por-id/{id}")
	public EmpresaDTO buscarID(@PathVariable Long id) {
		return eS.buscarEmpresaPorId(id);
	}
	
	@GetMapping("/buscar-por-cnpj")
	public EmpresaDTO buscarCNPJ(@RequestParam String cnpj) {
		return eS.buscarEmpresaPorCNPJ(cnpj);
	}
	
	@PostMapping("/cadastrar")
	public EmpresaDTO criar(@RequestBody Empresa empresa) {
		return eS.salvarEmpresa(empresa);
	}
	
	@PutMapping("/atualizar/{id}")
	public EmpresaDTO atualizar(@PathVariable Long id, @RequestBody Empresa empresa) {
		return eS.atualizarEmpresa(id, empresa);
	}
	
	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		eS.deletarEmpresa(id);
		return ResponseEntity.noContent().build();
	}
}
