package dan.ms.usuarios.services.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dan.ms.usuarios.domain.Obra;

@Repository
public interface ObraRepository extends JpaRepository<Obra, Integer>{

}
