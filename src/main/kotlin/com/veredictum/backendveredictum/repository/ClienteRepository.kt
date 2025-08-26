package com.veredictum.backendveredictum.repository

import com.veredictum.backendveredictum.entity.Cliente
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ClienteRepository : JpaRepository<Cliente, Int> {

    @Query("SELECT c FROM Cliente c ORDER BY c.isAtivo DESC")
    fun findAllOrderByIsAtivo(): List<Cliente>

    @Query(
        "SELECT c FROM Cliente c " +
                "WHERE FUNCTION('MONTH', c.dataNascimento) = FUNCTION('MONTH', CURRENT_DATE) " +
                "ORDER BY FUNCTION('DAY', c.dataNascimento) ASC"
    )
    fun findClientesAniversariantesDoMes(): List<Cliente>

}