package dan.ms.usuarios.domain;

public class Obra {
	private Integer id;
	private String descripcion;
	private Float latitud;
	private Float longitud;
	private String direccion;
	private Integer superficie;
	private Cliente cliente;
	private TipoObra tipo;
	
	public Obra(Integer id, String descripcion, Float latitud, Float longitud, String direccion, Integer superficie,
			Cliente cliente, TipoObra tipo) {
		super();
		this.id = id;
		this.descripcion = descripcion;
		this.latitud = latitud;
		this.longitud = longitud;
		this.direccion = direccion;
		this.superficie = superficie;
		this.cliente = cliente;
		this.tipo = tipo;
	}
	public Obra() {super();}
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
	public Float getLatitud() {
		return latitud;
	}
	public void setLatitud(Float latitud) {
		this.latitud = latitud;
	}
	public Float getLongitud() {
		return longitud;
	}
	public void setLongitud(Float longitud) {
		this.longitud = longitud;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public Integer getSuperficie() {
		return superficie;
	}
	public void setSuperficie(Integer superficie) {
		this.superficie = superficie;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public TipoObra getTipo() {
		return tipo;
	}
	public void setTipo(TipoObra tipo) {
		this.tipo = tipo;
	}
	@Override
	public String toString() {
		return "Obra [id=" + id + ", descripcion=" + descripcion + ", latitud=" + latitud + ", longitud=" + longitud
				+ ", direccion=" + direccion + ", superficie=" + superficie + ", cliente=" + cliente + ", tipo=" + tipo
				+ "]";
	}

	
	
	
	
}
