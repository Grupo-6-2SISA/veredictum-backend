package com.veredictum.backendveredictum.controller

import com.veredictum.backendveredictum.entity.Rotina
import com.veredictum.backendveredictum.repository.RotinaRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Rotina", description = "Endpoints para gerenciar rotinas")
@RestController
@RequestMapping("/rotinas")
class RotinaController(private val rotinaRepository: RotinaRepository) {

    @Operation(summary = "Listar todas as rotinas")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Rotinas retornadas com sucesso"),
            ApiResponse(responseCode = "204", description = "Nenhuma rotina encontrada")
        ]
    )
    @GetMapping
    fun listarTodas(): ResponseEntity<List<Rotina>> {
        val rotinas = rotinaRepository.findAll()
        return if (rotinas.isEmpty()) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok(rotinas)
        }
    }

    @Operation(summary = "Buscar rotina por ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Rotina encontrada"),
            ApiResponse(responseCode = "404", description = "Rotina não encontrada")
        ]
    )
    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<Rotina> {
        val rotina = rotinaRepository.findById(id)
        return if (rotina.isPresent) {
            ResponseEntity.ok(rotina.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @Operation(summary = "Criar uma nova rotina")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Rotina criada com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados inválidos")
        ]
    )
    @PostMapping
    fun criar(@RequestBody rotina: Rotina): ResponseEntity<Rotina> {
        val novaRotina = rotinaRepository.save(rotina)
        return ResponseEntity.status(201).body(novaRotina)
    }

    @Operation(summary = "Atualizar uma rotina existente")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Rotina atualizada com sucesso"),
            ApiResponse(responseCode = "404", description = "Rotina não encontrada")
        ]
    )
    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Int, @RequestBody rotinaAtualizada: Rotina): ResponseEntity<Rotina> {
        return rotinaRepository.findById(id).map { rotinaExistente ->
            rotinaAtualizada.idRotina = rotinaExistente.idRotina
            val rotinaSalva = rotinaRepository.save(rotinaAtualizada)
            ResponseEntity.ok(rotinaSalva)
        }.orElse(ResponseEntity.notFound().build())
    }

    @Operation(summary = "Excluir uma rotina por ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Rotina excluída com sucesso"),
            ApiResponse(responseCode = "404", description = "Rotina não encontrada")
        ]
    )
    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Int): ResponseEntity<Void> {
        return if (rotinaRepository.existsById(id)) {
            rotinaRepository.deleteById(id)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @Operation(summary = "Ativar uma rotina")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Rotina ativada com sucesso"),
            ApiResponse(responseCode = "404", description = "Rotina não encontrada")
        ]
    )
    @PatchMapping("/{id}/ativar")
    fun ativar(@PathVariable id: Int): ResponseEntity<Rotina> {
        return rotinaRepository.findById(id).map { rotina ->
            rotina.isAtivo = true
            val rotinaAtualizada = rotinaRepository.save(rotina)
            ResponseEntity.ok(rotinaAtualizada)
        }.orElse(ResponseEntity.notFound().build())
    }

    @Operation(summary = "Inativar uma rotina")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Rotina inativada com sucesso"),
            ApiResponse(responseCode = "404", description = "Rotina não encontrada")
        ]
    )
    @PatchMapping("/{id}/inativar")
    fun inativar(@PathVariable id: Int): ResponseEntity<Rotina> {
        return rotinaRepository.findById(id).map { rotina ->
            rotina.isAtivo = false
            val rotinaAtualizada = rotinaRepository.save(rotina)
            ResponseEntity.ok(rotinaAtualizada)
        }.orElse(ResponseEntity.notFound().build())
    }
}