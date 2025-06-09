package com.veredictum.backendveredictum.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "conta")
data class Conta(

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idConta: Int? = null,

    @Column(name = "data_criacao", updatable = false)
    var dataCriacao: LocalDate = LocalDate.now(),

    var etiqueta: String? = null,

    var valor: Double? = null,

    @Column(name = "data_vencimento")
    var dataVencimento: LocalDate?,

    @Column(name = "url_nuvem")
    var urlNuvem: String? = null,

    var descricao: String? = null,

    @Column(name = "is_pago")
    var isPago: Boolean = false

)

{
    constructor() : this(
        null,
        LocalDate.now(),
        null,
        null,
        null,
        null,
        null,
        false
    )
}