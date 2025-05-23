package com.knucl.FaceAnalyze.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("CodeArena Swagger")
                        .description("CodeArena 유저 및 인증, ps, 알림에 관한 REST API")
                        .version("1.0.0"));
    }
}
