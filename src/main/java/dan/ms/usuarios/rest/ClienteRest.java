package dan.ms.usuarios.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
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

import dan.ms.usuarios.domain.Cliente;
import dan.ms.usuarios.domain.Obra;
import dan.ms.usuarios.domain.Usuario;
import dan.ms.usuarios.services.ClientServiceImp;
import dan.ms.usuarios.services.interfaces.ClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/cliente")
@Api(value = "ClienteRest", description = "Permite gestionar los clientes de la empresa")
public class ClienteRest {

	@Autowired
	private ClientService clientService;

	private static final List<Cliente> listaClientesAntigua = new ArrayList<>();
	private static Integer ID_GEN = 1;

	@GetMapping(path = "/{id}")
	@ApiOperation(value = "Busca un cliente por id")
	public ResponseEntity<Cliente> clientePorId(@PathVariable Integer id) {

		// Optional<Cliente> c = listaClientesAntigua.stream().filter(unCli ->
		// unCli.getId().equals(id)).findFirst();

		return ResponseEntity.of(clientService.buscarPorId(id));
	}

	@GetMapping(path = "/obtenerCliente/{cuit}")
	@ApiOperation(value = "Busca un cliente por el cuit")
	public ResponseEntity<Cliente> clientePorCuit(@PathVariable String cuit) {
		Optional<Cliente> c = listaClientesAntigua.stream().filter(unCli -> unCli.getCuit().equals(cuit)).findFirst();
		return ResponseEntity.of(c);

	}

	@GetMapping(path = "/obtenerCliente")
	@ApiOperation(value = "Busca un cliente por la razon social")
	public ResponseEntity<Cliente> clientePorRazonSocial(@RequestParam(required = false) String razonSocial) {
		Optional<Cliente> c = listaClientesAntigua.stream().filter(unCli -> unCli.getRazonSocial().equals(razonSocial))
				.findFirst();
		return ResponseEntity.of(c);
	}

	@GetMapping
	public ResponseEntity<List<Cliente>> todos() {
		return ResponseEntity.ok(listaClientesAntigua);
	}

	@PostMapping
	public ResponseEntity<Cliente> crear(@RequestBody Cliente nuevo) {
		// • Cuando se da de alta un Cliente, se debe indicar al menos una Obra con su
		// Tipo de Obra
		// y la información de usuario y clave para crear el usuario.
		List<Obra> obras = nuevo.getObras();
		if (obras != null) {
			Boolean ObrasConSutipo = obras.stream().allMatch(unaObra -> (unaObra.getTipo()!=null));
			Usuario user = nuevo.getUser();
			Boolean existeUser = !user.getUser().equals(null);
			Boolean existePassword = !user.equals(null);
			Boolean existeTipoUser = !user.getTipoUsuario().equals(null);

			if (ObrasConSutipo && existeUser && existePassword && existeTipoUser) {
				// El cliente pose una o mas obras con su tipo (obligatorio)
				// El cliente posee un usuario con toda su informacion (user,password,tipo)
				nuevo.setId(ID_GEN++);

				if (clientService.guardarCliente(nuevo) != null) {
					return ResponseEntity.ok(nuevo);
				}

			}
		}

		return ResponseEntity.badRequest().build();

	}

	@PutMapping(path = "/{id}")
	@ApiOperation(value = "Actualiza un cliente")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Actualizado correctamente"),
			@ApiResponse(code = 401, message = "No autorizado"), @ApiResponse(code = 403, message = "Prohibido"),
			@ApiResponse(code = 404, message = "El ID no existe") })
	public ResponseEntity<Cliente> actualizar(@RequestBody Cliente nuevo, @PathVariable Integer id) {
		OptionalInt indexOpt = IntStream.range(0, listaClientesAntigua.size())
				.filter(i -> listaClientesAntigua.get(i).getId().equals(id)).findFirst();

		if (indexOpt.isPresent()) {
			listaClientesAntigua.set(indexOpt.getAsInt(), nuevo);
			return ResponseEntity.ok(nuevo);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	
	// Para dar de baja un cliente, no se puede eliminar si ya ha realizado algun pedido, por lo 
	// que en ese caso, debe agregar un atributo, "fechaBaja" y asignarle una fecha. Todos los 
	// clientes activos son aquellos que no tienen fechaBaja (o es null)
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Cliente> borrar(@PathVariable Integer id) {
		
		
		this.clientService.borrarCliente(this.clientService.buscarPorId(id).get());
		return ResponseEntity.ok().build();
		/*OptionalInt indexOpt = IntStream.range(0, listaClientesAntigua.size())
				.filter(i -> listaClientesAntigua.get(i).getId().equals(id)).findFirst();

		if (indexOpt.isPresent()) {
			listaClientesAntigua.remove(indexOpt.getAsInt());
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}*/
	}

}
