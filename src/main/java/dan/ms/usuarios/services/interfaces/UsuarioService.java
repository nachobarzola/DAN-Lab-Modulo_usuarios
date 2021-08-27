package dan.ms.usuarios.services.interfaces;

import java.util.Optional;

import dan.ms.usuarios.domain.Empleado;
import dan.ms.usuarios.domain.Usuario;

public interface UsuarioService {
	public Optional<Usuario> guardarUsuario(Usuario usuario);
	
	public Optional<Usuario> actualizarUsuario(Usuario usuario);
	
	public void borrarUsuario(Usuario usuario);
	
	public Optional<Usuario> getUsuario(Integer idUsuario);
}
