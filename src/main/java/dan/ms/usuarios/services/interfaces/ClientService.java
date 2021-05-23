package dan.ms.usuarios.services.interfaces;

import java.util.Optional;
import java.util.List;

import dan.ms.usuarios.domain.Cliente;
import dan.ms.usuarios.domain.Obra;


public interface ClientService {

	public Optional<Cliente> guardarCliente(Cliente cli);
	
	public Optional<Cliente> buscarPorId(Integer id);
	
	public Optional<Cliente> buscarPorCuit(String cuit);
	
	public Optional<Cliente> buscarPorRazonSocial(String razonSocial);
	
	public Optional<Cliente> buscarPorIdObra(Integer idObra);
	
	public Optional<Cliente> buscarPorObra(Obra obra);
	
	public Boolean borrarCliente(Cliente cli);
	
	public Optional<Cliente> actualizarCliente(Cliente cli);
	
	public List<Cliente> getAllCliente();
	
}
