package dan.ms.usuarios.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import dan.ms.persistence.repositories.ClienteRepositorio;

@Configuration
public class MiConfig {

	@Bean
	public ClienteRepositorio clienteRepositorio() {

		return new ClienteRepositorio();

	}
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}


}
