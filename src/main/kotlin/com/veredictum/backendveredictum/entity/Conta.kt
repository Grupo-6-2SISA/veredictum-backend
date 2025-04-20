package com.veredictum.backendveredictum.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.FutureOrPresent
import org.hibernate.annotations.CreationTimestamp
import org.springframework.web.client.RestClientException
import java.time.LocalDate

@Entity
@Table(name = "conta")
data class Conta(

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    var idConta: Int? = null,

    @ManyToOne
    @JoinColumn(name = "fk_usuario")
    var usuario: Usuario? = null,

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    var dataCriacao: LocalDate = LocalDate.now(),

    var etiqueta: String? = null,

    var valor: Double? = null,

    var dataVencimento: LocalDate,

    var descricao: String? = null,

    var isEmitida: Boolean = false

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
