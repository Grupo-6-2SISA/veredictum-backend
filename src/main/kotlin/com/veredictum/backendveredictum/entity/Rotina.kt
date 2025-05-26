package com.veredictum.backendveredictum.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "rotina")
data class Rotina(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idRotina : Int? = null,

    @Column(name = "nome_rotina")
    var nomeRotina: String = "",

    @Column(name = "data_inicio")
    var dataInicio: LocalDate = LocalDate.now(),

    @Column(name = "data_hora")
    var dataFim: LocalDate = LocalDate.now(),

    @Column(name = "rotina_chamada")
    var rotinaChamada: String = "",

    var isAtivo: Boolean = false

)

{}
