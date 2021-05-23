package dan.ms.usuarios.services.interfaces;

import java.util.List;
import java.util.Optional;

import dan.ms.usuarios.domain.Obra;
import dan.ms.usuarios.domain.TipoObra;

public interface ObraService {
	public Optional<Obra> guardarObra(Obra obra);
	
	public List<Obra> guardarObras(List<Obra> obra);
	
	public void borrarObras(List<Obra> listaObra);
	
	public Optional<Obra> buscarObra(Integer idObra);
	
	public Optional<Obra> buscarObra(Obra obra);
	
	public Optional<Obra> crearObraYAsignarselaAlCliente(Obra obra);
	
	public List<Obra> buscarObraPorIdCliente(Integer idCliente);
	
	public List<Obra> buscarObraPorTipoObra(Integer idTipoObra);
	
	public List<Obra> buscarObraPorCuitCliente(String cuitCliente);
	
	public Optional<Obra> actualizarObra(Obra obra);
}
