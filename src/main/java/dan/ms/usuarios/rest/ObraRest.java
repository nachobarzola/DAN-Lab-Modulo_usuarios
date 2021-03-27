package dan.ms.usuarios.rest;

import dan.ms.usuarios.domain.Obra;
import dan.ms.usuarios.domain.TipoObra;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/obra")
public class ObraRest {
	private static final List<Obra> listaObras= new ArrayList<>();
	private static Integer ID_GEN = 1;
	
	@PostMapping
	@ApiOperation(value="Crear una obra")
	public ResponseEntity<Obra> crear(@RequestBody Obra obraNueva){
		System.out.print("Se creo nueva obra: "+ obraNueva.toString()+"\n");
		obraNueva.setId(ID_GEN++);
		listaObras.add(obraNueva);
		return ResponseEntity.ok(obraNueva);
	}
	
	
	
}
