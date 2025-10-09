package com.veredictum.backendveredictum.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "historico_status_agendamento")
data class HistoricoStatusAgendamento(

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idHistoricoAgendamento: Int? = null,

    @ManyToOne
    @JoinColumn(name = "fk_atendimento")
    @org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    var atendimento: Atendimento?,

    @ManyToOne
    @JoinColumn(name = "fk_nota_fiscal")
    @org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    var nota: NotaFiscal?,

    @ManyToOne
    @JoinColumn(name = "fk_conta")
    @org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    var conta: Conta?,

    @ManyToOne
    @field:NotNull(message = "O status do agendamento é obrigatório")
    @JoinColumn(name = "fk_status_agendamento")
    var statusAgendamento: StatusAgendamento,

    @CreationTimestamp
    @Column(name = "data_hora_alteracao", updatable = false)
    var dataHoraAlteracao: LocalDateTime? = null

) {
    constructor() : this(
        null,
        null,
        null,
        null,
        StatusAgendamento(),
        LocalDateTime.now()
    )
}
