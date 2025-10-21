package com.veredictum.backendveredictum.dto

import java.time.LocalDateTime

interface ListagemLogsDTO {

    val idTipo: Int?
    val tipo: String?
    val dataEnvio: String?
    val mensagem: String?
    val clienteRelacionado: String?

}