package com.veredictum.backendveredictum.dto

import java.time.LocalDateTime

data class AtendimentoDTO (

    val idAgendamento: Int? = null,

    val fkCliente: Int,  // ID do Cliente

    val fkUsuario: Int,  // ID do Usuário

    val etiqueta: String,

    val valor: Double,

    val descricao: String,

    val dataInicio: LocalDateTime,

    val dataFim: LocalDateTime,

    val dataVencimento: LocalDateTime,

    val isPago: Boolean

)
