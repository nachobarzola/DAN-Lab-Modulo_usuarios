package dan.ms.usuarios.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dan.ms.usuarios.domain.TipoUsuario;
import dan.ms.usuarios.domain.Usuario;
import dan.ms.usuarios.services.dao.TipoUsuarioRepository;
import dan.ms.usuarios.services.dao.UsuarioRepository;
import dan.ms.usuarios.services.interfaces.UsuarioService;

@Service
public class UsuarioServiceImp implements UsuarioService{

	@Autowired
	UsuarioRepository usuarioRepo;
	
	@Autowired
	TipoUsuarioRepository tipoUsuarioRepo;
	
	@Override
	public Optional<Usuario> guardarUsuario(Usuario usuario) {
		//TODO: No funca esta busqueda porque en ningun momento se cargan esos valores preestablecidos
		Optional<TipoUsuario> optTipoU = tipoUsuarioRepo.findById(usuario.getTipoUsuario().getId());
		System.out.println("[debug-UsuarioServiceImp-Mtdo: guardarUsuario]El tipo de usuario retornado dio null: "+optTipoU.isEmpty());
		if(optTipoU.isPresent()) {
			//Se encontro el tipo usuario
			usuario.setTipoUsuario(optTipoU.get());
			return Optional.of(usuarioRepo.save(usuario));
		}
		
		return Optional.empty();
	}

}
