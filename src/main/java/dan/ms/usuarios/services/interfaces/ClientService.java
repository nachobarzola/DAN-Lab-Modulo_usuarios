package dan.ms.usuarios.services.interfaces;

import java.util.Optional;

import dan.ms.usuarios.domain.Cliente;


public interface ClientService {

	public Cliente guardarCliente(Cliente cli);

	public Optional<Cliente> buscarPorId(Integer id);
	
	public void borrarCliente(Cliente cli);
	
	public Cliente actualizarCliente(Cliente cli);
	
}
