package dan.ms.usuarios.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import dan.ms.usuarios.domain.Cliente;
import dan.ms.usuarios.services.dao.ClienteRepository;
import dan.ms.usuarios.services.dao.UsuarioRepository;
import dan.ms.usuarios.services.interfaces.ClientService;
import dan.ms.usuarios.services.interfaces.RiesgoBCRAService;

@Service
public class ClientServiceImp implements ClientService {

	private static String API_REST_PEDIDO = "http://localhost:8080/api";
	private static String ENDPOINT_PEDIDO = "/pedido";
	

	@Autowired
	ClienteRepository clienteRepo;
	@Autowired
	UsuarioRepository usuarioRepo;

	@Autowired
	RiesgoBCRAService riesgoBcra;

	@Override
	public Optional<Cliente> guardarCliente(Cliente clienteNuevo) {

		if (tieneRiesgoCrediticio(clienteNuevo)) {
			return Optional.empty();
		}
		return Optional.of(clienteRepo.save(clienteNuevo));
	}

	@Override
	public Optional<Cliente> buscarPorId(Integer id) {

		Optional<Cliente> optcl = this.clienteRepo.findById(id);

		if (optcl.isPresent()) {
			if (!dadoDeBaja(optcl.get())) {
				return optcl;
			}
		}
		return Optional.empty();

	}

	@Override
	public Optional<Cliente> buscarPorCuit(String cuit) {
		Optional<Cliente> optCliente = Optional.of(this.clienteRepo.findByCuit(cuit));

		if (optCliente.isPresent()) {
			if (!dadoDeBaja(optCliente.get())) {
				return optCliente;
			}
		}
		//TODO: retornar un opcional en todos
		return Optional.empty();
	}

	@Override
	public Optional<Cliente> buscarPorRazonSocial(String razonSocial) {
		Optional<Cliente> optCliente = Optional.of(clienteRepo.findByRazonSocial(razonSocial));

		if (optCliente.isPresent()) {
			if (!dadoDeBaja(optCliente.get())) {
				return optCliente;
			}
		}
		//TODO: retornar un opcional en todos
		return Optional.empty();
	}
	private Boolean tieneRiesgoCrediticio(Cliente cli) {

		if (riesgoBcra.estadoCrediticio(cli) != (1 | 2)) {
			return false;
		}

		return true;

	}

	/*
	 * Para dar de baja un cliente, no se puede eliminar si ya ha realizado algun
	 * pedido, por lo que en ese caso, debe agregar un atributo, "fechaBaja" y
	 * asignarle una fecha. Todos los clientes activos son aquellos que no tienen
	 * fechaBaja (o es null).
	 * 
	 */
	@Override
	public void borrarCliente(Cliente cli) {

		RestTemplate restUsuario = new RestTemplate();
		String uri = API_REST_PEDIDO + ENDPOINT_PEDIDO;

		uri = uri + "?idCliente=" + cli.getId();

		ResponseEntity<Object[]> respuesta = restUsuario.exchange(uri, HttpMethod.GET, null, Object[].class);
		Object[] pedidosRespuesta = respuesta.getBody();

		Boolean tienePedidos = (pedidosRespuesta.length > 0);

		if (tienePedidos) {
			Date date = new Date(System.currentTimeMillis());
			cli.setFechaBaja(date);
			actualizarCliente(cli);

		} else {
			this.clienteRepo.delete(cli);

		}

	}

	@Override
	public Cliente actualizarCliente(Cliente cli) {
		// TODO Falta implementar actualizar cliente
		return null;
	}

	private Boolean dadoDeBaja(Cliente cli) {

		return cli.getFechaBaja() != null;
	}


}
