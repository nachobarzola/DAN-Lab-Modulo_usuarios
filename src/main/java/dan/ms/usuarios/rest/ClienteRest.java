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
import dan.ms.usuarios.services.interfaces.ClientService;
import dan.ms.usuarios.services.interfaces.UsuarioService;
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
	
	@Autowired
	UsuarioService usuarioService;

	private static final List<Cliente> listaClientesAntigua = new ArrayList<>();
	private static Integer ID_GEN = 1;

	@GetMapping(path = "/{id}")
	@ApiOperation(value = "Busca un cliente por id")
	public ResponseEntity<Cliente> clientePorId(@PathVariable Integer id) {
		return ResponseEntity.of(clientService.buscarPorId(id));
	}

	@GetMapping(path = "/obtenerCliente/{cuit}")
	@ApiOperation(value = "Busca un cliente por el cuit")
	public ResponseEntity<Cliente> clientePorCuit(@PathVariable String cuit) {
		return ResponseEntity.of(clientService.buscarPorCuit(cuit));

	}

	@GetMapping(path = "/obtenerCliente")
	@ApiOperation(value = "Busca un cliente por la razon social")
	public ResponseEntity<Cliente> clientePorRazonSocial(@RequestParam(required = false) String razonSocial) {
		return ResponseEntity.of(clientService.buscarPorRazonSocial(razonSocial));

	}
	@GetMapping(path = "/obtenerCliente/obra/{idObra}")
	@ApiOperation(value = "Busca un cliente por el id de una obra")
	public ResponseEntity<Cliente> clientePorIdObra(@PathVariable Integer idObra) {
		return ResponseEntity.of(clientService.buscarPorIdObra(idObra));

	}
	@GetMapping(path = "/obtenerCliente/obra")
	@ApiOperation(value = "Busca un cliente por el id de una obra")
	public ResponseEntity<Cliente> clientePorObra(@RequestBody Obra obra) {
		return ResponseEntity.of(clientService.buscarPorObra(obra));

	}

	@GetMapping
	@ApiOperation(value = "Se obtienen todos los clientes")
	public ResponseEntity<List<Cliente>> getAllCliente() {
		return ResponseEntity.ok(clientService.getAllCliente());
	}

	@PostMapping
	public ResponseEntity<Cliente> crear(@RequestBody Cliente nuevoC) {
		// • Cuando se da de alta un Cliente, se debe indicar al menos una Obra con su
		// Tipo de Obra
		ResponseEntity<Cliente> respEntBadRequest = ResponseEntity.badRequest().build();
		// Validamos que el cliente exista
		if (nuevoC == null) {
			return respEntBadRequest;
		}
		List<Obra> obras = nuevoC.getObras();
		// Validamos que tenga obras
		if (obras == null || obras.size() == 0) {
			return respEntBadRequest;
		}
		Boolean ObrasConSutipo = obras.stream().allMatch(unaObra -> (unaObra.getTipo() != null));
		
		if (ObrasConSutipo) {
			// El cliente pose una o mas obras con su tipo (obligatorio)

			Optional<Cliente> optClienteReturn = clientService.guardarCliente(nuevoC);
	
			if (optClienteReturn.isPresent()) {
				return ResponseEntity.ok(optClienteReturn.get());
			}

		}
		return respEntBadRequest;

	}

	@PutMapping(path="/{cuit}")
	@ApiOperation(value = "Actualiza el cliente. El cliente no debe estar dado de baja.Para actualizar usa el cuit")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Actualizado correctamente"),
			@ApiResponse(code = 401, message = "No autorizado"), 
			@ApiResponse(code = 403, message = "Prohibido"),
			@ApiResponse(code = 404, message = "El cliente no existe") })
	public ResponseEntity<Cliente> actualizar(@RequestBody Cliente nuevo,@PathVariable String cuit) {
		Optional<Cliente> optClienteBuscado = clientService.buscarPorCuit(cuit);
		if (optClienteBuscado.isPresent()) {
			nuevo.setId(optClienteBuscado.get().getId());
			return ResponseEntity.of(clientService.actualizarCliente(nuevo));
		} 
		else {
			//El cliente no existe
			return ResponseEntity.notFound().build();
		}
	}
	@PutMapping(path="/{cuit}/usuario")
	@ApiOperation(value = "Actualiza el usuario de un cliente. Para actualizar usa el cuit del cliente")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Actualizado correctamente"),
			@ApiResponse(code = 401, message = "No autorizado"), 
			@ApiResponse(code = 403, message = "Prohibido"),
			@ApiResponse(code = 404, message = "El cliente no existe") })
	public ResponseEntity<Usuario> actualizarUsuario(@RequestBody Usuario nuevoUsuario,@PathVariable String cuit) {
		Optional<Cliente> optClienteBuscado = clientService.buscarPorCuit(cuit);
		if (optClienteBuscado.isPresent()) {
			System.out.println("El usuario del cliente es: "+optClienteBuscado.get().getUser());
			nuevoUsuario.setId(optClienteBuscado.get().getUser().getId());
			return ResponseEntity.of(usuarioService.actualizarUsuario(nuevoUsuario));
		} 
		else {
			//El cliente no existe
			return ResponseEntity.notFound().build();
		}
	}

	// Para dar de baja un cliente, no se puede eliminar si ya ha realizado algun
	// pedido, por lo
	// que en ese caso, debe agregar un atributo, "fechaBaja" y asignarle una fecha.
	// Todos los
	// clientes activos son aquellos que no tienen fechaBaja (o es null)
	@DeleteMapping(path = "/{id}")
	@ApiOperation(value = "Borrar un cliente")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Borrado correctamente"),
			@ApiResponse(code = 400, message = "El cliente no existe, ingrese id valida"),
			@ApiResponse(code = 404, message = "Hubo un error al borrar") })
	public ResponseEntity<Cliente> borrar(@PathVariable Integer id) {
		Optional<Cliente> optClienteABorrar = this.clientService.buscarPorId(id);
		if (optClienteABorrar.isPresent()) {
			Boolean borrado = this.clientService.borrarCliente(optClienteABorrar.get());
			if (borrado) {
				//Se borro correctamente
				return ResponseEntity.of(optClienteABorrar);
			} 
			else {
				//Error al intentar borrar
				return ResponseEntity.notFound().build();
			}
		} 
		else {
			//El cliente no existe
			return ResponseEntity.badRequest().build();
		}
	}

}
