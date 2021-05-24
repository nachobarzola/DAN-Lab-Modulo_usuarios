package dan.ms.usuarios.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import dan.ms.usuarios.domain.Obra;
import dan.ms.usuarios.services.interfaces.ObraService;
import dan.ms.usuarios.services.interfaces.PedidoRestExternoService;

@Service
public class PedidoRestExternoServiceImp implements PedidoRestExternoService{

	private static String API_REST_PEDIDO = "http://localhost:9002/";
	private static String ENDPOINT_PEDIDO = "api/pedido";
	
	@Autowired
	ObraService obraService;
	
	
	@Override
	public Boolean tienePedidos(Integer idCliente) {
		RestTemplate restUsuario = new RestTemplate();
		String uri = API_REST_PEDIDO + ENDPOINT_PEDIDO;
		//
		ResponseEntity<Object[]> respuesta;
		Object[] pedidosRespuesta;
		Boolean tienePedidos;
		//
		List<Obra> listaObra = obraService.buscarObraPorIdCliente(idCliente);
		
		//Chequemos si tiene pedidos cada obra de un cliente
		for(Obra unaObra: listaObra) {
			uri = uri+ "/obra/" + unaObra.getId();
			respuesta = restUsuario.exchange(uri, HttpMethod.GET, null, Object[].class);
			pedidosRespuesta = respuesta.getBody();

			tienePedidos = (pedidosRespuesta.length > 0);
			if(tienePedidos) {
				return true;
			}
		}
		return false;
	}

}
