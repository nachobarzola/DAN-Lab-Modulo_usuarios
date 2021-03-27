package dan.ms.usuarios.domain;

public class Empleado {
	private Integer id;
	private String mail;
	private Usuario user;
	
	public Empleado(Integer id, String mail, Usuario user) {
		super();
		this.id = id;
		this.mail = mail;
		this.user = user;
	}
	public Empleado(){super();}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public Usuario getUser() {
		return user;
	}
	public void setUser(Usuario user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "Empleado [id=" + id + ", mail=" + mail + ", user=" + user + "]";
	}
	

}
