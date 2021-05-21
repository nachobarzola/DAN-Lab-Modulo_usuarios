package dan.ms.usuarios.services.interfaces;

import java.util.Optional;

import dan.ms.usuarios.domain.Cliente;


public interface ClientService {

	public Optional<Cliente> guardarCliente(Cliente cli);
	

	public Optional<Cliente> buscarPorId(Integer id);
	
	public Optional<Cliente> buscarPorCuit(String cuit);
	
	public Optional<Cliente> buscarPorRazonSocial(String razonSocial);
	
	public Boolean borrarCliente(Cliente cli);
	
	public Optional<Cliente> actualizarCliente(Cliente cli);
	
}
