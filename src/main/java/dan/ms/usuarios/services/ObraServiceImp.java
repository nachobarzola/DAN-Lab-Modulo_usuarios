package dan.ms.usuarios.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dan.ms.usuarios.domain.Obra;
import dan.ms.usuarios.domain.TipoObra;
import dan.ms.usuarios.domain.TipoUsuario;
import dan.ms.usuarios.services.dao.ObraRepository;
import dan.ms.usuarios.services.dao.TipoObraRepository;
import dan.ms.usuarios.services.interfaces.ObraService;

@Service
public class ObraServiceImp implements ObraService{

	@Autowired
	ObraRepository obraRepo;
	
	@Autowired
	TipoObraRepository tipoObraRepo;
	
	
	@Override
	public Optional<Obra> guardarObra(Obra obra) {
		Optional<TipoObra> optTipoObra = tipoObraRepo.findById(obra.getTipo().getId());
		if(optTipoObra.isEmpty()) {
			return Optional.empty();
		}
		//Le asignamos el objeto tipo obra a la clase
		obra.setTipo(optTipoObra.get());
		//Guardamos la obra
		Obra obraReturn = obraRepo.save(obra);
		if(obraReturn == null) {
			return Optional.empty();
		}
		return Optional.of(obraReturn);
	}


	@Override
	public List<Obra> guardarObras(List<Obra> listaObra) {
		List<Obra> listaObraReturn=new ArrayList<>();
		for(Obra unaObra: listaObra) {
			Optional<Obra> optObraReturn = this.guardarObra(unaObra);
			if(optObraReturn.isEmpty()) {
				return null;
			}
			listaObraReturn.add(optObraReturn.get());
		}
		return listaObraReturn;
	}

	
	
}
