package com.veredictum.backendveredictum.repository

import com.veredictum.backendveredictum.dto.ContasPorAnoDTO
import com.veredictum.backendveredictum.dto.VisaoGeralAtendimentoDTO
import com.veredictum.backendveredictum.entity.Atendimento
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.NativeQuery
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface AtendimentoRepository: JpaRepository<Atendimento, Int> {

    fun findByClienteIdCliente(idCliente: Int): List<Atendimento>

    fun findByUsuarioIdUsuario(idUsuario: Int): List<Atendimento>

    @NativeQuery(
        value = "SELECT * FROM atendimento WHERE YEAR(data_inicio) = ?1 AND MONTH(data_inicio) = ?2 order by data_inicio DESC",
    )
    fun findByDataInicioYearAndDataInicioMonth(ano: Int, mes: Int): List<Atendimento>

    @NativeQuery(
        value="""
    SELECT 
	B.NOME, 
    A.DATA_INICIO
	FROM ATENDIMENTO A
    JOIN CLIENTE B
    WHERE A.FK_CLIENTE = B.ID_CLIENTE 
    ORDER BY A.DATA_INICIO DESC 
    LIMIT 10;
        """
    )
    fun visaoGeral(): List<VisaoGeralAtendimentoDTO>

    @Query("""
        SELECT 
    anos.ano,
    meses.mes,
    COUNT(c.id_atendimento) AS valor
FROM 
    (SELECT :anoAnterior AS ano UNION SELECT :anoSelecionado) anos
CROSS JOIN 
    (
        SELECT 1 AS mes UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION
        SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION
        SELECT 11 UNION SELECT 12
    ) meses
LEFT JOIN atendimento c
    ON c.is_pago = 0
    AND c.data_vencimento < STR_TO_DATE(CONCAT(anos.ano,'-',meses.mes,'-01'), '%Y-%m-%d')
GROUP BY 
    anos.ano,
    meses.mes
ORDER BY 
    anos.ano,
    meses.mes;
    """, nativeQuery = true)
    fun graficoAtrasados(@Param("anoSelecionado") anoSelecionado: Int, @Param("anoAnterior") anoAnterior: Int): List<ContasPorAnoDTO>

    @Query("""
        SELECT 
    anos.ano,
    meses.mes,
    COUNT(c.id_atendimento) AS valor
FROM 
    (SELECT :anoAnterior AS ano UNION SELECT :anoSelecionado) anos
CROSS JOIN 
    (
        SELECT 1 AS mes UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION
        SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION
        SELECT 11 UNION SELECT 12
    ) meses
LEFT JOIN atendimento c
    ON c.is_pago = 1
    AND c.data_vencimento < STR_TO_DATE(CONCAT(anos.ano,'-',meses.mes,'-01'), '%Y-%m-%d')
GROUP BY 
    anos.ano,
    meses.mes
ORDER BY 
    anos.ano,
    meses.mes;
    """, nativeQuery = true)
    fun graficoConcluidos(@Param("anoSelecionado") anoSelecionado: Int, @Param("anoAnterior") anoAnterior: Int): List<ContasPorAnoDTO>


@Query("""
    SELECT count(*)
    FROM atendimento
    WHERE is_pago = 1
    AND MONTH(data_vencimento) = :mes
    AND YEAR(data_vencimento) = :ano
""", nativeQuery = true)
fun countConcluidos(@Param("mes") mes: Int, @Param("ano") ano: Int): Int

@Query("""
    SELECT count(*)
    FROM atendimento
    WHERE MONTH(data_vencimento) = :mes
    AND YEAR(data_vencimento) = :ano
""", nativeQuery = true)
fun countAtendimentosPorMesEAno(@Param("mes") mes: Int, @Param("ano") ano: Int): Int

@Query("""
        SELECT count(*)
        FROM atendimento
        WHERE is_pago = 0
        AND MONTH(data_vencimento) = :mes
        AND YEAR(data_vencimento) = :ano
    """, nativeQuery = true)
    fun countNaoConcluidos(@Param("mes") mes: Int, @Param("ano") ano: Int): Int

}