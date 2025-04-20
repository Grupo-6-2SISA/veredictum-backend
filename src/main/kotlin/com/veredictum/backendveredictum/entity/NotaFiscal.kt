package com.veredictum.backendveredictum.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.FutureOrPresent
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDate

@Entity
@Table(name = "nota_fiscal")
data class NotaFiscal(

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    var idNotaFiscal: Int? = null,

    @JoinColumn(name = "fk_cliente")
    @ManyToOne
    var cliente: Cliente? = null,

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    var dataCriacao: LocalDate = LocalDate.now(),

    var etiqueta: String? = null,

    var valor: Double? = null,

    var dataVencimento: LocalDate,

    var descricao: String? = null,

    var isPago: Boolean = false



    ) {
    constructor() : this(
        null,
        null,
        LocalDate.now(),
        "",
        0.0,
        LocalDate.now(),
        "",
        false
    )
}
