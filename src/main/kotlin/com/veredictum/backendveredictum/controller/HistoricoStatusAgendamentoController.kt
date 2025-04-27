package com.veredictum.backendveredictum.controller

import com.veredictum.backendveredictum.entity.HistoricoStatusAgendamento
import com.veredictum.backendveredictum.repository.HistoricoStatusAgendamentoRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Histórico de Status de Agendamento", description = "Endpoints para gerenciar histórico de status de agendamentos")
@RestController
@RequestMapping("/historico-status-agendamento")
class HistoricoStatusAgendamentoController(
    val historicoStatusAgendamentoRepository: HistoricoStatusAgendamentoRepository
) {

    @Operation(summary = "Buscar todos os históricos de status de agendamento")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Históricos retornados com sucesso"),
            ApiResponse(responseCode = "204", description = "Nenhum histórico encontrado")
        ]
    )
    @GetMapping
    fun buscarTodos(): ResponseEntity<List<HistoricoStatusAgendamento>> {
        val historicos = historicoStatusAgendamentoRepository.findAll()
        return if (historicos.isEmpty()) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok(historicos)
        }
    }

    @Operation(summary = "Buscar histórico de status de agendamento por ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Histórico encontrado"),
            ApiResponse(responseCode = "404", description = "Histórico não encontrado")
        ]
    )
    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<HistoricoStatusAgendamento> {
        return historicoStatusAgendamentoRepository.findById(id)
            .map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())
    }

    @Operation(summary = "Criar um novo histórico de status de agendamento")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Histórico criado com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados inválidos")
        ]
    )
    @PostMapping
    fun criar(@RequestBody @Valid novoHistorico: HistoricoStatusAgendamento): ResponseEntity<HistoricoStatusAgendamento> {
        val historicoSalvo = historicoStatusAgendamentoRepository.save(novoHistorico)
        return ResponseEntity.status(HttpStatus.CREATED).body(historicoSalvo)
    }

    @Operation(summary = "Atualizar um histórico de status de agendamento existente")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Histórico atualizado com sucesso"),
            ApiResponse(responseCode = "404", description = "Histórico não encontrado")
        ]
    )
    @PutMapping("/{id}")
    fun atualizar(
        @PathVariable id: Int,
        @RequestBody @Valid historicoAtualizado: HistoricoStatusAgendamento
    ): ResponseEntity<HistoricoStatusAgendamento> {
        return historicoStatusAgendamentoRepository.findById(id).map { historicoExistente ->
            historicoAtualizado.idHistoricoAgendamento = id
            val historicoSalvo = historicoStatusAgendamentoRepository.save(historicoAtualizado)
            ResponseEntity.ok(historicoSalvo)
        }.orElse(ResponseEntity.notFound().build())
    }

    @Operation(summary = "Excluir um histórico de status de agendamento por ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Histórico excluído com sucesso"),
            ApiResponse(responseCode = "404", description = "Histórico não encontrado")
        ]
    )
    @DeleteMapping("/{id}")
    fun excluir(@PathVariable id: Int): ResponseEntity<Void> {
        val historicoOptional = historicoStatusAgendamentoRepository.findById(id)
        return if (historicoOptional.isPresent) {
            historicoStatusAgendamentoRepository.delete(historicoOptional.get())
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
