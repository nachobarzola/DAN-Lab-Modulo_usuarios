package dan.ms.usuarios.services;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dan.ms.usuarios.domain.Empleado;
import dan.ms.usuarios.domain.TipoUsuario;
import dan.ms.usuarios.domain.Usuario;
import dan.ms.usuarios.services.dao.EmpleadoRepository;
import dan.ms.usuarios.services.interfaces.EmpleadoService;
import dan.ms.usuarios.services.interfaces.UsuarioService;



@Service
public class EmpleadoServiceImp implements EmpleadoService{

	private static final Logger logger = LoggerFactory.getLogger(EmpleadoServiceImp.class);

	@Autowired
	private EmpleadoRepository empleadoRepo;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Override
	public Optional<Empleado> guardarEmpleado(Empleado emp) {
		logger.info("Solicitud de guardado de empleado");
		try {
			// Usuario
			Usuario usuario = new Usuario();
			usuario.setUser(emp.getMail());
			//Password aleatoria
			Random random = new Random();
			String password="";
			for(int i=0; i<8;i++) {
				password += (char) (random.nextInt(26) + 'a');
			}
			usuario.setPassword(password);
			//
			usuario.setTipoUsuario(new TipoUsuario(2, "VENDEDOR"));
			
			// Guardamos el asuario del cliente
			Optional<Usuario> optUsuario = usuarioService.guardarUsuario(usuario);
			if (optUsuario.isEmpty()) {
				// No puedo guardar el usuario
				return Optional.empty();
			}
			// Asignamos el usuario guardado al empleado
			emp.setUser(optUsuario.get());
			
			empleadoRepo.saveAndFlush(emp);
			logger.debug("Se guardo correctamente el empleado");
			return Optional.of(emp);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Fallo al guardar el empleado");
			return Optional.empty();
		}
	}

	@Override
	public Optional<Empleado> actualizarEmpleado(Empleado emp) {
		logger.info("Solicitud de actualizacion de empleado");
		try {
			// Chequemos que ya exista
			if (emp.getId() != null) {
				Optional<Empleado> optEmpleadoBuscado = empleadoRepo.findById(emp.getId());
				if (optEmpleadoBuscado.isPresent()) {
					// Existe en la DB
					logger.debug("Existe el empleado con id: " + emp.getId());
					//Busco el usuario asociado
					emp.setUser(optEmpleadoBuscado.get().getUser());
					empleadoRepo.saveAndFlush(emp);
					return Optional.of(emp);
				} else {
					// No existe en la DB
					logger.error("El empleado con la id: " + emp.getId() + " no existe en la DB");
					throw new IllegalArgumentException(
							"El empleado con la id: " + emp.getId() + " no existe en la DB");
				}
			} else {
				// Id nula
				logger.error("La id recibida es nula");
				throw new IllegalArgumentException("La id recibida es nula");
			}

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			logger.error("El empleado no pudo ser actualizado");
			return Optional.empty();
		}
	}
	
	@Override
	public Optional<Empleado> getEmpleado(Integer idEmpleado) {
		logger.info("Solicitud de obtenciion del empleado: " + idEmpleado);
		try {
			Optional<Empleado> optEmpleado = empleadoRepo.findById(idEmpleado);
			// Chequemos si la encontro
			if (optEmpleado.isEmpty()) {
				throw new Exception("No se encontro el empleado con la id: " + idEmpleado + " en la DB");
			}
			logger.debug("Se encontro el empleado con la id: " + idEmpleado);
			return optEmpleado;

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			logger.error("La id recibida es null");
			return Optional.empty();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("No se encontro el empleado con la id: " + idEmpleado + " en la DB");
			return Optional.empty();
		}
	}

	@Override
	public Optional<Empleado> removeEmpleado(Empleado emp) {
		logger.info("Solicitud de borrado del empleado: " + emp.toString());

		try {
			if (getEmpleado(emp.getId()).isEmpty()) {
				logger.error("No existe el empleado con id: " + emp.getId() + " en la base de datos");
				throw new IllegalArgumentException(
						"No existe el empleado con id: " + emp.getId() + " en la base de datos");
			}

			empleadoRepo.deleteById(emp.getId());
			logger.debug("Se borro correctamente el empleado: " + emp.toString());
			return Optional.of(emp);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("No se pudo remover el empleado: " + emp.toString());
			return Optional.empty();

		}
	}

	@Override
	public Optional<Empleado> getEmpleadoPorNombreDeUsuario(String nombreDeUsuario) {
		logger.info("Solicitud de obtenencio empleado dado el nombre: " + nombreDeUsuario);
		try {
			Optional<Empleado> optEmpleado = empleadoRepo.findEmpleadoByUserName(nombreDeUsuario);
			// Chequemos si la encontro
			if (optEmpleado.isEmpty()) {
				throw new Exception("No se encontro el empleado con el nombre de usuario: " + nombreDeUsuario + " en la DB");
			}
			logger.debug("Se encontro el empleado con el nombre de usuario: " + nombreDeUsuario);
			return optEmpleado;

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			logger.error("El nombre de usuario recibido es null");
			return Optional.empty();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("No se encontro el empleado con el nombre de usuario: " + nombreDeUsuario + " en la DB");
			return Optional.empty();
		}
	}

	@Override
	public List<Empleado> getAll() {
		logger.info("Solicitud de obtencion de todos los empleados: ");
		return empleadoRepo.findAll();
	}

	
	
	
	
	
}
