package com.veredictum.backendveredictum.repository

import com.veredictum.backendveredictum.entity.Conta
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Sort

@Repository
interface ContaRepository : JpaRepository<Conta, Int> {
    fun findByUsuario_IdUsuario(idUsuario: Int): List<Conta>
    fun findByIsPago(isPago: Boolean): List<Conta>
    override fun findAll(sort: Sort): List<Conta>
}
