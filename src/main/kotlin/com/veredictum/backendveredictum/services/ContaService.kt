package com.veredictum.backendveredictum.services

import com.veredictum.backendveredictum.entity.Conta
import com.veredictum.backendveredictum.entity.Usuario
import com.veredictum.backendveredictum.repository.ContaRepository
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.util.*

@Service
class ContaService(
    private val contaRepository: ContaRepository,
) {


    fun save(conta: Conta): Conta {


        val contaToSave: Conta
        val contaId = conta.idConta

        if (contaId != null && contaId != 0) {
            val existingConta = contaRepository.findById(contaId).orElseThrow {
                ResponseStatusException(HttpStatus.NOT_FOUND, "Conta com ID $contaId não encontrada para atualização")
            }
            contaToSave = existingConta.copy(
                etiqueta = conta.etiqueta,
                valor = conta.valor,
                dataVencimento = conta.dataVencimento,
                urlNuvem = conta.urlNuvem,
                descricao = conta.descricao,
                isPago = conta.isPago
            )
        } else {
            contaToSave = Conta(
                dataCriacao = LocalDate.now(),
                etiqueta = conta.etiqueta,
                valor = conta.valor,
                dataVencimento = conta.dataVencimento,
                urlNuvem = conta.urlNuvem,
                descricao = conta.descricao,
                isPago = conta.isPago
            )
        }
        return contaRepository.save(contaToSave)
    }


    fun findById(id: Int): Optional<Conta> {
        return contaRepository.findById(id)
    }


    fun findAll(): List<Conta> {
        return contaRepository.findAll()
    }


    fun findAll(sort: Sort): List<Conta> {
        return contaRepository.findAll(sort)
    }


    fun findByIsPago(isPago: Boolean): List<Conta> {
        return contaRepository.findByIsPago(isPago)
    }

    fun existsById(id: Int): Boolean {
        return contaRepository.existsById(id)
    }


    fun deleteById(id: Int) {
        contaRepository.deleteById(id)
    }


    fun partialUpdate(id: Int, updates: Map<String, Any>): Conta {
        val conta = contaRepository.findById(id)
            .orElseThrow { NoSuchElementException("Conta com ID $id não encontrada") }

        updates.forEach { (key, value) ->
            when (key) {
                "etiqueta" -> conta.etiqueta = value as? String
                "valor" -> conta.valor = (value as? Number)?.toDouble()
                "dataVencimento" -> conta.dataVencimento = value as? LocalDate
                "urlNuvem" -> conta.urlNuvem = value as? String
                "descricao" -> conta.descricao = value as? String
                "isPago" -> conta.isPago = value as? Boolean ?: conta.isPago
                // dataCriacao não deve ser atualizado
                else -> {} // Ignora outras propriedades
            }
        }
        return contaRepository.save(conta)
    }
}
