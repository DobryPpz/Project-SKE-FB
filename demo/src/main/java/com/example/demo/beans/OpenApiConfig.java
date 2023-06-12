package com.example.demo.beans;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI flashcardsAPI(){
        return new OpenAPI().info(
            new Info().title("API SKE:FB")
                    .description("Opis korzystania z API naszego projektu")
        );
    }
}
