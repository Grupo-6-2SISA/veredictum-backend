package com.veredictum.backendveredictum.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "log_envio_lembrete")
data class LogEnvioLembrete(

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    var idLogEnvioLembrete: Int? = null,

    @ManyToOne
    @JoinColumn(name = "fk_tipo_lembrete")
    var tipoLembrete: TipoLembrete,

    @ManyToOne
    @JoinColumn(name = "fk_nota_fiscal")
    var notaFiscal: NotaFiscal,

    @ManyToOne
    @JoinColumn(name = "fk_conta")
    var conta: Conta,

    @ManyToOne
    @JoinColumn(name = "fk_atendimento")
    var atendimento: Atendimento,

    @CreationTimestamp
    var dataHoraCriacao: LocalDateTime = LocalDateTime.now(),

    @field:NotBlank(message = "A mensagem do lembrete é obrigatória")
    var mensagem: String = "",

) {
    constructor() : this(
        null,
        TipoLembrete(),
        NotaFiscal(),
        Conta(),
        Atendimento(),
        LocalDateTime.now(),
        ""
    )
}
