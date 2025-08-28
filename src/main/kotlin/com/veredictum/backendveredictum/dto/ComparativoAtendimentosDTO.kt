package com.veredictum.backendveredictum.dto

data class ComparativoAtendimentosDTO(
    val ano: Int,
    val meses: List<DadoMensalAtendimentoDTO>
)
