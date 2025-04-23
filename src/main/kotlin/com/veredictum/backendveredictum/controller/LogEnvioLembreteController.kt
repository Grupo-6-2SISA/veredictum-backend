package com.veredictum.backendveredictum.controller

import com.veredictum.backendveredictum.repository.LogEnvioLembreteRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Log Envio Lembrete", description = "Endpoints para gerenciar Logs de Envios de Lembretes")
@RestController
@RequestMapping("/log-envio-lembrete")
class LogEnvioLembreteController(
    val repository: LogEnvioLembreteRepository
) {



}