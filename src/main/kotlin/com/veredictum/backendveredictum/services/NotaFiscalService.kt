package com.veredictum.backendveredictum.services

import com.veredictum.backendveredictum.entity.NotaFiscal
import com.veredictum.backendveredictum.repository.NotaFiscalRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class NotaFiscalService(
    private val notaFiscalRepository: NotaFiscalRepository
) {

    fun save(notaFiscal: NotaFiscal): NotaFiscal {
        return notaFiscalRepository.save(notaFiscal)
    }

    fun findById(id: Int): Optional<NotaFiscal> {
        return notaFiscalRepository.findById(id)
    }

    fun findAll(): List<NotaFiscal> {
        return notaFiscalRepository.findAll()
    }


    fun existsById(id: Int): Boolean {
        return notaFiscalRepository.existsById(id)
    }

    fun deleteById(id: Int) {
        notaFiscalRepository.deleteById(id)
    }

    fun getMaisAtrasadas(): List<NotaFiscal> {
        return notaFiscalRepository.findMaisAtrasadasAnoAtual(PageRequest.of(0, 10))
    }

    fun getMaisRecentes(): List<NotaFiscal> {
        return notaFiscalRepository.findMaisRecentesAnoAtual(PageRequest.of(0, 10))
    }
}
