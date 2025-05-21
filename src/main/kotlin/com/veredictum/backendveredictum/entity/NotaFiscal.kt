package com.veredictum.backendveredictum.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "nota_fiscal")
data class NotaFiscal(

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idNotaFiscal: Int? = null,

    @ManyToOne
    @JoinColumn(name = "fk_cliente")
    var cliente: Cliente? = null,

    @Column(name = "data_criacao", updatable = false)
    var dataCriacao: LocalDate = LocalDate.now(),

    var etiqueta: String? = null,

    var valor: Double? = null,

    @Column(name = "data_vencimento")
    var dataVencimento: LocalDate?,

    var descricao: String? = null,

    @Column(name = "url_nuvem")
    var urlNuvem: String? = null,

    @Column(name = "is_emitida")
    var isEmitida: Boolean = false
)
{
    constructor() : this(
        null,
        Cliente(),
        LocalDate.now(),
        null,
        null,
        null,
        null,
        null,
        false
    )
}