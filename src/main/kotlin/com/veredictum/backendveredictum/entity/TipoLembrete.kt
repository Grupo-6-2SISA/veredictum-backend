package com.veredictum.backendveredictum.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank

@Entity
@Table(name = "tipo_lembrete")
data class TipoLembrete(

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    var idTipoLembrete: Int? = null,

    @field:NotBlank(message = "O tipo do lembrete é obrigatório")
    var tipo: String = "",

)

{
    constructor() : this(
        null,
        ""
    )
}
