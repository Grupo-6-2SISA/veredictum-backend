package com.veredictum.backendveredictum.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

@Entity
@Table(name = "status_agendamento")
data class StatusAgendamento(

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idStatusAgendamento: Int? = null,

    @Column(name = "descricao", nullable = false, columnDefinition = "varchar(45)")
    var descricao: String

) {
    constructor() : this(
        null,
        ""
    )
}
