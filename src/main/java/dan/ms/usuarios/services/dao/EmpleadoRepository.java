package dan.ms.usuarios.services.dao;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dan.ms.usuarios.domain.Empleado;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer>{

	
	@Query("SELECT DISTINCT e FROM Empleado e JOIN e.user u "
			+ "WHERE u.user=:userName")
	Optional<Empleado> findEmpleadoByUserName(String userName);
	
}
