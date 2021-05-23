package dan.ms.usuarios.services.interfaces;

import java.util.List;
import java.util.Optional;

import dan.ms.usuarios.domain.Obra;

public interface ObraService {
	public Optional<Obra> guardarObra(Obra obra);
	
	public List<Obra> guardarObras(List<Obra> obra);
	
	public void borrarObras(List<Obra> listaObra);
	
	public Optional<Obra> buscarObra(Integer idObra);
	
	public Optional<Obra> buscarObra(Obra obra);
}
