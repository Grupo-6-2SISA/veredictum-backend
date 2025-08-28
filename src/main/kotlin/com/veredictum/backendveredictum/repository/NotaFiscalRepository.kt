package com.veredictum.backendveredictum.repository

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

    @Query("SELECT n FROM NotaFiscal n WHERE n.isEmitida = false ORDER BY n.dataVencimento ASC")
    fun findMaisAtrasadasAnoAtual(pageable: Pageable): List<NotaFiscal>

    @Query(
        "SELECT n FROM NotaFiscal n " +
                "WHERE FUNCTION('YEAR', n.dataVencimento) = FUNCTION('YEAR', CURRENT_DATE) " +
                "ORDER BY n.dataVencimento DESC"
    )
    fun findMaisRecentesAnoAtual(pageable: Pageable): List<NotaFiscal>

    @Query("SELECT n FROM NotaFiscal n WHERE YEAR(n.dataVencimento) = :ano AND MONTH(n.dataVencimento) = :mes ORDER BY n.isEmitida ASC")
    fun findByAnoAndMes(@Param("ano") ano: Int, @Param("mes") mes: Int): List<NotaFiscal>

}
