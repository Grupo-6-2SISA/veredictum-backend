package com.veredictum.backendveredictum

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfig {

    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {  // <- aqui usamos object anônimo
            override fun addCorsMappings(registry: CorsRegistry) {
                println("[#INFO] Configurando CORS")
                registry.addMapping("/**")
                    .allowedOriginPatterns("http://localhost:*") // permite qualquer porta localhost
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true)
            }
        }
    }
}
