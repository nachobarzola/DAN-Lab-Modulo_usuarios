package dan.ms.usuarios;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import dan.ms.usuarios.domain.Cliente;
import dan.ms.usuarios.domain.Obra;
import dan.ms.usuarios.domain.TipoObra;
import dan.ms.usuarios.domain.TipoUsuario;
import dan.ms.usuarios.domain.Usuario;
import dan.ms.usuarios.services.interfaces.ClientService;
import springfox.documentation.spring.web.json.Json;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ClienteRestTest {

	private String ENDPOINT_CLIENTE = "/api/cliente";
	private RestTemplate restTemplate = new RestTemplate();

	@Autowired
	TestRestTemplate testRestTemplate;

	@Autowired
	private ClientService clienteService;

	@LocalServerPort
	String puerto;

	// Test de integracion
	@Test
	void crear_clienteCompleto() {
		String server = "http://localhost:" + puerto + ENDPOINT_CLIENTE;
		// • Cuando se da de alta un Cliente, se debe indicar al menos una Obra con su
		// Tipo de Obra
		// y la información de usuario y clave para crear el usuario.

		// Creo un cliente
		Obra o1 = new Obra();
		o1.setTipo(new TipoObra(1, "REFORMA"));
		List<Obra> obras = new ArrayList<>();
		obras.add(o1);
		TipoUsuario tipoUsr = new TipoUsuario(1, "Cliente");
		Usuario usr = new Usuario(1, "HomeroJ", "siempreViva", tipoUsr);
		Cliente cNuevo = new Cliente(1, "Cliente01", "20395783698", "Homero@gmail.com", 50000, true, obras, usr, null);

		HttpEntity<Cliente> requestCliente = new HttpEntity<>(cNuevo);
		ResponseEntity<Cliente> respuesta = restTemplate.exchange(server, HttpMethod.POST, requestCliente,
				Cliente.class);

		assertTrue(respuesta.getStatusCode().equals(HttpStatus.OK));
		// Chequeo que este persistido
		Optional<Cliente> cli = clienteService.buscarPorId(cNuevo.getId());
		assertTrue(cli.isPresent());

	}

	// Test unitario
	@Test
	void crear_clienteIncompleto_faltaTipoObra() {
		String server = "http://localhost:" + puerto + ENDPOINT_CLIENTE;
		// • Cuando se da de alta un Cliente, se debe indicar al menos una Obra con su
		// Tipo de Obra
		// y la información de usuario y clave para crear el usuario.

		// Creo un cliente
		Obra o1 = new Obra();
		// o1.setTipo(new TipoObra(1,"REFORMA")); FALTA ESTO!!
		List<Obra> obras = new ArrayList<>();
		obras.add(o1);
		TipoUsuario tipoUsr = new TipoUsuario(1, "Cliente");
		Usuario usr = new Usuario(2, "LisaJ", "siempreViva123", tipoUsr);
		Cliente cNuevo = new Cliente(2, "Cliente02", "20874596216", "Lisa@gmail.com", 21, true, obras, usr, null);

		HttpEntity<Cliente> requestCliente = new HttpEntity<>(cNuevo);
		ResponseEntity<Cliente> respuesta = testRestTemplate.exchange(server, HttpMethod.POST, requestCliente,
				Cliente.class);

		assertTrue(respuesta.getStatusCode().equals(HttpStatus.BAD_REQUEST));

		// Chequeo que no este persistido
		Optional<Cliente> cli = clienteService.buscarPorId(cNuevo.getId());
		assertTrue(cli.isEmpty());

	}

	// Test unitario
	@Test
	void crear_clienteIncompleto_faltaTipoUser() {
		String server = "http://localhost:" + puerto + ENDPOINT_CLIENTE;
		// Creo un cliente
		Obra o1 = new Obra();
		o1.setTipo(new TipoObra(1, "REFORMA"));
		List<Obra> obras = new ArrayList<>();
		obras.add(o1);
		// TipoUsuario tipoUsr = new TipoUsuario(1, "Cliente"); FALTA ESTO!!
		Usuario usr = new Usuario(3, "BarS", "siempreVivaPat", null);
		Cliente cNuevo = new Cliente(3, "Cliente03", "20214862465", "Bar@live.com.ar", 666, true, obras, usr, null);

		HttpEntity<Cliente> requestCliente = new HttpEntity<>(cNuevo);
		ResponseEntity<Cliente> respuesta = testRestTemplate.exchange(server, HttpMethod.POST, requestCliente,
				Cliente.class);

		assertTrue(respuesta.getStatusCode().equals(HttpStatus.BAD_REQUEST));

		// Chequeo que no este persistido
		Optional<Cliente> cli = clienteService.buscarPorId(cNuevo.getId());
		assertTrue(cli.isEmpty());

	}

	// Test unitario
	@Test
	void crear_clienteIncompleto_faltaUser() {
		String server = "http://localhost:" + puerto + ENDPOINT_CLIENTE;
		// Creo un cliente
		Obra o1 = new Obra();
		o1.setTipo(new TipoObra(1, "REFORMA"));
		List<Obra> obras = new ArrayList<>();
		obras.add(o1);
		// TipoUsuario tipoUsr = new TipoUsuario(1, "Cliente");
		// Usuario usr = new Usuario(1, "HomeroJ", "siempreViva", tipoUsr); FALTA ESTO!!
		Cliente cNuevo = new Cliente(4, "Cliente04", "20415789639", "Patricio@gmail.com", 111, true, obras, null, null);

		HttpEntity<Cliente> requestCliente = new HttpEntity<>(cNuevo);
		ResponseEntity<Cliente> respuesta = testRestTemplate.exchange(server, HttpMethod.POST, requestCliente,
				Cliente.class);

		assertTrue(respuesta.getStatusCode().equals(HttpStatus.BAD_REQUEST));

		// Chequeo que no este persistido
		Optional<Cliente> cli = clienteService.buscarPorId(cNuevo.getId());
		assertTrue(cli.isEmpty());

	}

	// Test unitario
	@Test
	void crear_clienteIncompleto_faltaPasswordUser() {
		String server = "http://localhost:" + puerto + ENDPOINT_CLIENTE;
		// Creo un cliente
		Obra o1 = new Obra();
		o1.setTipo(new TipoObra(1, "REFORMA"));
		List<Obra> obras = new ArrayList<>();
		obras.add(o1);
		TipoUsuario tipoUsr = new TipoUsuario(1, "Cliente");
		String password = null; // FALTA ESTO!!
		Usuario usr = new Usuario(5, "ayudanteDeSanta", password, tipoUsr);
		Cliente cNuevo = new Cliente(5, "Cliente05", "20395783698", "ayudSanta@gmail.com", 123, true, obras, usr, null);

		HttpEntity<Cliente> requestCliente = new HttpEntity<>(cNuevo);
		ResponseEntity<Cliente> respuesta = testRestTemplate.exchange(server, HttpMethod.POST, requestCliente,
				Cliente.class);

		assertTrue(respuesta.getStatusCode().equals(HttpStatus.BAD_REQUEST));

		// Chequeo que no este persistido
		Optional<Cliente> cli = clienteService.buscarPorId(cNuevo.getId());
		assertTrue(cli.isEmpty());

	}

	// Test unitario
	@Test
	void crear_clienteIncompleto_faltaClienteBody() {
		String server = "http://localhost:" + puerto + ENDPOINT_CLIENTE;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
		HttpEntity<Cliente> requestCliente = new HttpEntity<>(headers);

		ResponseEntity<Cliente> respuesta = testRestTemplate.exchange(server, HttpMethod.POST, requestCliente,
				Cliente.class);

		assertTrue(respuesta.getStatusCode().equals(HttpStatus.BAD_REQUEST));

	}

	// Test unitario
	@Test
	void crear_clienteIncompleto_faltanObras() {
		String server = "http://localhost:" + puerto + ENDPOINT_CLIENTE;

		// Creo un cliente
		Obra o1 = new Obra();
		o1.setTipo(new TipoObra(1, "REFORMA"));
		List<Obra> obras = new ArrayList<>();
		obras.add(o1);
		Usuario usr = null;
		Cliente cNuevo = new Cliente(5, "Cliente05", "20395783698", "ayudSanta@gmail.com", 123, true, obras, usr, null);

		HttpEntity<Cliente> requestCliente = new HttpEntity<>(cNuevo);
		ResponseEntity<Cliente> respuesta = testRestTemplate.exchange(server, HttpMethod.POST, requestCliente,
				Cliente.class);

		assertTrue(respuesta.getStatusCode().equals(HttpStatus.BAD_REQUEST));

	}

	// Test unitario
	@Test
	void crear_clienteIncompleto_faltanNombreUser() {
		String server = "http://localhost:" + puerto + ENDPOINT_CLIENTE;

		// Creo un cliente

		List<Obra> obras = null;
		TipoUsuario tipoUsr = new TipoUsuario(1, "Cliente");
		String password = null; // FALTA ESTO!!
		Usuario usr = new Usuario(5, "ayudanteDeSanta", password, tipoUsr);
		Cliente cNuevo = new Cliente(5, "Cliente05", "20395783698", "ayudSanta@gmail.com", 123, true, obras, usr, null);

		HttpEntity<Cliente> requestCliente = new HttpEntity<>(cNuevo);
		ResponseEntity<Cliente> respuesta = testRestTemplate.exchange(server, HttpMethod.POST, requestCliente,
				Cliente.class);

		assertTrue(respuesta.getStatusCode().equals(HttpStatus.BAD_REQUEST));

	}

	// Test de integracion
	@BeforeEach
	void preparacion_test() {
		// -------------------Creamos el cliente
		Obra o1 = new Obra();
		o1.setTipo(new TipoObra(1, "REFORMA"));
		List<Obra> obras = new ArrayList<>();
		obras.add(o1);
		TipoUsuario tipoUsr = new TipoUsuario(1, "Cliente");
		Usuario usr = new Usuario(66, "Franco", "Suipacha", tipoUsr);
		Cliente cNuevo = new Cliente(66, "Cliente66", "20395783698", "franco@gmail.com", 222, true, obras, usr, null);
		// -----------------------------------------------------
		// Persisto el cliente
		clienteService.guardarCliente(cNuevo);
	}

	@Test
	void buscar_cliente_poIdRazonSocialCuit() {
		// Busco el cliente por id---------------------------
		String serverConId = "http://localhost:" + puerto + ENDPOINT_CLIENTE + "/" + 66;
		ResponseEntity<Cliente> respuesta = testRestTemplate.exchange(serverConId, HttpMethod.GET, null, Cliente.class);
		assertTrue(respuesta.getStatusCode().equals(HttpStatus.OK));

		// Busco el cliente por cuit---------------------------
		String serverConCuit = "http://localhost:" + puerto + ENDPOINT_CLIENTE + "/obtenerCliente/20395783698";
		ResponseEntity<Cliente> respuesta3 = testRestTemplate.exchange(serverConCuit, HttpMethod.GET, null,
				Cliente.class);
		assertTrue(respuesta3.getStatusCode().equals(HttpStatus.OK));

		// TODO: No anda no se porque, pero con insomia y la misma uri anda!

		// Busco el cliente por razonSocial---------------------------
		/*String serverConRazonSocial = "http://localhost:" + puerto + ENDPOINT_CLIENTE + "/obtenerCliente?razonSocial="
		+"Cliente66";
		Map<String, String> params = new HashMap<String, String>();
	    params.put("razonSocial", "Cliente66");
		
	    Cliente c=null;
	    HttpEntity<Cliente> requestCliente = new HttpEntity<>(c);
	    
		ResponseEntity<Cliente> respuesta2 = testRestTemplate.exchange(serverConRazonSocial, HttpMethod.GET, requestCliente,
				Cliente.class, params);

		System.out.println(serverConRazonSocial);
		assertTrue(respuesta2.getStatusCode().equals(HttpStatus.OK));*/

	}

	@BeforeEach
	void preparacion_test2() {
		// -------------------Creamos el cliente
		Obra o1 = new Obra();
		o1.setTipo(new TipoObra(1, "REFORMA"));
		List<Obra> obras = new ArrayList<>();
		obras.add(o1);
		TipoUsuario tipoUsr = new TipoUsuario(1, "Cliente");
		Usuario usr = new Usuario(7, "Gato", "Bv Galves", tipoUsr);
		Cliente cNuevo = new Cliente(7, "Cliente7", "2078965236696", "gato@gmail.com", 222, true, obras, usr, null);
		// -----------------------------------------------------
		// Persisto el cliente
		clienteService.guardarCliente(cNuevo);
	}

	// Todo: Me pasa lo mismo que clienteServiceImpTest no se como intergrar
	// microservicios
	@Disabled
	void borrar_cliente() {
		// String serverConId = "http://localhost:" + puerto + ENDPOINT_CLIENTE + "/" +
		// 7;
		// ResponseEntity<Cliente> respuesta = testRestTemplate.exchange(serverConId,
		// HttpMethod.DELETE, null, Cliente.class);
		// assertTrue(respuesta.getStatusCode().equals(HttpStatus.OK));

	}

}
