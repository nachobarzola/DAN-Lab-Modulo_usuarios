package dan.ms.usuarios.services.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dan.ms.usuarios.domain.Cliente;
import dan.ms.usuarios.domain.Obra;
import dan.ms.usuarios.domain.TipoObra;

@Repository
public interface ObraRepository extends JpaRepository<Obra, Integer>{
	public List<Obra> findByCliente(Cliente cliente);
	
	public List<Obra> findByTipo(TipoObra tipoObra);
}
