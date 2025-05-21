package com.veredictum.backendveredictum.dto

import java.time.LocalDateTime

data class LogEnvioLembreteDTO(
    val idLogEnvioLembrete: Int?,
    val fkTipoLembrete: Int,
    val fkNotaFiscal: Int,
    val fkConta: Int,
    val fkAtendimento: Int,
    val fkCliente: Int?,
    val dataHoraCriacao: LocalDateTime,
    val mensagem: String
)
