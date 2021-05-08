package dan.ms.usuarios;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import dan.ms.usuarios.domain.Cliente;
import dan.ms.usuarios.domain.Obra;
import dan.ms.usuarios.domain.TipoObra;
import dan.ms.usuarios.domain.TipoUsuario;
import dan.ms.usuarios.domain.Usuario;
import dan.ms.usuarios.services.interfaces.ClientService;

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
	
	//Test de integracion
	@Test
	void crear_clienteCompleto() {
		String server = "http://localhost:"+ puerto +ENDPOINT_CLIENTE;
		// • Cuando se da de alta un Cliente, se debe indicar al menos una Obra con su
		// Tipo de Obra
		// y la información de usuario y clave para crear el usuario.
		
		//Creo un cliente
		Obra o1 = new Obra();
		o1.setTipo(new TipoObra(1,"REFORMA"));
		List<Obra> obras = new ArrayList<>();
		obras.add(o1);
		TipoUsuario tipoUsr = new TipoUsuario(1, "Cliente");
		Usuario usr = new Usuario(1, "HomeroJ", "siempreViva", tipoUsr);
		// Cliente dado de baja porque tiene fecha de baja
		Cliente cNuevo = new Cliente(1, "Cliente01", "20395783698", "Homero@gmail.com", 50000, true, obras, usr, null);

		
		HttpEntity<Cliente> requestCliente = new HttpEntity<>(cNuevo); 
		ResponseEntity<Cliente> respuesta = restTemplate.exchange(server,  
				HttpMethod.POST, requestCliente, Cliente.class);
		
		assertTrue(respuesta.getStatusCode().equals(HttpStatus.OK));
		//Chequeo que este persistido
		Optional<Cliente> cli = clienteService.buscarPorId(cNuevo.getId());
		assertTrue(cli.isPresent());
		
	}
	//Test unitario
	@Test
	void crear_clienteIncompleto_faltaTipoObra() {
		String server = "http://localhost:"+ puerto +ENDPOINT_CLIENTE;
		// • Cuando se da de alta un Cliente, se debe indicar al menos una Obra con su
		// Tipo de Obra
		// y la información de usuario y clave para crear el usuario.
			
		//Creo un cliente
		Obra o1 = new Obra();
		//o1.setTipo(new TipoObra(1,"REFORMA")); FALTA ESTO!!
		List<Obra> obras = new ArrayList<>();
		obras.add(o1);
		TipoUsuario tipoUsr = new TipoUsuario(1, "Cliente");
		Usuario usr = new Usuario(1, "HomeroJ", "siempreViva", tipoUsr);
		// Cliente dado de baja porque tiene fecha de baja
		Cliente cNuevo = new Cliente(1, "Cliente01", "20395783698", "Homero@gmail.com", 50000, true, obras, usr, null);

			
		HttpEntity<Cliente> requestCliente = new HttpEntity<>(cNuevo); 
		ResponseEntity<Cliente> respuesta = testRestTemplate.exchange(server,  
				HttpMethod.POST, requestCliente, Cliente.class);
		
		assertTrue(respuesta.getStatusCode().equals(HttpStatus.BAD_REQUEST));
		
		//Chequeo que no este persistido
		Optional<Cliente> cli = clienteService.buscarPorId(cNuevo.getId());
		System.out.println(cli.get().getRazonSocial()+" "+cli.get().getMail());
		assertTrue(cli.isEmpty());
			
	}
	//Test unitario
	@Test
	void crear_clienteIncompleto_faltaTipoUser() {
		String server = "http://localhost:"+ puerto +ENDPOINT_CLIENTE;
		//Creo un cliente
		Obra o1 = new Obra();
		o1.setTipo(new TipoObra(1,"REFORMA")); 
		List<Obra> obras = new ArrayList<>();
		obras.add(o1);
		//TipoUsuario tipoUsr = new TipoUsuario(1, "Cliente"); FALTA ESTO!!
		Usuario usr = new Usuario(1, "HomeroJ", "siempreViva", null);
		// Cliente dado de baja porque tiene fecha de baja
		Cliente cNuevo = new Cliente(1, "Cliente01", "20395783698", "Homero@gmail.com", 50000, true, obras, usr, null);

				
		HttpEntity<Cliente> requestCliente = new HttpEntity<>(cNuevo); 
		ResponseEntity<Cliente> respuesta = testRestTemplate.exchange(server,  
				HttpMethod.POST, requestCliente, Cliente.class);
			
		assertTrue(respuesta.getStatusCode().equals(HttpStatus.BAD_REQUEST));
			
		//Chequeo que no este persistido
		Optional<Cliente> cli = clienteService.buscarPorId(cNuevo.getId());
		System.out.println(cli.get().getRazonSocial()+" "+cli.get().getMail());
		assertTrue(cli.isEmpty());
				
	}
	//Test unitario
	@Test
	void crear_clienteIncompleto_faltaUser() {
		String server = "http://localhost:"+ puerto +ENDPOINT_CLIENTE;
		//Creo un cliente
		Obra o1 = new Obra();
		o1.setTipo(new TipoObra(1,"REFORMA")); 
		List<Obra> obras = new ArrayList<>();
		obras.add(o1);
		TipoUsuario tipoUsr = new TipoUsuario(1, "Cliente"); 
		//Usuario usr = new Usuario(1, "HomeroJ", "siempreViva", tipoUsr); FALTA ESTO!!
		// Cliente dado de baja porque tiene fecha de baja
		Cliente cNuevo = new Cliente(1, "Cliente01", "20395783698", "Homero@gmail.com", 50000, true, obras, null, null);

					
		HttpEntity<Cliente> requestCliente = new HttpEntity<>(cNuevo); 
		ResponseEntity<Cliente> respuesta = testRestTemplate.exchange(server,  
				HttpMethod.POST, requestCliente, Cliente.class);
				
		assertTrue(respuesta.getStatusCode().equals(HttpStatus.BAD_REQUEST));
				
		//Chequeo que no este persistido
		Optional<Cliente> cli = clienteService.buscarPorId(cNuevo.getId());
		System.out.println(cli.get().getRazonSocial()+" "+cli.get().getMail());
		assertTrue(cli.isEmpty());
					
	}
	//Test unitario
		@Test
		void crear_clienteIncompleto_faltaPasswordUser() {
			String server = "http://localhost:"+ puerto +ENDPOINT_CLIENTE;
			//Creo un cliente
			Obra o1 = new Obra();
			o1.setTipo(new TipoObra(1,"REFORMA")); 
			List<Obra> obras = new ArrayList<>();
			obras.add(o1);
			TipoUsuario tipoUsr = new TipoUsuario(1, "Cliente"); 
			String password = null; //FALTA ESTO!!
			Usuario usr = new Usuario(1, "HomeroJ", password, tipoUsr); 
			// Cliente dado de baja porque tiene fecha de baja
			Cliente cNuevo = new Cliente(1, "Cliente01", "20395783698", "Homero@gmail.com", 50000, true, obras, usr, null);

						
			HttpEntity<Cliente> requestCliente = new HttpEntity<>(cNuevo); 
			ResponseEntity<Cliente> respuesta = testRestTemplate.exchange(server,  
					HttpMethod.POST, requestCliente, Cliente.class);
					
			assertTrue(respuesta.getStatusCode().equals(HttpStatus.BAD_REQUEST));
					
			//Chequeo que no este persistido
			Optional<Cliente> cli = clienteService.buscarPorId(cNuevo.getId());
			System.out.println(cli.get().getRazonSocial()+" "+cli.get().getMail());
			assertTrue(cli.isEmpty());
						
		}


}
