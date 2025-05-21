package com.veredictum.backendveredictum.repository

import com.veredictum.backendveredictum.entity.Conta
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface ContaRepository : JpaRepository<Conta, Int> {

//    fun findByEtiqueta(etiqueta: String): List<Conta>
//    fun findByUsuarioIdUsuario(idUsuario: Int): List<Conta>
//    fun findByDataVencimento(dataVencimento: LocalDate): List<Conta>
//    fun findByIsPago(isPago: Boolean): List<Conta>

}
