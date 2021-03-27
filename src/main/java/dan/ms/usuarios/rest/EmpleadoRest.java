package dan.ms.usuarios.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import dan.ms.usuarios.domain.Empleado;
import dan.ms.usuarios.domain.Obra;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@RequestMapping("/api/empleado")
@Api(value = "EmpleadoRest", description = "Permite gestionar los empleados de la empresa")
public class EmpleadoRest {
	private static final List<Empleado> listaEmpleado=new ArrayList<>();
	private static Integer ID_GEN = 1;
	
	
	@PostMapping
	@ApiOperation(value = "Crear un empleado")
	public ResponseEntity<Empleado> crear(@RequestBody Empleado nuevoEmp){
		System.out.print("Se creo un nuevo empleado con:"+nuevoEmp.toString()+"\n");
		nuevoEmp.setId(ID_GEN++);
		listaEmpleado.add(nuevoEmp);
		return ResponseEntity.ok(nuevoEmp);
	}
	
	@PutMapping(path = "/{id}")
	@ApiOperation(value = "Actualizar empleado")
	@ApiResponses(value = {
    @ApiResponse(code = 200, message = "Actualizado correctamente"),
    @ApiResponse(code = 401, message = "No autorizado"),
    @ApiResponse(code = 403, message = "Prohibido"),
    @ApiResponse(code = 404, message = "El ID no existe")
	})
	public ResponseEntity<Empleado> actualizar(@RequestBody Empleado empleado, @PathVariable Integer id){
		OptionalInt index = IntStream.range(0,listaEmpleado.size())
				.filter(i -> listaEmpleado.get(i).getId().equals(id))
				.findFirst();
		if(index.isPresent()) {
			listaEmpleado.set(index.getAsInt(),empleado);
			return ResponseEntity.ok(empleado);
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping(path= "/{id}")
	@ApiOperation(value= "Eliminar empleado")
	public ResponseEntity<Empleado> borrar(@PathVariable Integer id){
		OptionalInt index = IntStream.range(0, listaEmpleado.size())
				.filter(i -> listaEmpleado.get(i).getId().equals(id))
				.findFirst();
		if(index.isPresent()){
			listaEmpleado.remove(index.getAsInt());
			return ResponseEntity.ok().build();
		}
		else {
			return ResponseEntity.notFound().build();
		}
		
	}
	@GetMapping("/{id}")
	@ApiOperation(value= "Obtener empleado dada una id")
	@ApiResponses(value= {
			@ApiResponse(code= 200, message= "Obtuvo correctamente"),
			@ApiResponse(code= 404, message= "El ID no existe")
	})
	public ResponseEntity<Empleado> getPorId(@PathVariable Integer id){
		Optional<Empleado> empleado = listaEmpleado.stream()
				.filter(unEmpleado -> unEmpleado.getId().equals(id))
				.findFirst();
		return ResponseEntity.of(empleado);
	}
	@GetMapping
	@ApiOperation(value= "Obtener empleado dada su nombre de usuario")
	public ResponseEntity<Empleado> getPorNombre(@RequestParam(required= false) String nombre){
		Optional<Empleado> empleado = listaEmpleado.stream()
				.filter(unEmpleado -> unEmpleado.getUser().getUser().equals(nombre))
				.findFirst();
		return ResponseEntity.of(empleado);
	}
	
	
	
	
	
	

}
