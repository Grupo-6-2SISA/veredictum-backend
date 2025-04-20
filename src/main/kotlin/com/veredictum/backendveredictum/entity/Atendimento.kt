package com.veredictum.backendveredictum.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.*
import org.hibernate.validator.constraints.URL
import java.time.LocalDateTime

@Entity
@Table(name = "atendimento")
data class Atendimento(

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idAgendamento: Int? = null,

    @ManyToOne
    @JoinColumn(name = "fk_cliente")
    var cliente: Cliente,

    @ManyToOne
    @JoinColumn(name = "fk_usuario")
    var usuario: Usuario,

    @field:NotBlank(message = "A etiqueta é obrigatória")
    @field:Size(max = 30, message = "A etiqueta não pode ter mais que 30 caracteres")
    var etiqueta: String,

    @field:NotNull(message = "O valor é obrigatório")
    @field:DecimalMin(value = "0.0", inclusive = false, message = "O valor deve ser maior que 0")
    var valor: Double,

    @field:NotBlank(message = "A descrição é obrigatória")
    @field:Size(max = 255, message = "A descrição não pode ter mais que 255 caracteres")
    var descricao: String,

    var dataInicio: LocalDateTime,

    var dataFim: LocalDateTime,

    var dataVencimento: LocalDateTime,

    @field:URL(message = "A URL da nuvem deve ser válida (exemplo: https://exemplo.com).")
    var urlNuvem: String,

    @field:NotNull(message = "O status de pagamento é obrigatório")
    var isPago: Boolean = false,

    ) {
    constructor() : this(
        null,
        Cliente(),
        Usuario(),
        "",
        0.0,
        "",
        LocalDateTime.now(),
        LocalDateTime.now(),
        LocalDateTime.now(),
        "",
        false
    )
}
