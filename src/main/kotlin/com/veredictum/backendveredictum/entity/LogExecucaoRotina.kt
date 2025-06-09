package com.veredictum.backendveredictum.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "log_execucao_rotina")
data class LogExecucaoRotina(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idLogExecucaoRotina: Int? = null,

    @ManyToOne
    @JoinColumn(name = "fk_rotina")
    var rotina: Rotina? = null,

    @Column(name = "data_hora_ini_execucao")
    var dataHoraIniExecucao: LocalDateTime? = null,

    @Column(name = "data_hora_fim_execucao")
    var dataHoraFimExecucao: LocalDateTime? = null,

    @Column(name = "status_execucao")
    var statusExecucao: String? = null,

    var isBloqueado: Boolean = false,

    var funcionou: Boolean = false,

)

{}
