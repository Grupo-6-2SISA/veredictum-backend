package com.veredictum.backendveredictum

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfig {

    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                println("[#INFO] Configurando CORS - permissivo")

                registry.addMapping("/**")             // todos os endpoints
                    .allowedOriginPatterns("*")        // qualquer origem
                    .allowedMethods("*")               // todos os métodos (GET, POST, PATCH, DELETE...)
                    .allowedHeaders("*")               // qualquer header
                    .allowCredentials(true)            // permite cookies/autenticação
                    .maxAge(3600)                      // cache do preflight (1h)
            }
        }
    }
}
