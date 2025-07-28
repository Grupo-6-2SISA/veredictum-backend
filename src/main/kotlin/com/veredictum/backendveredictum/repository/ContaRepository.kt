package com.veredictum.backendveredictum.repository

import com.veredictum.backendveredictum.entity.Conta
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

@Repository
interface ContaRepository : JpaRepository<Conta, Int> {
    fun findByIsPago(isPago: Boolean): List<Conta>

    override fun findAll(sort: Sort): List<Conta>

    @Query("SELECT c FROM Conta c WHERE YEAR(c.dataVencimento) = :ano AND MONTH(c.dataVencimento) = :mes ORDER BY c.isPago ASC")
    fun findByAnoAndMes(@Param("ano") ano: Int, @Param("mes") mes: Int): List<Conta>

    @Query("SELECT SUM(c.valor) FROM Conta c WHERE YEAR(c.dataVencimento) = :ano AND MONTH(c.dataVencimento) = :mes")
    fun sumValorByAnoAndMes(@Param("ano") ano: Int, @Param("mes") mes: Int): Double?
}
