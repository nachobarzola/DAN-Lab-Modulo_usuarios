package dan.ms.usuarios.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import dan.ms.usuarios.domain.Obra;
import dan.ms.usuarios.services.interfaces.ObraService;
import dan.ms.usuarios.services.interfaces.PedidoRestExternoService;

@Service
public class PedidoRestExternoServiceImp implements PedidoRestExternoService {
	@SuppressWarnings("rawtypes")
	@Autowired
	CircuitBreakerFactory circuitBreakerFactory;
	
	@Autowired
	RestTemplate restPedido;

	@Autowired
	ObraService obraService;
	
	private static String API_REST_PEDIDO = "http://modulo-pedidos/";
	private static String ENDPOINT_PEDIDO = "api/pedido";
	Boolean error=false;

	

	@Override
	public Boolean tienePedidos(Integer idCliente) {
		CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
		String uri = API_REST_PEDIDO + ENDPOINT_PEDIDO + "/obra/";
		//
		Object[] respuesta;
		Boolean tienePedidos=false;
		
		//
		List<Obra> listaObra = obraService.buscarObraPorIdCliente(idCliente);

		// Chequemos si tiene algun pedido entre todas las obras un cliente
		for (Obra unaObra : listaObra) {

			//respuesta = restUsuario.exchange(uri + unaObra.getId(), HttpMethod.GET, null, Object[].class);
			respuesta = circuitBreaker.run(() -> 
			restPedido.getForObject(uri + unaObra.getId(),Object[].class),
			throwable -> defaultResponse());
			if(respuesta!=null) {
			tienePedidos = (respuesta.length > 0);
			}
			if (tienePedidos || error) {
				return tienePedidos;
			}
		}
		return false;
	}

	public Object[] defaultResponse() {
		error=true;
		return null;
			
	}

}
