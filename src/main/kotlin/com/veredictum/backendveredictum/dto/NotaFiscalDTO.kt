package com.veredictum.backendveredictum.dto

import java.time.LocalDate

data class NotaFiscalDTO(
    val idNotaFiscal: Int?,
    val fkCliente: Int?, // Alterado para fkCliente
    val dataCriacao: LocalDate,
    val etiqueta: String?,
    val valor: Double?,
    val dataVencimento: LocalDate?,
    val descricao: String?,
    val urlNuvem: String?,
    val isEmitida: Boolean
)
