package com.veredictum.backendveredictum.dto

import java.math.BigDecimal

interface ContasPorAnoDTO {
    val ano: Int
    val mes: Int
    val valor: BigDecimal
}