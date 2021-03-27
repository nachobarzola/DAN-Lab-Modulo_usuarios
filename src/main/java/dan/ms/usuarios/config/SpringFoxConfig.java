package dan.ms.usuarios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/*Esta clase nos permite configurar el swagger que nos documentara las APIs
* Para ver la api entrar en http://localhost:8080/swagger-ui/
*/

@Configuration
@EnableSwagger2
public class SpringFoxConfig {
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("dan.ms.usuarios.rest"))
				.paths(PathSelectors.any())
				.build();
		
	}
}
