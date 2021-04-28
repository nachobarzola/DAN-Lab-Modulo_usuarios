package dan.ms.usuarios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dan.ms.persistence.repositories.ClienteRepository;

@Configuration
public class MiConfig {

	@Bean
	public ClienteRepository clienteRepository() {

		return new ClienteRepository();

	}

}
