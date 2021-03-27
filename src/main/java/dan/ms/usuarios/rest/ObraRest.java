package dan.ms.usuarios.rest;

import dan.ms.usuarios.domain.Obra;
import dan.ms.usuarios.domain.TipoObra;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/obra")
public class ObraRest {
	private static final List<Obra> listaObra= new ArrayList<>();
	private static Integer ID_GEN = 1;
	
	@PostMapping
	@ApiOperation(value="Crear una obra")
	public ResponseEntity<Obra> crear(@RequestBody Obra obraNueva){
		System.out.print("Se creo nueva obra: "+ obraNueva.toString()+"\n");
		obraNueva.setId(ID_GEN++);
		listaObra.add(obraNueva);
		return ResponseEntity.ok(obraNueva);
	}
	
	@PutMapping(path= "/{id}")
	@ApiOperation(value="Actualiza una obra")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Obra actualizada correctamente"),
			@ApiResponse(code=404,message = "No se encontro la obra con ese ID")
	})
	public ResponseEntity<Obra> actualizar(@RequestBody Obra obra, @PathVariable Integer id){
		OptionalInt index = IntStream.range(0,listaObra.size())
				.filter(i -> listaObra.get(i).getId().equals(id))
				.findFirst();
		if(index.isPresent()) {
			listaObra.set(index.getAsInt(), obra);
			return ResponseEntity.ok(obra);
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping(path="/{id}")
	@ApiOperation(value= "Borrar una obra")
	public ResponseEntity<Obra> borrar(@PathVariable Integer id){
		OptionalInt index = IntStream.range(0, listaObra.size())
				.filter(i -> listaObra.get(i).getId().equals(id))
				.findFirst();
		if(index.isPresent()) {
			listaObra.remove(index.getAsInt());
			return ResponseEntity.ok().build();
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}

	
	
	
	
}
