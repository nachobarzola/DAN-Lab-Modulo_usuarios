package dan.ms.usuarios.services.interfaces;

import java.util.List;
import java.util.Optional;

import dan.ms.usuarios.domain.Empleado;

public interface EmpleadoService {
	public Optional<Empleado> guardarEmpleado(Empleado emp);
	
	public Optional<Empleado> getEmpleado(Integer idEmpleado);
	
	public Optional<Empleado> getEmpleadoPorNombreDeUsuario(String nombreEmpleado);
	
	public Optional<Empleado> actualizarEmpleado(Empleado emp);
	
	public Optional<Empleado> removeEmpleado(Empleado emp);
	
	public List<Empleado> getAll();
	
	
}
