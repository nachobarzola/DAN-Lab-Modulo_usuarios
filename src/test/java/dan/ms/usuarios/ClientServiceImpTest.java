package dan.ms.usuarios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import dan.ms.usuarios.domain.Cliente;
import dan.ms.usuarios.domain.Obra;
import dan.ms.usuarios.domain.TipoObra;
import dan.ms.usuarios.domain.TipoUsuario;
import dan.ms.usuarios.domain.Usuario;
import dan.ms.usuarios.services.dao.ClienteRepository;
import dan.ms.usuarios.services.dao.ObraRepository;
import dan.ms.usuarios.services.dao.UsuarioRepository;
import dan.ms.usuarios.services.interfaces.ClientService;

@SpringBootTest
@Profile("testing")
public class ClientServiceImpTest {
	private String API_REST_PEDIDO = "http://localhost:8080/api/pedido";

	@Autowired
	private ClientService clientService;

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

	// Test de integracion con DB/repositorio
	@Test
	public void guardarCliente() {
		// Tipos necesarios
		TipoUsuario tipoUsr = new TipoUsuario(1, "Cliente");
		TipoObra tipoObra1 = new TipoObra(1, "REFORMA");
		TipoObra tipoObra2 = new TipoObra(2, "CASA");
		// -----Obra1
		Obra o1 = new Obra();

		o1.setDescripcion("Una obra chiquita");
		o1.setDireccion("Bv Galvez");
		o1.setLatitud(Float.valueOf(1225));
		o1.setLongitud(Float.valueOf(1225));
		o1.setSuperficie(100);
		o1.setTipo(tipoObra1);
		// --------------
		// -----Obra2
		Obra o2 = new Obra();
		o2.setDescripcion("Una obra muy grande");
		o2.setDireccion("Av San Juan");
		o2.setLatitud(Float.valueOf(1225));
		o2.setLongitud(Float.valueOf(1225));
		o2.setSuperficie(9999999);
		o2.setTipo(tipoObra2);

		// ---------------------
		List<Obra> obras = new ArrayList<>();
		obras.add(o1);
		obras.add(o2);
		// -----------------------------
		Usuario usr = new Usuario("HomeroJ", "siempreViva", tipoUsr);
		Cliente c1 = new Cliente("Cliente01", "20395783698", "Homero@gmail.com", 50000, true, obras, usr, null);
		// Setenmos la obra a su cliente
		o1.setCliente(c1);
		o2.setCliente(c1);

		// Persisto el cliente
		Optional<Cliente> cReturn = clientService.guardarCliente(c1);
		assertTrue(cReturn.isPresent());
	}

	// Test de intergracion con DB/repositorio
	@Test
	public void buscarCliente() {
		// Tipos necesarios
		TipoUsuario tipoUsr1 = new TipoUsuario(1, "CLIENTE");
		TipoUsuario tipoUsr2 = new TipoUsuario(2, "VENDEDOR");
		TipoObra tipoObra1 = new TipoObra(1, "REFORMA");
		TipoObra tipoObra2 = new TipoObra(2, "CASA");
		// -----Obra1
		Obra o1 = new Obra();
		o1.setDescripcion("Una obra chiquita");
		o1.setDireccion("Bv Galvez");
		o1.setLatitud(Float.valueOf(1225));
		o1.setLongitud(Float.valueOf(1225));
		o1.setSuperficie(100);
		o1.setTipo(tipoObra1);
		// --------------
		// -----Obra2
		Obra o2 = new Obra();
		o2.setDescripcion("Una obra muy grande");
		o2.setDireccion("Av San Juan");
		o2.setLatitud(Float.valueOf(1225));
		o2.setLongitud(Float.valueOf(1225));
		o2.setSuperficie(9999999);
		o2.setTipo(tipoObra2);
		// -----Obra3
		Obra o3 = new Obra();
		o3.setDescripcion("Una obra muy grande");
		o3.setDireccion("Av San Juan");
		o3.setLatitud(Float.valueOf(1225));
		o3.setLongitud(Float.valueOf(1225));
		o3.setSuperficie(9999999);
		o3.setTipo(tipoObra2);
		// ---------------------
		List<Obra> obras = new ArrayList<>();
		obras.add(o1);
		obras.add(o2);
		obras.add(o3);
		// Voy a usar esas obras para los tres clientes

		// Cliente 1
		Usuario usr = new Usuario("HomeroJ", "siempreViva", tipoUsr1);
		Cliente c1 = new Cliente("Cliente01", "20395783698", "homero@gmail.com", 50000, true, null, usr, null);
		for (Obra unaObra : obras) {
			unaObra.setCliente(c1);
		}
		c1.setObras(obras);
		// Persisto al cliente 1
		Optional<Cliente> optC1 = clientService.guardarCliente(c1);
		c1 = optC1.get();
		// Cliente 2
		Usuario usr2 = new Usuario("Bar", "siempreViva123", tipoUsr1);
		Cliente c2 = new Cliente("Cliente02", "20752489659", "bar@gmail.com", 50000, true, null, usr2, null);
		for (Obra unaObra : obras) {
			unaObra.setCliente(c2);
		}
		c2.setObras(obras);
		// Persisto al cliente 2
		Optional<Cliente> optC2 = clientService.guardarCliente(c2);
		c2 = optC2.get();
		// Cliente 3
		Usuario usr3 = new Usuario("Lisa", "siempreViva1234", tipoUsr2);
		Cliente c3 = new Cliente("Cliente03", "20741857962", "lisa@gmail.com", 50000, true, null, usr3, null);
		for (Obra unaObra : obras) {
			unaObra.setCliente(c3);
		}
		c3.setObras(obras);
		// Persisto al cliente 3
		Optional<Cliente> optC3 = clientService.guardarCliente(c3);
		c3 = optC3.get();
		// Lo busco a donde se persistio
		Optional<Cliente> optCReturn1 = clientService.buscarPorId(c1.getId());
		assertTrue(optCReturn1.isPresent());
		Optional<Cliente> optCReturn2 = clientService.buscarPorRazonSocial(c2.getRazonSocial());
		assertTrue(optCReturn2.isPresent());
		Optional<Cliente> optCReturn3 = clientService.buscarPorCuit(c3.getCuit());
		assertTrue(optCReturn3.isPresent());

	}

	// Test de intergracion con DB/repositorio
	@Test
	public void buscarCliente_DadoDeBaja() {
		//Tipos requridos
		TipoObra tipoObra1 = new TipoObra(1, "REFORMA");
		TipoObra tipoObra2 = new TipoObra(2, "CASA");
		// -----Obra1
		Obra o1 = new Obra();
		o1.setDescripcion("Una obra chiquita");
		o1.setDireccion("Bv Galvez");
		o1.setLatitud(Float.valueOf(1225));
		o1.setLongitud(Float.valueOf(1225));
		o1.setSuperficie(100);
		o1.setTipo(tipoObra1);
		// --------------
		// -----Obra2
		Obra o2 = new Obra();
		o2.setDescripcion("Una obra muy grande");
		o2.setDireccion("Av San Juan");
		o2.setLatitud(Float.valueOf(1225));
		o2.setLongitud(Float.valueOf(1225));
		o2.setSuperficie(9999999);
		o2.setTipo(tipoObra2);
		//
		List<Obra> obras = new ArrayList<>();
		obras.add(o1);
		obras.add(o2);
		//
		TipoUsuario tipoUsr = new TipoUsuario(1, "Cliente");
		Usuario usr = new Usuario("HomeroJ", "siempreViva", tipoUsr);
		Date fechaBaja = new Date(System.currentTimeMillis());
		// Cliente dado de baja porque tiene fecha de baja
		Cliente c1 = new Cliente("Cliente01", "20395783698", "Homero@gmail.com", 50000, true, null, usr, fechaBaja);
		obras.get(0).setCliente(c1);
		obras.get(1).setCliente(c1);
		c1.setObras(obras);
		
		// Persisto el cliente
		Optional<Cliente> optC1 = clientService.guardarCliente(c1);
		c1 = optC1.get();
		// ----------------Lo buscamos
		// Busqueda por id
		Optional<Cliente> optC = clientService.buscarPorId(c1.getId());
		assertEquals(Optional.empty(), optC); // No debe encontrarlo porque esta dado de baja
		// Busqueda por cuit
		Optional<Cliente> optC2 = clientService.buscarPorCuit(c1.getCuit());
		assertEquals(Optional.empty(), optC2);// No debe encontrarlo porque esta dado de baja
		// Busqueda por razonSocial
		Optional<Cliente> optC3 = clientService.buscarPorRazonSocial(c1.getRazonSocial());
		assertEquals(Optional.empty(), optC3);// No debe encontrarlo porque esta dado de baja
	}

	// TODO: NO SE COMO IMPLEMENTAR ESTE TEST: deberia de alguna forma mockear el microservicio pedido.
	@Disabled
	public void borrarCliente_sinPedidos() {
		// Crear cliente
		TipoUsuario tipoUsr = new TipoUsuario(1, "Cliente");
		Usuario usr = new Usuario("HomeroJ", "siempreViva", tipoUsr);
		Cliente c1 = new Cliente("Cliente01", "20395783698", "Homero@gmail.com", 50000, true, null, usr, null);// Cliente
																												// dado
																												// de
																												// baja
																												// porque
																												// tiene
																												// fecha
																																																	// baja

		// Persisto el cliente
		clientService.guardarCliente(c1);

		// Crear microservicio pedido falso.

		RestTemplate cliRest = new RestTemplate();

		MockRestServiceServer server = MockRestServiceServer.bindTo(cliRest).build();

		server.expect(requestTo(API_REST_PEDIDO + "?idCliente=" + c1.getId())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess("", MediaType.APPLICATION_JSON));

		// Borro cliente
		clientService.borrarCliente(c1);
		// Busco cliente
		Optional<Cliente> clienteBorrado = clientService.buscarPorId(c1.getId());
		assertEquals(Optional.empty(), clienteBorrado);

	}

}
