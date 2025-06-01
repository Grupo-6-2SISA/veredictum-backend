package com.veredictum.backendveredictum.entity

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalTime

@Entity
@Table(name = "rotina")
data class Rotina(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idRotina : Int? = null,

    @Column(name = "nome_rotina")
    var nomeRotina: String = "",

    @Column(name = "hora_inicio")
    @JsonFormat(pattern = "HH:mm:ss")
    var horaInicio: LocalTime = LocalTime.of(9, 0),

    @Column(name = "data_inicio")
    var dataInicio: LocalDate = LocalDate.now(),

    @Column(name = "data_fim")
    var dataFim: LocalDate = LocalDate.now(),

    @Column(name = "rotina_chamada")
    var rotinaChamada: String = "",

    var isAtivo: Boolean = false

)

{}
