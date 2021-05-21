package dan.ms.usuarios;

import static org.junit.jupiter.api.Assertions.assertNull;
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
import org.springframework.context.annotation.Profile;
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
import dan.ms.usuarios.services.dao.ClienteRepository;
import dan.ms.usuarios.services.dao.ObraRepository;
import dan.ms.usuarios.services.dao.UsuarioRepository;
import dan.ms.usuarios.services.interfaces.ClientService;
import springfox.documentation.spring.web.json.Json;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Profile("testing")
class ClienteRestTest {

	private String ENDPOINT_CLIENTE = "/api/cliente";
	private RestTemplate restTemplate = new RestTemplate();

	@Autowired
	TestRestTemplate testRestTemplate;

	@Autowired
	ClientService clienteService;

	@LocalServerPort
	String puerto;

	@Autowired
	ObraRepository obraRepo;

	@Autowired
	UsuarioRepository usuarioRepo;

	@Autowired
	ClienteRepository clienteRepo;

	@BeforeEach
	public void limpiarRepositorios() {
		obraRepo.deleteAll();
		clienteRepo.deleteAll();
		usuarioRepo.deleteAll();
	}

	// Test de integracion
	@Test
	void crear_clienteCompleto() {
		String server = "http://localhost:" + puerto + ENDPOINT_CLIENTE;
		// • Cuando se da de alta un Cliente, se debe indicar al menos una Obra con su
		// Tipo de Obra
		// y la información de usuario y clave para crear el usuario.

		// Tipos necesarios
		TipoUsuario tipoUsr = new TipoUsuario(1, "Cliente");
		TipoObra tipoObra1 = new TipoObra(1, "REFORMA");
		TipoObra tipoObra2 = new TipoObra(2, "CASA");
		// -----Obra1
		Obra o1 = new Obra();
		o1.setDescripcion("Una obra chiquita");
		o1.setDireccion("Bv Galvez");
		o1.setLatitud((float) 42252);
		o1.setLongitud((float) 1225);
		o1.setSuperficie(100);
		o1.setTipo(tipoObra1);
		// --------------
		// -----Obra2
		Obra o2 = new Obra();
		o2.setDescripcion("Una obra muy grande");
		o2.setDireccion("Av San Juan");
		o2.setLatitud((float) 42252);
		o2.setLongitud((float) 1225);
		o2.setSuperficie(9999999);
		o2.setTipo(tipoObra2);

		// ---------------------
		List<Obra> obras = new ArrayList<>();
		obras.add(o1);
		obras.add(o2);
		// -----------------------------
		Usuario usr = new Usuario("HomeroJ", "siempreViva", tipoUsr);
		Cliente cNuevo = new Cliente("Cliente01", "20395783698", "Homero@gmail.com", 50000, true, obras, usr, null);
		// Setenmos la obra a su cliente
		obras.get(0).setCliente(cNuevo);
		obras.get(1).setCliente(cNuevo);

		HttpEntity<Cliente> requestCliente = new HttpEntity<>(cNuevo);
		ResponseEntity<Cliente> respuesta = restTemplate.exchange(server, HttpMethod.POST, requestCliente,
				Cliente.class);
		// Debe responder OK (cod 200)
		assertTrue(respuesta.getStatusCode().equals(HttpStatus.OK));
		Integer idCliente = respuesta.getBody().getId();
		// Chequeo que este persistido
		Optional<Cliente> cli = clienteService.buscarPorId(idCliente);
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
		Usuario usr = new Usuario("LisaJ", "siempreViva123", tipoUsr);
		Cliente cNuevo = new Cliente("Cliente02", "20874596216", "Lisa@gmail.com", 21, true, obras, usr, null);
		obras.get(0).setCliente(cNuevo);

		HttpEntity<Cliente> requestCliente = new HttpEntity<>(cNuevo);
		ResponseEntity<Cliente> respuesta = testRestTemplate.exchange(server, HttpMethod.POST, requestCliente,
				Cliente.class);

		assertTrue(respuesta.getStatusCode().equals(HttpStatus.BAD_REQUEST));

		// Chequeo que no este persistido
		Cliente cli = clienteRepo.findByCuit(cNuevo.getCuit());
		assertNull(cli);

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
		Usuario usr = new Usuario("BarS", "siempreVivaPat", null);
		Cliente cNuevo = new Cliente("Cliente03", "20214862465", "Bar@live.com.ar", 666, true, obras, usr, null);

		HttpEntity<Cliente> requestCliente = new HttpEntity<>(cNuevo);
		ResponseEntity<Cliente> respuesta = testRestTemplate.exchange(server, HttpMethod.POST, requestCliente,
				Cliente.class);

		assertTrue(respuesta.getStatusCode().equals(HttpStatus.BAD_REQUEST));

		// Chequeo que no este persistido
		Cliente cli = clienteRepo.findByCuit(cNuevo.getCuit());
		assertNull(cli);

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
		Cliente cNuevo = new Cliente("Cliente04", "20415789639", "Patricio@gmail.com", 111, true, obras, null, null);

		HttpEntity<Cliente> requestCliente = new HttpEntity<>(cNuevo);
		ResponseEntity<Cliente> respuesta = testRestTemplate.exchange(server, HttpMethod.POST, requestCliente,
				Cliente.class);

		assertTrue(respuesta.getStatusCode().equals(HttpStatus.BAD_REQUEST));

		// Chequeo que no este persistido
		Cliente cli = clienteRepo.findByCuit(cNuevo.getCuit());
		assertNull(cli);

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
		Usuario usr = new Usuario("ayudanteDeSanta", password, tipoUsr);
		Cliente cNuevo = new Cliente("Cliente05", "20395783698", "ayudSanta@gmail.com", 123, true, obras, usr, null);

		HttpEntity<Cliente> requestCliente = new HttpEntity<>(cNuevo);
		ResponseEntity<Cliente> respuesta = testRestTemplate.exchange(server, HttpMethod.POST, requestCliente,
				Cliente.class);

		assertTrue(respuesta.getStatusCode().equals(HttpStatus.BAD_REQUEST));

		// Chequeo que no este persistido
		Cliente cli = clienteRepo.findByCuit(cNuevo.getCuit());
		assertNull(cli);

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

		List<Obra> obras = null; // FALTA ESTO!!
		TipoUsuario tipoUsr = new TipoUsuario(1, "Cliente");
		String password = "12345";
		Usuario usr = new Usuario("ayudanteDeSanta", password, tipoUsr);
		Cliente cNuevo = new Cliente("Cliente05", "20395783698", "ayudSanta@gmail.com", 123, true, obras, usr, null);

		HttpEntity<Cliente> requestCliente = new HttpEntity<>(cNuevo);
		ResponseEntity<Cliente> respuesta = testRestTemplate.exchange(server, HttpMethod.POST, requestCliente,
				Cliente.class);

		assertTrue(respuesta.getStatusCode().equals(HttpStatus.BAD_REQUEST));

	}

	// Test unitario
	@Test
	void crear_clienteIncompleto_faltanNombreUsuario() {
		String server = "http://localhost:" + puerto + ENDPOINT_CLIENTE;

		// Creo un cliente
		Obra o1 = new Obra();
		o1.setTipo(new TipoObra(1, "REFORMA"));
		List<Obra> obras = new ArrayList<>();
		obras.add(o1);
		Usuario usr = null;
		Cliente cNuevo = new Cliente("Cliente05", "20395783698", "ayudSanta@gmail.com", 123, true, obras, usr, null);

		HttpEntity<Cliente> requestCliente = new HttpEntity<>(cNuevo);
		ResponseEntity<Cliente> respuesta = testRestTemplate.exchange(server, HttpMethod.POST, requestCliente,
				Cliente.class);

		assertTrue(respuesta.getStatusCode().equals(HttpStatus.BAD_REQUEST));

	}

	@Test
	void crear_clienteIncompleto_faltaPassword() {
		String server = "http://localhost:" + puerto + ENDPOINT_CLIENTE;

		// Creo un cliente

		Obra o1 = new Obra();
		o1.setTipo(new TipoObra(1, "REFORMA"));
		List<Obra> obras = new ArrayList<>();
		obras.add(o1);
		TipoUsuario tipoUsr = new TipoUsuario(1, "Cliente");
		String password = null; // Falta esto
		Usuario usr = new Usuario("ayudanteDeSanta", password, tipoUsr);
		Cliente cNuevo = new Cliente("Cliente05", "20395783698", "ayudSanta@gmail.com", 123, true, obras, usr, null);

		HttpEntity<Cliente> requestCliente = new HttpEntity<>(cNuevo);
		ResponseEntity<Cliente> respuesta = testRestTemplate.exchange(server, HttpMethod.POST, requestCliente,
				Cliente.class);

		assertTrue(respuesta.getStatusCode().equals(HttpStatus.BAD_REQUEST));

	}

	@Test
	void buscar_cliente_poIdRazonSocialCuit() {
		// -------------------Creamos el cliente
		Obra o1 = new Obra();
		o1.setTipo(new TipoObra(1, "REFORMA"));
		List<Obra> obras = new ArrayList<>();
		obras.add(o1);
		TipoUsuario tipoUsr = new TipoUsuario(1, "Cliente");
		Usuario usr = new Usuario("Franco", "Suipacha", tipoUsr);
		Cliente cNuevo = new Cliente("Cliente66", "20395783698", "franco@gmail.com", 222, true, obras, usr, null);
		obras.get(0).setCliente(cNuevo);
		// -----------------------------------------------------
		// Persisto el cliente
		Optional<Cliente> optCNuevo = clienteService.guardarCliente(cNuevo);
		cNuevo = optCNuevo.get();

		// Busco el cliente por id---------------------------
		String serverConId = "http://localhost:" + puerto + ENDPOINT_CLIENTE + "/" + cNuevo.getId();
		ResponseEntity<Cliente> respuesta = testRestTemplate.exchange(serverConId, HttpMethod.GET, null, Cliente.class);
		assertTrue(respuesta.getStatusCode().equals(HttpStatus.OK));

		// Busco el cliente por cuit---------------------------
		String serverConCuit = "http://localhost:" + puerto + ENDPOINT_CLIENTE + "/obtenerCliente/" + cNuevo.getCuit();
		ResponseEntity<Cliente> respuesta3 = testRestTemplate.exchange(serverConCuit, HttpMethod.GET, null,
				Cliente.class);
		assertTrue(respuesta3.getStatusCode().equals(HttpStatus.OK));

		// Busco el cliente por razonSocial---------------------------
		String serverConRazonSocial = "http://localhost:" + puerto + ENDPOINT_CLIENTE + "/obtenerCliente?razonSocial="
				+ cNuevo.getRazonSocial();
		
		ResponseEntity<Cliente> respuesta2 = testRestTemplate.exchange(serverConRazonSocial, HttpMethod.GET, null,
				Cliente.class);
		assertTrue(respuesta2.getStatusCode().equals(HttpStatus.OK));

	}

	// Todo: Me pasa lo mismo que clienteServiceImpTest no se como intergrar
	// microservicios
	@Disabled
	void borrar_cliente() {
		// -------------------Creamos el cliente
		Obra o1 = new Obra();
		o1.setTipo(new TipoObra(1, "REFORMA"));
		List<Obra> obras = new ArrayList<>();
		obras.add(o1);
		TipoUsuario tipoUsr = new TipoUsuario(1, "Cliente");
		Usuario usr = new Usuario("Gato", "Bv Galves", tipoUsr);
		Cliente cNuevo = new Cliente("Cliente7", "2078965236696", "gato@gmail.com", 222, true, obras, usr, null);
		// -----------------------------------------------------
		// Persisto el cliente
		clienteService.guardarCliente(cNuevo);
		// String serverConId = "http://localhost:" + puerto + ENDPOINT_CLIENTE + "/" +
		// 7;
		// ResponseEntity<Cliente> respuesta = testRestTemplate.exchange(serverConId,
		// HttpMethod.DELETE, null, Cliente.class);
		// assertTrue(respuesta.getStatusCode().equals(HttpStatus.OK));

	}

}
