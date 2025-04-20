package com.veredictum.backendveredictum.controller

import com.veredictum.backendveredictum.entity.StatusAgendamento
import com.veredictum.backendveredictum.repository.StatusAgendamentoRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Status Agendamento", description = "Endpoints para gerenciar clientes")
@RestController
@RequestMapping("/status-agendamento")
class StatusAgendamentoController(
     val repository: StatusAgendamentoRepository
) {


    @Operation(summary = "Buscar todos os status de agendamento")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Status retornados com sucesso"),
            ApiResponse(responseCode = "204", description = "Nenhum status encontrado")
        ]
    )
    @GetMapping
    fun buscarTodos(): ResponseEntity<List<StatusAgendamento>> {
        val status = repository.findAll()
        return if (status.isEmpty()) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok(status)
        }
    }

    @Operation(summary = "Buscar status de agendamento por ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Status encontrado"),
            ApiResponse(responseCode = "404", description = "Status não encontrado")
        ]
    )
    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<StatusAgendamento> {
        return repository.findById(id)
            .map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())
    }

    @Operation(summary = "Criar um novo status de agendamento")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Status criado com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados inválidos")
        ]
    )
    @PostMapping
    fun criar(@RequestBody @Valid novoStatus: StatusAgendamento): ResponseEntity<StatusAgendamento> {
        val statusSalvo = repository.save(novoStatus)
        return ResponseEntity.status(HttpStatus.CREATED).body(statusSalvo)
    }

    @Operation(summary = "Atualizar um status de agendamento existente")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
            ApiResponse(responseCode = "404", description = "Status não encontrado")
        ]
    )
    @PutMapping("/{id}")
    fun atualizar(
        @PathVariable id: Int,
        @RequestBody @Valid statusAtualizado: StatusAgendamento
    ): ResponseEntity<StatusAgendamento> {
        return repository.findById(id).map { statusExistente ->
            statusAtualizado.idStatusAgendamento = id
            val statusSalvo = repository.save(statusAtualizado)
            ResponseEntity.ok(statusSalvo)
        }.orElse(ResponseEntity.notFound().build())
    }


}