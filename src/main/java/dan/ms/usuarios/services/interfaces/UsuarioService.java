package dan.ms.usuarios.services.interfaces;

import java.util.Optional;

import dan.ms.usuarios.domain.Usuario;

public interface UsuarioService {
	Optional<Usuario> guardarUsuario(Usuario usuario);
}
