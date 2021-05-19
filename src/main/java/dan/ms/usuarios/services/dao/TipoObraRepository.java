package dan.ms.usuarios.services.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dan.ms.usuarios.domain.TipoObra;

@Repository
public interface TipoObraRepository extends JpaRepository<TipoObra, Integer>{

}
