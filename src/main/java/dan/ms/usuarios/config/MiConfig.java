package dan.ms.usuarios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dan.ms.persistence.repositories.ClienteRepositorio;

@Configuration
public class MiConfig {

	@Bean
	public ClienteRepositorio clienteRepositorio() {

		return new ClienteRepositorio();

	}

}
