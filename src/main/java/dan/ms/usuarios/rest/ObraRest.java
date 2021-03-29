package dan.ms.usuarios.rest;

import dan.ms.usuarios.domain.Obra;
import dan.ms.usuarios.domain.TipoObra;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collector;
import java.util.stream.Collectors;
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

	@GetMapping(path= "/{id}")
	@ApiOperation(value="Obtener obra por Id")
	public ResponseEntity<Obra> getPorId(@PathVariable Integer id){
		Optional<Obra> obra = listaObra.stream()
				.filter(unaObra -> unaObra.getId().equals(id))
				.findFirst();
		return ResponseEntity.of(obra);
	}
	
	//ResponseEntity<List<Obra>>
	@GetMapping
	@ApiOperation(value="Obtener obra por cliente y/o tipo de obra. Usando parametros opcionales")
	public ResponseEntity<List<Obra>> getObraPorClienteOTipo(@RequestParam(required=false) Integer id_cliente, @RequestParam(required=false) Integer tipoObra){
		
		//Si se ingresa el parametro id_cliente pero no el tipoObra
		if(id_cliente != null && tipoObra == null) {
			return ResponseEntity.ok(filtrarListaPorIdCliente(id_cliente));
		}
		//Si no se ingresa el parametro id_cliente pero si el tipoObra
		else if(id_cliente == null && tipoObra != null) {
			return ResponseEntity.ok(filtrarListaPorTipoObra(tipoObra));
		}
		//Si ingresan ambos parametros
		else if(id_cliente != null && tipoObra != null){
			List<Obra> listAux;
			listAux = filtrarListaPorIdCliente(id_cliente);
			listAux.addAll(filtrarListaPorTipoObra(tipoObra));
			return ResponseEntity.ok(listAux);
		}
		//No se ingresa ningun parametro
		else {
			return ResponseEntity.ok().build();
		}
	}
	
	
	
	//METODOS AUXILIARES 
	//no pertenecen a la API REST
	private List<Obra> filtrarListaPorIdCliente(Integer id_cliente){
		List<Obra> listaFiltrada= new ArrayList<>();
		listaFiltrada = listaObra.stream()
				.filter(unaObra -> unaObra.getCliente().getId().equals(id_cliente)).collect(Collectors.toList());
		return listaFiltrada;
	}
	private List<Obra> filtrarListaPorTipoObra(Integer tipoObra){
		List<Obra> listaFiltrada= new ArrayList<>();
		listaFiltrada = listaObra.stream()
				.filter(unaObra -> unaObra.getTipo().getId().equals(tipoObra)).collect(Collectors.toList());
		return listaFiltrada;
	}
	
	
}
