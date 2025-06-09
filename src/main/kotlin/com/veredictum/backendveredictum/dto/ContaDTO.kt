package com.veredictum.backendveredictum.dto

import java.time.LocalDate

data class ContaDTO(
    val idConta: Int?,
    val dataCriacao: LocalDate,
    val etiqueta: String?,
    val valor: Double?,
    val dataVencimento: LocalDate?,
    val urlNuvem: String?,
    val descricao: String?,
    val isPago: Boolean
)
