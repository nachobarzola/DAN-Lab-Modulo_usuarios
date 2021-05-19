package dan.ms.usuarios.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
public class Cliente {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //valor autonumerico
	@Column(name="ID_CLIENTE")
	private Integer id;
	
	private String razonSocial;
	private String cuit;
	private String mail;
	private Integer maxCuentaCorriente;
	private Boolean habilitadoOnline;
	
	@OneToMany(mappedBy = "cliente")
	private List<Obra> obras;
	
	@OneToOne
	@JoinColumn(name="ID_USUARIO")
	private Usuario user;

	@Temporal(TemporalType.DATE)
	private Date fechaBaja;
	
	public Cliente(String razonSocial, String cuit, String mail, Integer maxCuentaCorriente,
			Boolean habilitadoOnline, List<Obra> obras, Usuario user,Date fechaBaja) {
		super();
		this.razonSocial = razonSocial;
		this.cuit = cuit;
		this.mail = mail;
		this.maxCuentaCorriente = maxCuentaCorriente;
		this.habilitadoOnline = habilitadoOnline;
		this.obras = obras;
		this.user = user;
		this.fechaBaja = fechaBaja;
	}
	public Cliente() {
		super();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public String getCuit() {
		return cuit;
	}
	public void setCuit(String cuit) {
		this.cuit = cuit;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public Integer getMaxCuentaCorriente() {
		return maxCuentaCorriente;
	}
	public void setMaxCuentaCorriente(Integer maxCuentaCorriente) {
		this.maxCuentaCorriente = maxCuentaCorriente;
	}
	public Boolean getHabilitadoOnline() {
		return habilitadoOnline;
	}
	public void setHabilitadoOnline(Boolean habilitadoOnline) {
		this.habilitadoOnline = habilitadoOnline;
	}
	public List<Obra> getObras() {
		return obras;
	}
	public void setObras(List<Obra> obras) {
		this.obras = obras;
	}
	public Usuario getUser() {
		return user;
	}
	public void setUser(Usuario user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "Cliente [id=" + id + ", razonSocial=" + razonSocial + ", cuit=" + cuit + ", mail=" + mail
				+ ", maxCuentaCorriente=" + maxCuentaCorriente + ", habilitadoOnline=" + habilitadoOnline + ", obras="
				+ obras + ", user=" + user + "]";
	}
	public Date getFechaBaja() {
		return fechaBaja;
	}
	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
	}
	
	
	
	
	
}
