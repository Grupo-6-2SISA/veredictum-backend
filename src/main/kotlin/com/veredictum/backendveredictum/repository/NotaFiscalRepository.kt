package com.veredictum.backendveredictum.repository

import com.veredictum.backendveredictum.entity.NotaFiscal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NotaFiscalRepository : JpaRepository<NotaFiscal, Int> {
//    fun findByEtiqueta(etiqueta: String): NotaFiscal?
//    fun findByUrlNuvem(urlNuvem: String): NotaFiscal?
//    fun findByIdNotaFiscal(idNotaFiscal: Int): NotaFiscal?

}
