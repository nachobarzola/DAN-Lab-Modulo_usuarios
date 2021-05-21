package dan.ms.usuarios.services;

import java.util.Date;
import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import dan.ms.usuarios.domain.Cliente;
import dan.ms.usuarios.domain.Obra;
import dan.ms.usuarios.domain.Usuario;
import dan.ms.usuarios.services.dao.ClienteRepository;
import dan.ms.usuarios.services.dao.UsuarioRepository;
import dan.ms.usuarios.services.interfaces.ClientService;
import dan.ms.usuarios.services.interfaces.ObraService;
import dan.ms.usuarios.services.interfaces.PedidoRestExternoService;
import dan.ms.usuarios.services.interfaces.RiesgoBCRAService;
import dan.ms.usuarios.services.interfaces.UsuarioService;

@Service
public class ClientServiceImp implements ClientService {



	// -------------Repositories
	@Autowired
	ClienteRepository clienteRepo;

	// -------------SERVICES
	@Autowired
	ObraService obraService;

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	RiesgoBCRAService riesgoBcra;
	
	@Autowired
	PedidoRestExternoService pedidoRestExternaService;

	@Override
	public Optional<Cliente> guardarCliente(Cliente clienteNuevo) {
		if (tieneRiesgoCrediticio(clienteNuevo)) {
			return Optional.empty();
		}
		// Cliente que voy a guadar al final en la base de datos
		Cliente clienteAGuardar = new Cliente();
		clienteAGuardar.setCuit(clienteNuevo.getCuit());
		clienteAGuardar.setFechaBaja(clienteNuevo.getFechaBaja());
		clienteAGuardar.setHabilitadoOnline(clienteNuevo.getHabilitadoOnline());
		clienteAGuardar.setMail(clienteNuevo.getMail());
		clienteAGuardar.setMaxCuentaCorriente(clienteNuevo.getMaxCuentaCorriente());
		clienteAGuardar.setRazonSocial(clienteNuevo.getRazonSocial());

		// Guardamos el asuario del cliente
		Optional<Usuario> optUsuario = usuarioService.guardarUsuario(clienteNuevo.getUser());
		if (optUsuario.isEmpty()) {
			// No puedo guardar el usuario
			return Optional.empty();
		}
		// Asignamos el usuario guardado al clienteAGuadar
		clienteAGuardar.setUser(optUsuario.get());

		// ---------Obra es la dueña de la relacion, obra guarda la relacion con cliente
		// Por lo tanto primero guardamos el cliente y despues las obras
		clienteAGuardar = clienteRepo.save(clienteAGuardar);
		if (clienteAGuardar == null) {
			// no pudo guardar el cliente
			return Optional.empty();
		}
		// Guardamos las obras
		List<Obra> listaObrasReturn = obraService.guardarObras(clienteNuevo.getObras());
		if (listaObrasReturn == null) {
			// no pudo guardar la lista de obras
			return Optional.empty();
		}
		// Le asignamos las obras al clinteAGuardar para que quede consiste el modelo de
		// objetos
		clienteAGuardar.setObras(listaObrasReturn);

		return Optional.of(clienteAGuardar);
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
		// TODO: retornar un opcional en todos
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
		// TODO: retornar un opcional en todos
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
		Boolean tienePedidos = pedidoRestExternaService.tienePedidos(cli.getId());
		if (tienePedidos) {
			Date date = new Date(System.currentTimeMillis());
			cli.setFechaBaja(date);
			actualizarCliente(cli);

		} else {
			this.obraService.borrarObras(cli.getObras());
			cli.setObras(null);
			Usuario usuarioABorrar= cli.getUser();
			this.clienteRepo.delete(cli);
			this.usuarioService.borrarUsuario(usuarioABorrar);
			cli = null;
		}

	}

	@Override
	public Optional<Cliente> actualizarCliente(Cliente cli) {
		return Optional.of(clienteRepo.save(cli));
	}

	private Boolean dadoDeBaja(Cliente cli) {

		return cli.getFechaBaja() != null;
	}

}
