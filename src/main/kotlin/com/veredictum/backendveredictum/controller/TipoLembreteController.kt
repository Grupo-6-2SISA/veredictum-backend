package com.veredictum.backendveredictum.controller

import com.veredictum.backendveredictum.entity.TipoLembrete
import com.veredictum.backendveredictum.repository.TipoLembreteRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Tipo Lembrete", description = "Endpoints para gerenciar tipos de lembrete")
@RestController
@RequestMapping("/tipo-lembrete")
class TipoLembreteController(
    val repository: TipoLembreteRepository
) {

    @Operation(summary = "Buscar todos os tipos de lembrete")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Tipos retornados com sucesso"),
            ApiResponse(responseCode = "204", description = "Nenhum tipo encontrado")
        ]
    )
    @GetMapping
    fun buscarTodos(): ResponseEntity<List<TipoLembrete>> {
        val tipos = repository.findAll()
        return if (tipos.isEmpty()) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok(tipos)
        }
    }

    @Operation(summary = "Buscar tipo de lembrete por ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Tipo encontrado"),
            ApiResponse(responseCode = "404", description = "Tipo não encontrado")
        ]
    )
    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<TipoLembrete> {
        return repository.findById(id)
            .map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())
    }

    @Operation(summary = "Criar um novo tipo de lembrete")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Tipo criado com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados inválidos")
        ]
    )
    @PostMapping
    fun criar(@RequestBody @Valid novoTipo: TipoLembrete): ResponseEntity<TipoLembrete> {
        val tipoSalvo = repository.save(novoTipo)
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoSalvo)
    }

    @Operation(summary = "Atualizar um tipo de lembrete existente")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Tipo atualizado com sucesso"),
            ApiResponse(responseCode = "404", description = "Tipo não encontrado")
        ]
    )
    @PutMapping("/{id}")
    fun atualizar(
        @PathVariable id: Int,
        @RequestBody @Valid tipoAtualizado: TipoLembrete
    ): ResponseEntity<TipoLembrete> {
        return repository.findById(id).map { tipoExistente ->
            tipoAtualizado.idTipoLembrete = id
            val tipoSalvo = repository.save(tipoAtualizado)
            ResponseEntity.ok(tipoSalvo)
        }.orElse(ResponseEntity.notFound().build())
    }

    @Operation(summary = "Excluir um tipo de lembrete por ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Tipo excluído com sucesso"),
            ApiResponse(responseCode = "404", description = "Tipo não encontrado")
        ]
    )
    @DeleteMapping("/{id}")
    fun excluir(@PathVariable id: Int): ResponseEntity<Void> {
        val tipoOptional = repository.findById(id)
        return if (tipoOptional.isPresent) {
            repository.delete(tipoOptional.get())
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}