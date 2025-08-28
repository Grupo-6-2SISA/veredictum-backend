package com.veredictum.backendveredictum.services

import com.veredictum.backendveredictum.entity.Conta
import com.veredictum.backendveredictum.entity.HistoricoStatusAgendamento
import com.veredictum.backendveredictum.entity.NotaFiscal
import com.veredictum.backendveredictum.entity.StatusAgendamento
import com.veredictum.backendveredictum.repository.HistoricoStatusAgendamentoRepository
import com.veredictum.backendveredictum.repository.NotaFiscalRepository
import com.veredictum.backendveredictum.repository.StatusAgendamentoRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.Optional

@Service
class NotaFiscalService(
    private val notaFiscalRepository: NotaFiscalRepository,
    private val statusAgendamentoRepository: StatusAgendamentoRepository,
    private val historicoStatusAgendamentoRepository: HistoricoStatusAgendamentoRepository,
) {

    fun save(notaFiscal: NotaFiscal): NotaFiscal {
        return notaFiscalRepository.save(notaFiscal)
    }

    fun criarNota(nota: NotaFiscal, statusInicialId: Int): NotaFiscal? {
        val novaNota = notaFiscalRepository.save(nota)

        val statusInicial = statusAgendamentoRepository.findById(statusInicialId).orElse(null) ?: return null

        registrarHistorico(novaNota, statusInicial)

        return novaNota

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

    fun getMaisAtrasadas(pageSize: Int?): List<NotaFiscal> {
        return notaFiscalRepository.findMaisAtrasadasAnoAtual(PageRequest.of(0, pageSize ?: 10))
    }

    fun getMaisRecentes(pageSize: Int?): List<NotaFiscal> {
        return notaFiscalRepository.findMaisRecentesAnoAtual(PageRequest.of(0, pageSize ?: 10))
    }

    fun findByMesEAno(mes: Int, ano: Int): List<NotaFiscal> {
        return notaFiscalRepository.findByAnoAndMes(ano, mes)
    }

    private fun registrarHistorico(nota: NotaFiscal, statusAgendamento: StatusAgendamento) {

        val historico = HistoricoStatusAgendamento(
            atendimento = null,
            nota = nota,
            conta = null,
            statusAgendamento = statusAgendamento,
            dataHoraAlteracao = LocalDateTime.now()
        )
        historicoStatusAgendamentoRepository.save(historico)
    }

    fun getPorStatus(statusId: Int): List<NotaFiscal> {
        val historicos = historicoStatusAgendamentoRepository.findAll()

        val ultimoStatusPorConta = historicos
            .filter { it.nota?.idNotaFiscal != null && it.statusAgendamento?.idStatusAgendamento != null }
            .groupBy { it.nota!!.idNotaFiscal!! } // safe agora porque filtramos acima
            .mapValues { entry ->
                entry.value.maxByOrNull { it.dataHoraAlteracao ?: LocalDateTime.MIN } // Se data for nula, assume MIN
            }
            .filterValues { it?.statusAgendamento?.idStatusAgendamento == statusId }

        val contas = ultimoStatusPorConta.values
            .mapNotNull { it?.nota }
            .sortedBy { it.isEmitida }

        return contas
    }

    fun mudarStatus(idNota: Int, novoStatusId: Int): Boolean {
        val nota = notaFiscalRepository.findById(idNota).orElse(null) ?: return false
        val novoStatus = statusAgendamentoRepository.findById(novoStatusId).orElse(null) ?: return false

        registrarHistorico(nota, novoStatus)

        return true
    }

}
