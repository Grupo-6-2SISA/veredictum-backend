package com.veredictum.backendveredictum.controller

import com.veredictum.backendveredictum.entity.LogExecucaoRotina
import com.veredictum.backendveredictum.enums.TipoStatusLogRotina
import com.veredictum.backendveredictum.repository.LogExecucaoRotinaRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Log Execução Rotina", description = "Endpoints para gerenciar logs de execução de rotinas")
@RestController
@RequestMapping("/log-execucao-rotina")
class LogExecucaoRotinaController(private val logExecucaoRotinaRepository: LogExecucaoRotinaRepository) {

    @Operation(summary = "Listar todos os logs de execução de rotina")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Logs retornados com sucesso"),
            ApiResponse(responseCode = "204", description = "Nenhum log encontrado")
        ]
    )
    @GetMapping
    fun listarTodos(): ResponseEntity<List<LogExecucaoRotina>> {
        val logs = logExecucaoRotinaRepository.findAll()
        return if (logs.isEmpty()) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok(logs)
        }
    }

    @Operation(summary = "Buscar log de execução de rotina por ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Log encontrado"),
            ApiResponse(responseCode = "404", description = "Log não encontrado")
        ]
    )
    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<LogExecucaoRotina> {
        val log = logExecucaoRotinaRepository.findById(id)
        return if (log.isPresent) {
            ResponseEntity.ok(log.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @Operation(summary = "Criar um novo log de execução de rotina")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Log criado com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados inválidos")
        ]
    )
    @PostMapping
    fun criar(@RequestBody logExecucaoRotina: LogExecucaoRotina): ResponseEntity<LogExecucaoRotina> {
        val novoLog = logExecucaoRotinaRepository.save(logExecucaoRotina)
        return ResponseEntity.status(201).body(novoLog)
    }

    @Operation(summary = "Atualizar status do log para 'Em Andamento'")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
            ApiResponse(responseCode = "404", description = "Log não encontrado")
        ]
    )
    @PatchMapping("/{id}/status/em-andamento")
    fun atualizarParaEmAndamento(@PathVariable id: Int): ResponseEntity<LogExecucaoRotina> {
        return atualizarStatus(id, TipoStatusLogRotina.EM_ANDAMENTO)
    }

    @Operation(summary = "Atualizar status do log para 'Concluída'")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
            ApiResponse(responseCode = "404", description = "Log não encontrado")
        ]
    )
    @PatchMapping("/{id}/status/concluida")
    fun atualizarParaConcluida(@PathVariable id: Int): ResponseEntity<LogExecucaoRotina> {
        return atualizarStatus(id, TipoStatusLogRotina.CONCLUIDA)
    }

    @Operation(summary = "Atualizar status do log para 'Cancelada'")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
            ApiResponse(responseCode = "404", description = "Log não encontrado")
        ]
    )
    @PatchMapping("/{id}/status/cancelada")
    fun atualizarParaCancelada(@PathVariable id: Int): ResponseEntity<LogExecucaoRotina> {
        return atualizarStatus(id, TipoStatusLogRotina.CANCELADA)
    }

    @Operation(summary = "Excluir um log de execução de rotina por ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Log excluído com sucesso"),
            ApiResponse(responseCode = "404", description = "Log não encontrado")
        ]
    )
    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Int): ResponseEntity<Void> {
        return if (logExecucaoRotinaRepository.existsById(id)) {
            logExecucaoRotinaRepository.deleteById(id)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    private fun atualizarStatus(id: Int, status: TipoStatusLogRotina): ResponseEntity<LogExecucaoRotina> {
        return logExecucaoRotinaRepository.findById(id).map { log ->
            log.statusExecucao = status.tipo
            val logAtualizado = logExecucaoRotinaRepository.save(log)
            ResponseEntity.ok(logAtualizado)
        }.orElse(ResponseEntity.notFound().build())
    }
}