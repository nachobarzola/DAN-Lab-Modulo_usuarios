package dan.ms.usuarios.services;

import org.springframework.stereotype.Service;

import dan.ms.usuarios.domain.Cliente;
import dan.ms.usuarios.services.interfaces.RiesgoBCRAService;

@Service
public class RiesgoBCRAServiceImp implements RiesgoBCRAService {

	@Override
	public Integer estadoCrediticio(Cliente cli) {

		return estadoAceptado();
	}

	private Integer estadoAceptado() {

		return NORMAL;

	}

	private Integer estadoRechazado() {

		return MEDIO;
	}

}
