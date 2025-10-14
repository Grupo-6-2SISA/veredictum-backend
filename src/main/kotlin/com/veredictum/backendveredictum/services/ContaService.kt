package com.veredictum.backendveredictum.services

import com.veredictum.backendveredictum.dto.ContasPorAnoDTO
import com.veredictum.backendveredictum.entity.*
import com.veredictum.backendveredictum.repository.ContaRepository
import com.veredictum.backendveredictum.repository.HistoricoStatusAgendamentoRepository
import com.veredictum.backendveredictum.repository.StatusAgendamentoRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Service
class ContaService(
    private val contaRepository: ContaRepository,
    private val statusAgendamentoRepository: StatusAgendamentoRepository,
    private val historicoStatusAgendamentoRepository: HistoricoStatusAgendamentoRepository
) {


    fun criarConta(conta: Conta, statusInicialId: Int): Conta? {
        val novaConta = contaRepository.save(conta)

        val statusInicial = statusAgendamentoRepository.findById(statusInicialId).orElse(null) ?: return null

        registrarHistorico(novaConta, statusInicial)

        return novaConta

    }

    fun save(conta: Conta, statusInicialId: Int): Conta {

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
        val statusInicial = statusAgendamentoRepository.findById(statusInicialId).orElse(null)
        registrarHistorico(contaToSave, statusInicial)
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

    fun findByMesEAno(mes: Int, ano: Int): List<Conta> {
        return contaRepository.findByAnoAndMes(ano, mes)
    }

    fun getTotalPorMesEAno(mes: Int, ano: Int): Double {
        return contaRepository.sumValorByAnoAndMes(ano, mes) ?: 0.0
    }

    fun editarConta(id: Int, contaEditada: Conta): Conta? {
        val contaExistente = contaRepository.findById(id).orElse(null) ?: return null

        contaExistente.dataCriacao = contaEditada.dataCriacao
        contaExistente.etiqueta = contaEditada.etiqueta
        contaExistente.valor = contaEditada.valor
        contaExistente.dataVencimento = contaEditada.dataVencimento
        contaExistente.urlNuvem = contaEditada.urlNuvem
        contaExistente.descricao = contaEditada.descricao
        contaExistente.isPago = contaEditada.isPago

        return contaRepository.save(contaExistente)
    }

    private fun registrarHistorico(conta: Conta, statusAgendamento: StatusAgendamento) {

        val historico = HistoricoStatusAgendamento(
            atendimento = null,
            nota = null,
            conta = conta,
            statusAgendamento = statusAgendamento,
            dataHoraAlteracao = LocalDateTime.now()
        )
        historicoStatusAgendamentoRepository.save(historico)
    }

    fun getMaisAtrasadas(pageSize: Int?): List<Conta> {
        return contaRepository.findMaisAtrasadasAnoAtual(PageRequest.of(0, pageSize?: 10))
    }

    fun getMaisRecentes(pageSize: Int?): List<Conta> {
        return contaRepository.findMaisRecentesAnoAtual(PageRequest.of(0, pageSize?: 10))
    }

    fun getPorStatus(statusId: Int): List<Conta> {
        val historicos = historicoStatusAgendamentoRepository.findAll()

        val ultimoStatusPorConta = historicos
            .filter { it.conta?.idConta != null && it.statusAgendamento?.idStatusAgendamento != null }
            .groupBy { it.conta!!.idConta!! } // safe agora porque filtramos acima
            .mapValues { entry ->
                entry.value.maxByOrNull { it.dataHoraAlteracao ?: LocalDateTime.MIN } // Se data for nula, assume MIN
            }
            .filterValues { it?.statusAgendamento?.idStatusAgendamento == statusId }

        val contas = ultimoStatusPorConta.values
            .mapNotNull { it?.conta }
            .sortedBy { it.isPago }

        return contas
    }

    fun mudarStatus(idConta: Int, novoStatusId: Int): Boolean {
        val conta = contaRepository.findById(idConta).orElse(null) ?: return false
        val novoStatus = statusAgendamentoRepository.findById(novoStatusId).orElse(null) ?: return false

        registrarHistorico(conta, novoStatus)

        return true
    }

    fun contagemAtrasadas(mes: Int, ano: Int): Int {
        return historicoStatusAgendamentoRepository.findAll()
            .filter { it.conta?.dataVencimento?.monthValue == mes && it.conta?.dataVencimento?.year == ano && it.conta?.isPago == false }
            .count()
    }

    fun atrasadas(mes: Int, ano: Int): List<HistoricoStatusAgendamento> {
        return historicoStatusAgendamentoRepository.findAll()
            .filter { it.conta?.dataVencimento?.monthValue == mes && it.conta?.dataVencimento?.year == ano && it.conta?.isPago == false }
    }

    fun relatorioMensal(ano: Int): List<ContasPorAnoDTO> {
        return contaRepository.relatorioMensal(ano)
    }

}