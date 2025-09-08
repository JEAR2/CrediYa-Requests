package co.com.crediya.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Requests Microservice",
                description = "Microservice for Requests."
        )
)
public class OpenApiConfig {

}