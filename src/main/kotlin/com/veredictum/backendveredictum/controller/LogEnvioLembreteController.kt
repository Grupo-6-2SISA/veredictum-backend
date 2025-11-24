package com.veredictum.backendveredictum.controller

import com.veredictum.backendveredictum.dto.ListagemLogsDTO
import com.veredictum.backendveredictum.dto.LogEnvioLembreteDTO
import com.veredictum.backendveredictum.entity.LogEnvioLembrete
import com.veredictum.backendveredictum.repository.LogEnvioLembreteRepository
import com.veredictum.backendveredictum.services.LogEnvioLembreteService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.apache.coyote.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Log Envio Lembrete", description = "Endpoints para gerenciar Logs de Envios de Lembretes")
@RestController
@RequestMapping("/log-envio-lembrete")
class LogEnvioLembreteController(
    val repository: LogEnvioLembreteRepository,
    private val logEnvioLembreteService: LogEnvioLembreteService
) {

    @Operation(summary = "Buscar todos os logs de envio de lembretes")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Logs retornados com sucesso"),
            ApiResponse(responseCode = "204", description = "Nenhum log encontrado")
        ]
    )
    @GetMapping
    fun buscarTodos(): ResponseEntity<List<LogEnvioLembreteDTO>> {
        val logs = repository.findAll().map { it.toDto() }
        return if (logs.isEmpty()) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok(logs)
        }
    }

    @Operation(summary = "Buscar log de envio de lembrete por ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Log encontrado"),
            ApiResponse(responseCode = "404", description = "Log não encontrado")
        ]
    )
    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<LogEnvioLembreteDTO> {
        return repository.findById(id)
            .map { ResponseEntity.ok(it.toDto()) }
            .orElse(ResponseEntity.notFound().build())
    }

    @Operation(summary = "Criar um novo log de envio de lembrete")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Log criado com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados inválidos")
        ]
    )
    @PostMapping
    fun criar(@RequestBody @Valid novoLog: LogEnvioLembrete): ResponseEntity<LogEnvioLembreteDTO> {
        val logSalvo = repository.save(novoLog)
        return ResponseEntity.status(HttpStatus.CREATED).body(logSalvo.toDto())
    }

    @Operation(summary = "Atualizar um log de envio de lembrete existente")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Log atualizado com sucesso"),
            ApiResponse(responseCode = "404", description = "Log não encontrado")
        ]
    )
    @PutMapping("/{id}")
    fun atualizar(
        @PathVariable id: Int,
        @RequestBody @Valid logAtualizado: LogEnvioLembrete
    ): ResponseEntity<LogEnvioLembreteDTO> {
        return repository.findById(id).map { logExistente ->
            logAtualizado.idLogEnvioLembrete = id
            val logSalvo = repository.save(logAtualizado)
            ResponseEntity.ok(logSalvo.toDto())
        }.orElse(ResponseEntity.notFound().build())
    }

    @Operation(summary = "Excluir um log de envio de lembrete por ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Log excluído com sucesso"),
            ApiResponse(responseCode = "404", description = "Log não encontrado")
        ]
    )
    @DeleteMapping("/{id}")
    fun excluir(@PathVariable id: Int): ResponseEntity<Void> {
        val logOptional = repository.findById(id)
        return if (logOptional.isPresent) {
            repository.delete(logOptional.get())
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/listagem-logs")
    fun listagemLogs(): ResponseEntity<List<ListagemLogsDTO>> {
        val resposta = logEnvioLembreteService.listagemLogs()
        return if (resposta.isEmpty()) {
            ResponseEntity.status(404).build()
        } else {
            ResponseEntity(resposta, HttpStatus.OK)
        }
    }

    @GetMapping("/csv-logs", produces = ["text/csv"])
    fun exportarLogsCsv(): ResponseEntity<ByteArray> {
        val csv = logEnvioLembreteService.csvLogs()

        if (csv.isEmpty()) {
            return ResponseEntity.notFound().build()
        }

        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=log_historico_email.csv")
            .body(csv)
    }



}