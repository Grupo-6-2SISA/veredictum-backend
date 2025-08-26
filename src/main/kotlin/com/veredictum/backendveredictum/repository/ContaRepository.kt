package com.veredictum.backendveredictum.repository

import com.veredictum.backendveredictum.entity.Conta
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.domain.Pageable

@Repository
interface ContaRepository : JpaRepository<Conta, Int> {
    fun findByIsPago(isPago: Boolean): List<Conta>

    override fun findAll(sort: Sort): List<Conta>

    @Query("SELECT c FROM Conta c WHERE YEAR(c.dataVencimento) = :ano AND MONTH(c.dataVencimento) = :mes ORDER BY c.isPago ASC")
    fun findByAnoAndMes(@Param("ano") ano: Int, @Param("mes") mes: Int): List<Conta>

    @Query("SELECT SUM(c.valor) FROM Conta c WHERE YEAR(c.dataVencimento) = :ano AND MONTH(c.dataVencimento) = :mes")
    fun sumValorByAnoAndMes(@Param("ano") ano: Int, @Param("mes") mes: Int): Double?

    @Query(
        "SELECT c FROM Conta c " +
                "WHERE FUNCTION('YEAR', c.dataVencimento) = FUNCTION('YEAR', CURRENT_DATE) " +
                "AND c.isPago = false " +
                "ORDER BY c.dataVencimento ASC"
    )
    fun findMaisAtrasadasAnoAtual(pageable: Pageable): List<Conta>

    @Query(
        "SELECT c FROM Conta c " +
                "WHERE FUNCTION('YEAR', c.dataVencimento) = FUNCTION('YEAR', CURRENT_DATE) " +
                "ORDER BY c.dataVencimento DESC"
    )
    fun findMaisRecentesAnoAtual(pageable: Pageable): List<Conta>

}
