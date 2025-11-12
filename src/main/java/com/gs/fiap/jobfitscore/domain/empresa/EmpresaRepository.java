package com.gs.fiap.jobfitscore.domain.empresa;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
	Optional<Empresa> findByEmailIgnoreCase(String email);
	Optional<Empresa> findByCnpj(String cnpj);
}
