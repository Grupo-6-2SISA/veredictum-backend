package com.veredictum.backendveredictum.entity

import com.fasterxml.jackson.annotation.JsonProperty
import com.veredictum.backendveredictum.dto.LogEnvioLembreteDTO
import jakarta.persistence.Column
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

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "id_log_envio_lembrete")
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

    @ManyToOne
    @JoinColumn(name = "fk_cliente")
    var cliente: Cliente,

    @CreationTimestamp
    @Column(name = "data_hora_criacao")
    var dataHoraCriacao: LocalDateTime = LocalDateTime.now(),

    @Column(name = "mensagem")
    @field:NotBlank(message = "A mensagem do lembrete é obrigatória")
    var mensagem: String = "",

    var funcionou: Boolean = false,

    ) {
    constructor() : this(
        null,
        TipoLembrete(),
        NotaFiscal(),
        Conta(),
        Atendimento(),
        Cliente(),
        LocalDateTime.now(),
        ""
    )

    fun toDto(): LogEnvioLembreteDTO {
        return LogEnvioLembreteDTO(
            idLogEnvioLembrete = this.idLogEnvioLembrete,
            fkTipoLembrete = this.tipoLembrete.idTipoLembrete ?: 0,
            fkNotaFiscal = this.notaFiscal.idNotaFiscal ?: 0,
            fkConta = this.conta.idConta ?: 0,
            fkAtendimento = this.atendimento.idAtendimento ?: 0,
            fkCliente = this.cliente.idCliente,
            dataHoraCriacao = this.dataHoraCriacao,
            mensagem = this.mensagem
        )
    }
}
