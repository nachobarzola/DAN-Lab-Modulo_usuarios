package dan.ms.usuarios.services;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import dan.ms.usuarios.services.interfaces.PedidoRestExternoService;

@Service
public class PedidoRestExternoServiceImp implements PedidoRestExternoService{

	private static String API_REST_PEDIDO = "http://localhost:9002/api";
	private static String ENDPOINT_PEDIDO = "/pedido";
	
	@Override
	public Boolean tienePedidos(Integer idCliente) {
		RestTemplate restUsuario = new RestTemplate();
		String uri = API_REST_PEDIDO + ENDPOINT_PEDIDO;

		uri = uri + "?idCliente=" + idCliente;

		ResponseEntity<Object[]> respuesta = restUsuario.exchange(uri, HttpMethod.GET, null, Object[].class);
		Object[] pedidosRespuesta = respuesta.getBody();

		Boolean tienePedidos = (pedidosRespuesta.length > 0);
		return tienePedidos;
	}

}
