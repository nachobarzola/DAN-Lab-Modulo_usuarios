package dan.ms.usuarios.services.dao;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dan.ms.usuarios.domain.Empleado;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer>{

	//@Query("select u from User u where u.firstname like %?1")
	/*@Query("SELECT e FROM empleado e JOIN usuario u ON e.id_usuario=u.id_usuario "
			+ "WHERE u.user=?1", nativeQuery=true)*/
	
	/*@Query("SELECT * FROM Empleado e, Usuario u"
			+ "WHERE e.user=u"
			+ "AND u.user=?userName")*/
	
	@Query("SELECT DISTINCT e FROM Empleado e JOIN e.user u "
			+ "WHERE u.user=:userName")
	Optional<Empleado> findEmpleadoByUserName(String userName);
	
}
