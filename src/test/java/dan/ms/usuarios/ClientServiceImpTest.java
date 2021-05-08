package dan.ms.usuarios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import dan.ms.usuarios.domain.Cliente;
import dan.ms.usuarios.domain.Obra;
import dan.ms.usuarios.domain.TipoUsuario;
import dan.ms.usuarios.domain.Usuario;
import dan.ms.usuarios.services.interfaces.ClientService;


@SpringBootTest
public class ClientServiceImpTest {
	private String API_REST_PEDIDO = "http://localhost:8080/api/pedido";


	@Autowired
	private ClientService clientService;

	// Test de integracion con DB/repositorio
	@Test
	public void guardarCliente() {
		Obra o1 = new Obra();
		Obra o2 = new Obra();
		List<Obra> obras = new ArrayList<>();
		obras.add(o1);
		obras.add(o2);
		TipoUsuario tipoUsr = new TipoUsuario(1, "Cliente");
		Usuario usr = new Usuario(1, "HomeroJ", "siempreViva", tipoUsr);
		Cliente c1 = new Cliente(1, "Cliente01", "20395783698", "Homero@gmail.com", 50000, true, obras, usr, null);

		// Persisto el cliente
		Cliente cReturn = clientService.guardarCliente(c1);
		assertEquals(c1, cReturn);
	}

	// Test de intergracion con DB/repositorio
	@Test
	public void buscarCliente() {
		// Cliente 1
		Obra o1 = new Obra();
		Obra o2 = new Obra();
		List<Obra> obras = new ArrayList<>();
		obras.add(o1);
		obras.add(o2);
		TipoUsuario tipoUsr = new TipoUsuario(1, "Cliente");
		Usuario usr = new Usuario(1, "HomeroJ", "siempreViva", tipoUsr);
		Cliente c1 = new Cliente(1, "Cliente01", "20395783698", "homero@gmail.com", 50000, true, obras, usr, null);
		// Cliente 2
		Usuario usr2 = new Usuario(2, "Bar", "siempreViva123", tipoUsr);
		Cliente c2 = new Cliente(2, "Cliente02", "20752489659", "bar@gmail.com", 50000, true, obras, usr2, null);
		// Cliente 3;
		Usuario usr3 = new Usuario(3, "Lisa", "siempreViva1234", tipoUsr);
		Cliente c3 = new Cliente(3, "Cliente03", "20741857962", "lisa@gmail.com", 50000, true, obras, usr3, null);

		// Persisto a los clientes
		clientService.guardarCliente(c1);
		clientService.guardarCliente(c2);
		clientService.guardarCliente(c3);
		// Lo busco a donde se persistio
		Optional<Cliente> optC = clientService.buscarPorId(c1.getId());
		assertEquals(c1, optC.get());
		Optional<Cliente> optC2 = clientService.buscarPorRazonSocial(c2.getRazonSocial());
		assertEquals(c2, optC2.get());
		Optional<Cliente> optC3 = clientService.buscarPorCuit(c3.getCuit());
		assertEquals(c3, optC3.get());

	}

	// Test de intergracion con DB/repositorio
	@Test
	public void buscarCliente_DadoDeBaja() {
		Obra o1 = new Obra();
		Obra o2 = new Obra();
		List<Obra> obras = new ArrayList<>();
		obras.add(o1);
		obras.add(o2);
		TipoUsuario tipoUsr = new TipoUsuario(1, "Cliente");
		Usuario usr = new Usuario(1, "HomeroJ", "siempreViva", tipoUsr);
		Date fechaBaja = new Date(System.currentTimeMillis());
		// Cliente dado de baja porque tiene fecha de baja
		Cliente c1 = new Cliente(1, "Cliente01", "20395783698", "Homero@gmail.com", 50000, true, obras, usr, fechaBaja);

		// Persisto el cliente
		clientService.guardarCliente(c1);

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
	//TODO: NO SE COMO IMPLEMENTAR ESTE TEST:
	@Disabled
	public void borrarCliente_sinPedidos() {
		//Crear cliente
		TipoUsuario tipoUsr = new TipoUsuario(1, "Cliente");
		Usuario usr = new Usuario(1, "HomeroJ", "siempreViva", tipoUsr);
		Cliente c1 = new Cliente(1, "Cliente01", "20395783698", "Homero@gmail.com", 
				50000, true, null, usr, null);// Cliente dado de baja porque tiene fecha de baja

		// Persisto el cliente
		clientService.guardarCliente(c1);
		
		//Crear microservicio pedido falso.
		
		RestTemplate cliRest= new RestTemplate();
		
		MockRestServiceServer server = MockRestServiceServer.bindTo(cliRest).build();
		
		server.expect(requestTo(API_REST_PEDIDO+"?idCliente=" + c1.getId()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess("",MediaType.APPLICATION_JSON));
		
		// Borro cliente
		clientService.borrarCliente(c1);
		//Busco cliente
		Optional<Cliente> clienteBorrado = clientService.buscarPorId(c1.getId());
		assertEquals(Optional.empty(), clienteBorrado);		
				
	

	}

}
