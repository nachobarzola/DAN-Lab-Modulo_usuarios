package dan.ms.usuarios.services.interfaces;

import java.util.List;
import java.util.Optional;

import dan.ms.usuarios.domain.Obra;

public interface ObraService {
	public Optional<Obra> guardarObra(Obra obra);
	
	public List<Obra> guardarObras(List<Obra> obra);
}
