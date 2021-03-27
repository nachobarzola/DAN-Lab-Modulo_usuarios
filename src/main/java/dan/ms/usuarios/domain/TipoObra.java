package dan.ms.usuarios.domain;

public class TipoObra {
	private Integer id;
	private String descripcion;
	
	
	public TipoObra(Integer id, String descripcion) {
		super();
		this.id = id;
		this.descripcion = descripcion;
	}
	public TipoObra() {
		super();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	@Override
	public String toString() {
		return "TipoObra [id=" + id + ", descripcion=" + descripcion + "]";
	}
	
	
}
