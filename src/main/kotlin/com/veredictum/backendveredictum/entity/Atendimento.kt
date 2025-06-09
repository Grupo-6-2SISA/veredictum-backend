package com.veredictum.backendveredictum.entity

import com.fasterxml.jackson.annotation.JsonProperty
import com.veredictum.backendveredictum.dto.AtendimentoDTO
import jakarta.persistence.*
import jakarta.validation.constraints.*
import java.time.LocalDateTime

@Entity
@Table(name = "atendimento")
data class  Atendimento(

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idAtendimento: Int? = null,

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

    @field:NotNull(message = "O status de pagamento é obrigatório")
    var isPago: Boolean = false,

    var shouldEnviarEmail: Boolean = false,

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
        false
    )

    fun toDTO(): AtendimentoDTO {
        return AtendimentoDTO(
            idAgendamento = this.idAtendimento ?: 0,
            fkCliente = this.cliente.idCliente ?: 0,  // ID do Cliente
            fkUsuario = this.usuario.idUsuario ?: 0,  // ID do Usuário
            etiqueta = this.etiqueta ?: "",
            valor = this.valor,
            descricao = this.descricao ?: "",
            dataInicio = this.dataInicio,
            dataFim = this.dataFim,
            dataVencimento = this.dataVencimento,
            isPago = this.isPago,
            shouldEnviarEmail = this.shouldEnviarEmail,
        )
    }
}
