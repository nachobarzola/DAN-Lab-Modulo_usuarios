package dan.ms.usuarios.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dan.ms.usuarios.domain.Cliente;
import dan.ms.usuarios.domain.Obra;
import dan.ms.usuarios.domain.TipoObra;
import dan.ms.usuarios.domain.TipoUsuario;
import dan.ms.usuarios.services.dao.ObraRepository;
import dan.ms.usuarios.services.dao.TipoObraRepository;
import dan.ms.usuarios.services.interfaces.ClientService;
import dan.ms.usuarios.services.interfaces.ObraService;

@Service
public class ObraServiceImp implements ObraService {

	@Autowired
	ObraRepository obraRepo;

	@Autowired
	TipoObraRepository tipoObraRepo;

	@Autowired
	ClientService clienteService;

	@Override
	public Optional<Obra> guardarObra(Obra obra) {
		Obra obraAGuardar = new Obra();
		obraAGuardar.setDescripcion(obra.getDescripcion());
		obraAGuardar.setDireccion(obra.getDireccion());
		obraAGuardar.setLatitud(obra.getLatitud());
		obraAGuardar.setLongitud(obra.getLongitud());
		obraAGuardar.setSuperficie(obra.getSuperficie());
		// ----------------------------------------------------------------------
		// Buscamos el objeto tipoUsuario para asignarselo a la obra
		Optional<TipoObra> optTipoObra = tipoObraRepo.findById(obra.getTipo().getId());
		if (optTipoObra.isEmpty()) {
			// No se encontro el tipo obra
			return Optional.empty();
		}
		// Le asignamos el objeto tipo obra a la clase obraAGuardar
		obraAGuardar.setTipo(optTipoObra.get());
		// ----------------------------------------------------------------------
		// Asociamos el cliente de la obra.
		Cliente clienteAsociadoConObra = obra.getCliente();
		// Recuperamos el cliente guardado
		Optional<Cliente> optClienteAsociadoConObra = clienteService.buscarPorCuit(clienteAsociadoConObra.getCuit());
		if (optClienteAsociadoConObra.isPresent()) {
			// El cliente ya existe, debemos solo asignarselo a la obra
			obraAGuardar.setCliente(optClienteAsociadoConObra.get());
		}
		// ----------------------------------------------------------------------
		// Guardamos la obra
		obraAGuardar = obraRepo.save(obraAGuardar);
		if (obraAGuardar == null) {
			return Optional.empty();
		}
		return Optional.of(obraAGuardar);
	}

	@Override
	public List<Obra> guardarObras(List<Obra> listaObra) {
		List<Obra> listaObraReturn = new ArrayList<>();
		for (Obra unaObra : listaObra) {
			Optional<Obra> optObraReturn = this.guardarObra(unaObra);
			if (optObraReturn.isEmpty()) {
				return null;
			}
			listaObraReturn.add(optObraReturn.get());
		}
		return listaObraReturn;
	}

	@Override
	public void borrarObras(List<Obra> listaObra) {
		for (Obra unaObra : listaObra) {
			obraRepo.delete(unaObra);
		}
	}

	@Override
	public Optional<Obra> buscarObra(Integer idObra) {
		return obraRepo.findById(idObra);
	}

	@Override
	public Optional<Obra> buscarObra(Obra obra) {
		Optional<Obra> optObraBuscada = obraRepo.findById(obra.getId());
		if (optObraBuscada.isPresent()) {
			if (!optObraBuscada.get().getLatitud().equals(obra.getLatitud())) {
				return Optional.empty();
			}
			if (!optObraBuscada.get().getLongitud().equals(obra.getLongitud())) {
				return Optional.empty();
			}
			if (!optObraBuscada.get().getDescripcion().equals(obra.getDescripcion())) {
				return Optional.empty();
			}
			if (!optObraBuscada.get().getSuperficie().equals(obra.getSuperficie())) {
				return Optional.empty();
			}
			if (!optObraBuscada.get().getDireccion().equals(obra.getDireccion())) {
				return Optional.empty();
			}
			return optObraBuscada;

		}
		return Optional.empty();
	}

}
