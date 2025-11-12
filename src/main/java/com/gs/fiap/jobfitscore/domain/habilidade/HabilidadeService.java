package com.gs.fiap.jobfitscore.domain.habilidade;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HabilidadeService {
	
	private final HabilidadeRepository hR;
	
	public HabilidadeService(HabilidadeRepository hR) {
		this.hR = hR;
	}
	
	public List<HabilidadeDTO> listarHabilidades() {
		return hR.findAll()
				.stream()
				.map(HabilidadeDTO::fromEntity)
				.collect(Collectors.toList());
	}
	
	public HabilidadeDTO buscarHabilidadePorId(Long id) {
		Habilidade h = hR.findById(id)
				.orElseThrow(() -> new RuntimeException("Habilidade não encontrada"));
		return HabilidadeDTO.fromEntity(h);
	}
	
	@Transactional
	public HabilidadeDTO cadastrarHabilidade(HabilidadeDTO dto) {
		Habilidade h = dto.toEntity();
		return HabilidadeDTO.fromEntity(hR.save(h));
	}
	
	@Transactional
	public HabilidadeDTO atualizarHabilidade(Long id, HabilidadeDTO dto) {
		Habilidade h = hR.findById(id)
				.orElseThrow(() -> new RuntimeException("Habilidade não encontrada"));
		h.setNome(dto.getNome());
		return HabilidadeDTO.fromEntity(hR.save(h));
	}
	
	@Transactional
	public void deletarHabilidade(Long id) {
		hR.deleteById(id);
	}
}
