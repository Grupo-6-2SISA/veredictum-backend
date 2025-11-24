package com.veredictum.backendveredictum.services

import com.veredictum.backendveredictum.dto.ListagemLogsDTO
import com.veredictum.backendveredictum.entity.LogEnvioLembrete
import com.veredictum.backendveredictum.repository.LogEnvioLembreteRepository
import org.springframework.stereotype.Service

@Service
class LogEnvioLembreteService(

    private val logEnvioLembreteRepository: LogEnvioLembreteRepository

) {

    fun criarLog(logEnvioLembrete: LogEnvioLembrete): LogEnvioLembrete {
        return logEnvioLembreteRepository.save(logEnvioLembrete)
    }

    fun buscarPorId(id: Int): LogEnvioLembrete? {
        return logEnvioLembreteRepository.findById(id).orElse(null)
    }

    fun listarTodos(): List<LogEnvioLembrete> {
        return logEnvioLembreteRepository.findAll()
    }

    fun listagemLogs(): List<ListagemLogsDTO> {
        return logEnvioLembreteRepository.listagemLogs()
    }

    fun csvLogs(): ByteArray {
        val dados = logEnvioLembreteRepository.csvLogs()

        if (dados.isEmpty()) return ByteArray(0)

        val csv = buildString {

            appendLine("id;funcionou;id_atendimento;id_cliente;id_conta;id_tipo_lembrete;tipo;mensagem;data_hora_criacao")

            dados.forEach { row ->

                val formatted = row.map { col ->

                    if (col == null) return@map ""

                    var s = col.toString()

                    // Remove quebra de linha interna
                    s = s.replace("\n", " ").replace("\r", " ")

                    // coloca entre aspas se tiver acento ou espaço
                    if (s.any { it.code > 127 } || s.contains(" ")) {
                        s = "\"$s\""
                    }

                    s
                }

                appendLine(formatted.joinToString(";"))
            }
        }

        val bom = byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte())

        return bom + csv.toByteArray(Charsets.UTF_8)
    }



}