package dan.ms.usuarios.domain;

public class Usuario {
	private Integer id;
	private String user;
	private String password;
	private TipoUsuario tipoUsuario;
	
	public Usuario(Integer id, String user, String password, TipoUsuario tipoUsuario) {
		super();
		this.id = id;
		this.user = user;
		this.password = password;
		this.tipoUsuario = tipoUsuario;
	}

	public Usuario() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public TipoUsuario getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(TipoUsuario tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", user=" + user + ", password=" + password + ", tipoUsuario=" + tipoUsuario + "]";
	}
	
	
	

}
