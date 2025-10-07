package com.veredictum.backendveredictum.services

import com.veredictum.backendveredictum.entity.Atendimento
import com.veredictum.backendveredictum.entity.HistoricoStatusAgendamento
import com.veredictum.backendveredictum.entity.StatusAgendamento
import com.veredictum.backendveredictum.repository.*
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AtendimentoService(
    private val atendimentoRepository: AtendimentoRepository,
    private val statusAgendamentoRepository: StatusAgendamentoRepository,
    private val historicoStatusAgendamentoRepository: HistoricoStatusAgendamentoRepository,
) {

    fun getAtendimento(id: Int): Atendimento? {
        return atendimentoRepository.findById(id).orElse(null)
    }

    fun getTodos(): List<Atendimento> {
        return atendimentoRepository.findAll()
            .sortedWith(compareBy({ it.isPago }, { it.dataInicio }))
    }

    fun getPorCliente(idCliente: Int): List<Atendimento> {
        return atendimentoRepository.findByClienteIdCliente(idCliente)
            .sortedWith(compareBy({ it.isPago }, { it.dataInicio }))
    }

    fun getPorUsuario(idUsuario: Int): List<Atendimento> {
        return atendimentoRepository.findByUsuarioIdUsuario(idUsuario)
            .sortedWith(compareBy({ it.isPago }, { it.dataInicio }))
    }

    fun getPorStatus(statusId: Int): List<Atendimento> {
        val historicos = historicoStatusAgendamentoRepository.findAll()

        val ultimoStatusPorAtendimento = historicos
            .filter { it.atendimento?.idAtendimento != null && it.statusAgendamento?.idStatusAgendamento != null }
            .groupBy { it.atendimento!!.idAtendimento!! } // safe agora porque filtramos acima
            .mapValues { entry ->
                entry.value.maxByOrNull { it.dataHoraAlteracao ?: LocalDateTime.MIN } // Se data for nula, assume MIN
            }
            .filterValues { it?.statusAgendamento?.idStatusAgendamento == statusId }

        val atendimentos = ultimoStatusPorAtendimento.values
            .mapNotNull { it?.atendimento }
            .sortedBy { it.dataInicio }

        return atendimentos
    }

    fun getAtendimentosOrdenados(): List<Atendimento> {
        return atendimentoRepository.findAll()
            .sortedByDescending { it.dataInicio }  // Ordena os atendimentos pela data de início (do mais recente para o mais distante)
    }


    fun criarAtendimento(atendimento: Atendimento, statusInicialId: Int): Atendimento? {
        val conflito = atendimentoRepository.findByClienteIdCliente(atendimento.cliente.idCliente?: 0)
            .any { it.dataInicio == atendimento.dataInicio }

        if (conflito) {
            return null
        }

        val novoAtendimento = atendimentoRepository.save(atendimento)

        val statusInicial = statusAgendamentoRepository.findById(statusInicialId).orElse(null)

        registrarHistorico(novoAtendimento, statusInicial)

        return novoAtendimento
    }

    fun mudarStatus(idAtendimento: Int, novoStatusId: Int): Boolean {
        val atendimento = atendimentoRepository.findById(idAtendimento).orElse(null) ?: return false
        val novoStatus = statusAgendamentoRepository.findById(novoStatusId).orElse(null) ?: return false

        registrarHistorico(atendimento, novoStatus)

        return true
    }

    fun editarAtendimento(idAtendimento: Int, novoAtendimento: Atendimento): Atendimento? {
        val atendimentoExistente = atendimentoRepository.findById(idAtendimento).orElse(null) ?: return null

        atendimentoExistente.cliente = novoAtendimento.cliente
        atendimentoExistente.usuario = novoAtendimento.usuario
        atendimentoExistente.etiqueta = novoAtendimento.etiqueta
        atendimentoExistente.valor = novoAtendimento.valor
        atendimentoExistente.descricao = novoAtendimento.descricao
        atendimentoExistente.dataInicio = novoAtendimento.dataInicio
        atendimentoExistente.dataFim = novoAtendimento.dataFim
        atendimentoExistente.dataVencimento = novoAtendimento.dataVencimento
        atendimentoExistente.isPago = novoAtendimento.isPago

        return atendimentoRepository.save(atendimentoExistente)
    }

    private fun registrarHistorico(atendimento: Atendimento, statusAgendamento: StatusAgendamento) {

        val historico = HistoricoStatusAgendamento(
            atendimento = atendimento,
            nota = null,
            conta = null,
            statusAgendamento = statusAgendamento,
            dataHoraAlteracao = LocalDateTime.now()
        )
        historicoStatusAgendamentoRepository.save(historico)
    }

    fun excluirAtendimento(id: Int): Boolean {
        val atendimento = atendimentoRepository.findById(id)
        return atendimento.isPresent
    }

    fun getPorMesEAnoOrdenados(ano: Int, mes: Int): List<Atendimento>? {
        return atendimentoRepository.findByDataInicioYearAndDataInicioMonth(ano, mes)
    }

    fun contagemCancelados(mes: Int, ano: Int): Int {
        val statusCancelado = 3

        val historicos = historicoStatusAgendamentoRepository.findAll()

        val ultimoStatusPorAtendimento = historicos
            .filter { it.atendimento?.idAtendimento != null && it.statusAgendamento?.idStatusAgendamento != statusCancelado }
            .groupBy { it.atendimento!!.idAtendimento!! } // safe agora porque filtramos acima
            .mapValues { entry ->
                entry.value.maxByOrNull { it.dataHoraAlteracao ?: LocalDateTime.MIN } // Se data for nula, assume MIN
            }

        val atendimentosCanceladosNoMesEAno = ultimoStatusPorAtendimento.values
            .mapNotNull { it?.atendimento }
            .filter { atendimento ->
                atendimento.dataInicio.year == ano && atendimento.dataInicio.monthValue == mes
            }

        return atendimentosCanceladosNoMesEAno.size
    }

}
