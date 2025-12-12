package ua.edu.viti.military.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Military Warehouse API - Система управління військовим транспортом")
                        .version("1.0.0")
                        .description(
                                "REST API для системи управління військовим транспортом та логістикою.\n\n" +
                                        "**Функціональність:**\n" +
                                        "- Управління категоріями транспорту\n" +
                                        "- CRUD операції з транспортом (Vehicle)\n" +
                                        "- Управління водіями (Driver)\n" +
                                        "- Контроль технічного обслуговування (ТО)\n" +
                                        "- Контроль пробігу та палива\n\n" +
                                        "**Технології:** Spring Boot 3.2, Spring Data JPA, PostgreSQL, Lombok, Swagger/OpenAPI 3.0"
                        )
                        .contact(new Contact()
                                .name("ВНЗ ВІТС")
                                .email("support@vitis.edu.ua")
                                .url("https://vitis.edu.ua")
                        )
                        .license(new License()
                                .name("Educational Use")
                                .url("https://opensource.org/licenses/MIT")
                        )
                )
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server"),
                        new Server()
                                .url("https://api.military.example.com")
                                .description("Production Server")
                ));
    }
}
