package dan.ms.usuarios.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TipoUsuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //valor autonumerico
	@Column(name="ID_TIPO_USUARIO")
	private Integer id;
	private String tipo;
	
	public TipoUsuario(Integer id, String tipo) {
		super();
		this.id = id;
		this.tipo = tipo;
	}

	public TipoUsuario() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return "TipoUsuario [id=" + id + ", tipo=" + tipo + "]";
	}
	
	
}
