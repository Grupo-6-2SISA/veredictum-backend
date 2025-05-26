package com.veredictum.backendveredictum.controller

import com.veredictum.backendveredictum.entity.LogExecucaoRotina
import com.veredictum.backendveredictum.enums.TipoStatusLogRotina
import com.veredictum.backendveredictum.repository.LogExecucaoRotinaRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/log-execucao-rotina")
class LogExecucaoRotinaController(private val logExecucaoRotinaRepository: LogExecucaoRotinaRepository) {

    @GetMapping
    fun listarTodos(): ResponseEntity<List<LogExecucaoRotina>> {
        val logs = logExecucaoRotinaRepository.findAll()
        return ResponseEntity.ok(logs)
    }

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<LogExecucaoRotina> {
        val log = logExecucaoRotinaRepository.findById(id)
        return if (log.isPresent) {
            ResponseEntity.ok(log.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun criar(@RequestBody logExecucaoRotina: LogExecucaoRotina): ResponseEntity<LogExecucaoRotina> {
        val novoLog = logExecucaoRotinaRepository.save(logExecucaoRotina)
        return ResponseEntity.status(201).body(novoLog)
    }

    @PatchMapping("/{id}/status/em-andamento")
    fun atualizarParaEmAndamento(@PathVariable id: Int): ResponseEntity<LogExecucaoRotina> {
        return atualizarStatus(id, TipoStatusLogRotina.EM_ANDAMENTO)
    }

    @PatchMapping("/{id}/status/concluida")
    fun atualizarParaConcluida(@PathVariable id: Int): ResponseEntity<LogExecucaoRotina> {
        return atualizarStatus(id, TipoStatusLogRotina.CONCLUIDA)
    }

    @PatchMapping("/{id}/status/cancelada")
    fun atualizarParaCancelada(@PathVariable id: Int): ResponseEntity<LogExecucaoRotina> {
        return atualizarStatus(id, TipoStatusLogRotina.CANCELADA)
    }

    private fun atualizarStatus(id: Int, status: TipoStatusLogRotina): ResponseEntity<LogExecucaoRotina> {
        return logExecucaoRotinaRepository.findById(id).map { log ->
            log.statusExecucao = status.tipo
            val logAtualizado = logExecucaoRotinaRepository.save(log)
            ResponseEntity.ok(logAtualizado)
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Int): ResponseEntity<Void> {
        return if (logExecucaoRotinaRepository.existsById(id)) {
            logExecucaoRotinaRepository.deleteById(id)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}