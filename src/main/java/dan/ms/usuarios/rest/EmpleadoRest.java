package dan.ms.usuarios.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import dan.ms.usuarios.domain.Empleado;
import io.swagger.annotations.Api;


@RestController
@RequestMapping("/api/empleado")
@Api(value = "EmpleadoRest", description = "Permite gestionar los empleados de la empresa")
public class EmpleadoRest {
	private static final List<Empleado> listaEmpleado=new ArrayList<>();
	private static Integer ID_GEN = 1;
	
	
	@PostMapping
	public ResponseEntity<Empleado> crear(@RequestBody Empleado nuevoEmp){
		System.out.print("Se creo un nuevo empleado con:"+nuevoEmp.toString()+"\n");
		nuevoEmp.setId(ID_GEN++);
		listaEmpleado.add(nuevoEmp);
		return ResponseEntity.ok(nuevoEmp);
	}
	
	

}
