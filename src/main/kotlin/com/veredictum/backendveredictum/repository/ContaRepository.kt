package com.veredictum.backendveredictum.repository

import com.veredictum.backendveredictum.dto.ContasPorAnoDTO
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

    @Query("""
    SELECT 
        :ano AS ano,
        m.mes,
        COALESCE(SUM(nf.VALOR), 0) AS valor
    FROM (
        SELECT 1 AS mes UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION 
        SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION 
        SELECT 11 UNION SELECT 12
    ) m
    LEFT JOIN NOTA_FISCAL nf 
        ON m.mes = MONTH(nf.DATA_VENCIMENTO)
        AND YEAR(nf.DATA_VENCIMENTO) = :ano
    GROUP BY m.mes
    ORDER BY m.mes
""", nativeQuery = true)
    fun relatorioMensal(@Param("ano") ano: Int): List<ContasPorAnoDTO>

}
