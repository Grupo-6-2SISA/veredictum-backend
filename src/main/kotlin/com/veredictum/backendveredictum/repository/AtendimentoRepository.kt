package com.veredictum.backendveredictum.repository

import com.veredictum.backendveredictum.entity.Atendimento
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.NativeQuery

interface AtendimentoRepository: JpaRepository<Atendimento, Int> {

    fun findByClienteIdCliente(idCliente: Int): List<Atendimento>

    fun findByUsuarioIdUsuario(idUsuario: Int): List<Atendimento>

    @NativeQuery(
        value = "SELECT * FROM atendimento WHERE YEAR(data_inicio) = ?1 AND MONTH(data_inicio) = ?2 order by data_inicio DESC",
    )
    fun findByDataInicioYearAndDataInicioMonth(ano: Int, mes: Int): List<Atendimento>

}