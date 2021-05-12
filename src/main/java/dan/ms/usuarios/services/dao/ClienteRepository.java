package dan.ms.usuarios.services.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dan.ms.usuarios.domain.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer>{
	Cliente findByCuit(String cuit);
	Cliente findByRazonSocial(String razonSocial);
}
