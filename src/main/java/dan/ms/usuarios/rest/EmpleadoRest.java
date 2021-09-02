package dan.ms.usuarios.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dan.ms.usuarios.domain.Cliente;
import dan.ms.usuarios.domain.Empleado;
import dan.ms.usuarios.services.interfaces.EmpleadoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/empleado")
@CrossOrigin("*")
@Api(value = "EmpleadoRest", description = "Permite gestionar los empleados de la empresa")
public class EmpleadoRest {
	private static final Logger logger = LoggerFactory.getLogger(EmpleadoRest.class);

	@Autowired
	EmpleadoService empleadoService;

	@PostMapping
	@ApiOperation(value = "Crear un empleado")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Guardado correctamente"),
			@ApiResponse(code = 404, message = "No se pudo guardar") })
	public ResponseEntity<Empleado> crear(@RequestBody Empleado nuevoEmp) {
		logger.info("Se creo un nuevo empleado con:" + nuevoEmp.toString() + "\n");
		return ResponseEntity.of(empleadoService.guardarEmpleado(nuevoEmp));
	}

	@PutMapping(path = "/{id}")
	@ApiOperation(value = "Actualizar empleado")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Actualizado correctamente"),
			@ApiResponse(code = 401, message = "No autorizado"), @ApiResponse(code = 403, message = "Prohibido"),
			@ApiResponse(code = 404, message = "No se pudo actualizar") })
	public ResponseEntity<Empleado> actualizarEmpleado(@RequestBody Empleado empleado, @PathVariable Integer id) {
		empleado.setId(id);
		return ResponseEntity.of(empleadoService.actualizarEmpleado(empleado));
	}

	@DeleteMapping(path = "/{idEmpleado}")
	@ApiOperation(value = "Eliminar empleado")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Eliminado correctamente"),
			@ApiResponse(code = 400, message = "El empleado no existe"),
			@ApiResponse(code = 401, message = "No autorizado"), 
			@ApiResponse(code = 403, message = "Prohibido"),
			@ApiResponse(code = 404, message = "No se pudo eliminar") })
	public ResponseEntity<Empleado> borrar(@PathVariable Integer idEmpleado) {
		// Chequeamos que la sucursal exista
		Optional<Empleado> optSucursalAActualizar = empleadoService.getEmpleado(idEmpleado);
		if (optSucursalAActualizar.isPresent()) {
			// El empledo existe
			return ResponseEntity.of(empleadoService.removeEmpleado(optSucursalAActualizar.get()));
		} else {
			// La empleado no existe
			return ResponseEntity.badRequest().build();
		}

	}

	@GetMapping("/{id}")
	@ApiOperation(value = "Obtener empleado dada una id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Obtuvo correctamente"),
			@ApiResponse(code = 404, message = "El ID no existe") })
	public ResponseEntity<Empleado> getPorId(@PathVariable Integer id) {
		return ResponseEntity.of(empleadoService.getEmpleado(id));
	}

	@GetMapping
	@ApiOperation(value = "Obtener todos los empleados o por su nombre usando en el path: ?nombreUsuario=")
	public ResponseEntity<List<Empleado>> getAll_o_PorNombre(@RequestParam(required = false) String nombreUsuario) {
		if(nombreUsuario == null){
			//Get all
			return ResponseEntity.ok(empleadoService.getAll());
		}
		else {
			List<Empleado> aux = new ArrayList<>();
			aux.add(empleadoService.getEmpleadoPorNombreDeUsuario(nombreUsuario).get());
			return ResponseEntity.ok(aux);

		}
	}
	


}
