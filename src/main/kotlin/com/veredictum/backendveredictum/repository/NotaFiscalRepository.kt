package com.veredictum.backendveredictum.repository

import com.veredictum.backendveredictum.dto.ContasPorAnoDTO
import com.veredictum.backendveredictum.entity.Conta
import com.veredictum.backendveredictum.entity.NotaFiscal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

@Repository
interface NotaFiscalRepository : JpaRepository<NotaFiscal, Int> {
//    fun findByEtiqueta(etiqueta: String): NotaFiscal?
//    fun findByUrlNuvem(urlNuvem: String): NotaFiscal?
//    fun findByIdNotaFiscal(idNotaFiscal: Int): NotaFiscal?

    @Query("SELECT n FROM NotaFiscal n WHERE n.isEmitida = false AND n.dataVencimento < CURRENT_DATE ORDER BY n.dataVencimento ASC")
    fun findMaisAtrasadasAnoAtual(pageable: Pageable): List<NotaFiscal>

    @Query(
        "SELECT n FROM NotaFiscal n " +
                "WHERE FUNCTION('YEAR', n.dataVencimento) = FUNCTION('YEAR', CURRENT_DATE) " +
                "ORDER BY n.dataVencimento DESC"
    )
    fun findMaisRecentesAnoAtual(pageable: Pageable): List<NotaFiscal>

    @Query("SELECT n FROM NotaFiscal n WHERE YEAR(n.dataVencimento) = :ano AND MONTH(n.dataVencimento) = :mes ORDER BY n.isEmitida ASC")
    fun findByAnoAndMes(@Param("ano") ano: Int, @Param("mes") mes: Int): List<NotaFiscal>

    @Query("""
        SELECT 
    anos.ano,
    meses.mes,
    COUNT(c.id_nota_fiscal) AS valor
FROM 
    (SELECT :anoAnterior AS ano UNION SELECT :anoSelecionado) anos
CROSS JOIN 
    (
        SELECT 1 AS mes UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION
        SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION
        SELECT 11 UNION SELECT 12
    ) meses
LEFT JOIN nota_fiscal c
    ON c.is_emitida = 0
    AND c.data_vencimento < STR_TO_DATE(CONCAT(anos.ano,'-',meses.mes,'-01'), '%Y-%m-%d')
GROUP BY 
    anos.ano,
    meses.mes
ORDER BY 
    anos.ano,
    meses.mes;
    """, nativeQuery = true)
    fun graficoPendentes(@Param("anoSelecionado") anoSelecionado: Int, @Param("anoAnterior") anoAnterior: Int): List<ContasPorAnoDTO>

    @Query("""
        SELECT 
    anos.ano,
    meses.mes,
    COUNT(c.id_nota_fiscal) AS valor
FROM 
    (SELECT :anoAnterior AS ano UNION SELECT :anoSelecionado) anos
CROSS JOIN 
    (
        SELECT 1 AS mes UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION
        SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION
        SELECT 11 UNION SELECT 12
    ) meses
LEFT JOIN nota_fiscal c
    ON c.is_emitida = 1
    AND c.data_vencimento < STR_TO_DATE(CONCAT(anos.ano,'-',meses.mes,'-01'), '%Y-%m-%d')
GROUP BY 
    anos.ano,
    meses.mes
ORDER BY 
    anos.ano,
    meses.mes;
    """, nativeQuery = true)
    fun graficoEmitidas(@Param("anoSelecionado") anoSelecionado: Int, @Param("anoAnterior") anoAnterior: Int): List<ContasPorAnoDTO>

    @Query("""
        SELECT count(*)
        FROM nota_fiscal
        WHERE is_emitida = 0
          AND MONTH(data_vencimento) = :mes
          AND YEAR(data_vencimento) = :ano
    """, nativeQuery = true)
    fun countNaoEmitidas(@Param("mes") mes: Int, @Param("ano") ano: Int): Int

    @Query("""
        SELECT count(*)
        FROM nota_fiscal
        WHERE is_emitida = 1
          AND MONTH(data_vencimento) = :mes
          AND YEAR(data_vencimento) = :ano
    """, nativeQuery = true)
    fun countEmitidas(@Param("mes") mes: Int, @Param("ano") ano: Int): Int

    @Query("""
        SELECT count(*)
        FROM nota_fiscal
        WHERE MONTH(data_vencimento) = :mes
          AND YEAR(data_vencimento) = :ano
    """, nativeQuery = true)
    fun countTotalPorMesEAno(@Param("mes") mes: Int, @Param("ano") ano: Int): Int

}
