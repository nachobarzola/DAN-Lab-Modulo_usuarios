package dan.ms.usuarios.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(UsuarioServiceImp.class);

	
	@Override
	public Optional<Usuario> guardarUsuario(Usuario usuario) {
		Optional<TipoUsuario> optTipoU = tipoUsuarioRepo.findById(usuario.getTipoUsuario().getId());
		if(optTipoU.isPresent()) {
			//Se encontro el tipo usuario
			usuario.setTipoUsuario(optTipoU.get());
			return Optional.of(usuarioRepo.save(usuario));
		}
		
		return Optional.empty();
	}

	@Override
	public void borrarUsuario(Usuario usuario) {
		usuarioRepo.delete(usuario);
	}

	@Override
	public Optional<Usuario> actualizarUsuario(Usuario usuario) {
		//Debe existir el usuario
		Optional<Usuario> optUsuarioBuscado = usuarioRepo.findById(usuario.getId());
		if(optUsuarioBuscado.isPresent()) {
			optUsuarioBuscado.get().setPassword(usuario.getPassword());
			optUsuarioBuscado.get().setUser(usuario.getUser());
			optUsuarioBuscado.get().setTipoUsuario(usuario.getTipoUsuario());
			return this.guardarUsuario(optUsuarioBuscado.get());
		}
		return Optional.empty();
	}
	
	@Override
	public Optional<Usuario> getUsuario(Integer idUsuario) {
		logger.info("Solicitud de obtenciion del usuario: " + idUsuario);
		try {
			Optional<Usuario> optUsuario = usuarioRepo.findById(idUsuario);
			// Chequemos si la encontro
			if (optUsuario.isEmpty()) {
				throw new Exception("No se encontro el usuario con la id: " + idUsuario + " en la DB");
			}
			logger.debug("Se encontro el usario con la id: " + idUsuario);
			return optUsuario;

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			logger.error("La id recibida es null");
			return Optional.empty();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("No se encontro el usuario con la id: " + idUsuario + " en la DB");
			return Optional.empty();
		}
	}

}
