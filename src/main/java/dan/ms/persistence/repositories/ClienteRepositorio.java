package dan.ms.persistence.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import dan.ms.usuarios.domain.Cliente;
import frsf.isi.dan.InMemoryRepository;

@Repository
public class ClienteRepositorio extends InMemoryRepository<Cliente> {

	@Override
	public Integer getId(Cliente entity) {
		
		return entity.getId();
	}

	@Override
	public void setId(Cliente entity, Integer id) {
		entity.setId(id);

	}
	
	public Optional<Cliente> findByCuit(String cuit){
		Long cantClientes = this.count();
		Integer i=1;
		while(cantClientes >= 0) {
			Optional<Cliente> optCliente = this.findById(i);
			if(optCliente.isPresent()) {
				if(optCliente.get().getCuit().equals(cuit)) {
					return optCliente;
				}
			}
			i++;
			cantClientes--;
		}
		return Optional.empty();
	}
	public Optional<Cliente> findByRazonSocial(String razonSocial){
		Long cantClientes = this.count();
		Integer i=1;
		while(cantClientes >= 0) {
			Optional<Cliente> optCliente = this.findById(i);
			if(optCliente.isPresent()) {
				if(optCliente.get().getRazonSocial().equals(razonSocial)) {
					return optCliente;
				}
			}
			i++;
			cantClientes--;
		}
		return Optional.empty();
	}
	
	
	
	

}
