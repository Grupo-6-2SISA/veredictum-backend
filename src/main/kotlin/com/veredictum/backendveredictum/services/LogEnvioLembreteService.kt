package com.veredictum.backendveredictum.services

import com.veredictum.backendveredictum.entity.LogEnvioLembrete
import com.veredictum.backendveredictum.repository.LogEnvioLembreteRepository
import org.springframework.stereotype.Service

@Service
class LogEnvioLembreteService(

    private val logEnvioLembreteRepository: LogEnvioLembreteRepository

) {

    fun criarLog(logEnvioLembrete: LogEnvioLembrete): LogEnvioLembrete {
        return logEnvioLembreteRepository.save(logEnvioLembrete)
    }

    fun buscarPorId(id: Int): LogEnvioLembrete? {
        return logEnvioLembreteRepository.findById(id).orElse(null)
    }

    fun listarTodos(): List<LogEnvioLembrete> {
        return logEnvioLembreteRepository.findAll()
    }
}