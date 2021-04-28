package dan.ms.usuarios.services.interfaces;

import dan.ms.usuarios.domain.Cliente;

public interface RiesgoBCRAService {
	
	public static final Integer NORMAL = 1;
	public static final Integer BAJO = 2;
	public static final Integer MEDIO = 3;
	public static final Integer ALTO = 4;
	public static final Integer IRRECUPERABLE = 5;

	public Integer estadoCrediticio(Cliente cli);
	
	
	
}
