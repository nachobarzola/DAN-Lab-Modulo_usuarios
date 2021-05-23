package dan.ms.usuarios.rest;

import dan.ms.usuarios.domain.Obra;
import dan.ms.usuarios.domain.TipoObra;
import dan.ms.usuarios.services.interfaces.ObraService;
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

import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Autowired
	ObraService obraService;
	
	@PostMapping
	@ApiOperation(value="Crear una obra y se la asigna al cliente. El clinte tiene que estar en el json y contener un cuit")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Obra guardada y asignada a cliente"),
			@ApiResponse(code=400,message = "La obra no tiene cliente o el cliente no tiene cuit"),
			@ApiResponse(code=404,message = "Ocurrio un error al guardar la obra")
	})
	public ResponseEntity<Obra> crearObraYAsignarselaAlCliente(@RequestBody Obra obraNueva){
		if(obraNueva.getCliente() == null 
				&& (obraNueva.getCliente().getCuit() == null || obraNueva.getCliente().getCuit().isBlank())) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.of(obraService.crearObraYAsignarselaAlCliente(obraNueva));
	}
	
	@PutMapping(path= "/{id}")
	@ApiOperation(value="Actualiza una obra")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Obra actualizada correctamente"),
			@ApiResponse(code=404,message = "No se encontro la obra con ese ID")
	})
	public ResponseEntity<Obra> actualizar(@RequestBody Obra obra, @PathVariable Integer id){
		//seteamos el id de recibido en el path.
		obra.setId(id);
		Optional<Obra> optObraActualizada = obraService.actualizarObra(obra);
		if(optObraActualizada.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.of(optObraActualizada);
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
		return ResponseEntity.of(obraService.buscarObra(id));
	}
	
	//ResponseEntity<List<Obra>>
	@GetMapping
	@ApiOperation(value="Obtener obra por id del cliente y/o tipo de obra y/o cuit del cliente. Usando parametros opcionales")
	public ResponseEntity<List<Obra>> getObraPorClienteOTipo(@RequestParam(required=false) Integer idCliente, @RequestParam(required=false) Integer tipoObra,
			@RequestParam(required=false) String cuitCliente){
		
		List<Obra> respuesta1 = new ArrayList<>();
		List<Obra> respuesta2 = new ArrayList<>();
		List<Obra> respuesta3 = new ArrayList<>();
		//No se ingresa ningun parametro
		if(idCliente == null && tipoObra == null && cuitCliente == null) {
			return ResponseEntity.badRequest().build();
		}
		//Si se ingresa el parametro id_cliente
		if(idCliente != null) {
			respuesta1.addAll(obraService.buscarObraPorIdCliente(idCliente));
		}
		//Si se ingresa el parametro tipoObra
		if(tipoObra != null) {
			respuesta2.addAll(obraService.buscarObraPorTipoObra(tipoObra));
		}
		if(cuitCliente != null) {
			respuesta3.addAll(obraService.buscarObraPorCuitCliente(cuitCliente));
		}
		respuesta2.addAll(respuesta3);
		respuesta1.addAll(respuesta2);
		//Verificamos si hay elementos duplicados y los eliminamos.
		//Este caso se puede dar si se usan mutiples parametros y comparten los mismo valores
		List<Obra> respuestaFinal= respuesta1.stream()
				.distinct().collect(Collectors.toList());
		return ResponseEntity.ok(respuestaFinal);
	}
	
	
	
}
