package com.veredictum.backendveredictum.controller

import com.veredictum.backendveredictum.dto.AtendimentoDTO
import com.veredictum.backendveredictum.entity.Atendimento
import com.veredictum.backendveredictum.services.AtendimentoService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Atendimentos", description = "Endpoints para gerenciar atendimentos")
@RestController
@RequestMapping("/atendimentos")
class AtendimentoController(
    private val atendimentoService: AtendimentoService
) {

    @Operation(
        summary = "Buscar atendimento por ID",
        description = "Recupera as informações de um atendimento específico baseado no ID fornecido."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Atendimento encontrado com sucesso"),
            ApiResponse(responseCode = "404", description = "Atendimento não encontrado")
        ]
    )
    @GetMapping("/{id}")
    fun getAtendimento(@PathVariable id: Int): ResponseEntity<AtendimentoDTO> {

        val atendimento = atendimentoService.getAtendimento(id)

        if (atendimento != null) {

            val atendimentoDto = atendimento.toDTO()
            return ResponseEntity.ok(atendimentoDto)

        } else {
            return ResponseEntity.notFound().build()
        }
    }

    @Operation(
        summary = "Listar todos os atendimentos",
        description = "Recupera todos os atendimentos registrados no sistema."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Atendimentos encontrados com sucesso"),
            ApiResponse(responseCode = "204", description = "Nenhum atendimento encontrado")
        ]
    )
    @GetMapping()
    fun getTodos(): ResponseEntity<List<AtendimentoDTO>> {
        val atendimentos = atendimentoService.getTodos()
        return if (atendimentos.isEmpty()) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok(atendimentos.map { it.toDTO() })
        }
    }

    @Operation(
        summary = "Listar atendimentos por cliente",
        description = "Recupera todos os atendimentos associados a um cliente específico."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Atendimentos encontrados para o cliente"),
            ApiResponse(responseCode = "204", description = "Nenhum atendimento encontrado para este cliente")
        ]
    )
    @GetMapping("listar-por-cliente/{id}")
    fun listarPorCliente(@PathVariable id: Int): ResponseEntity<List<AtendimentoDTO>> {
        val atendimentos = atendimentoService.getPorCliente(id)
        return if (atendimentos.isEmpty()) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok(atendimentos.map { it.toDTO() })
        }
    }

    @Operation(
        summary = "Listar atendimentos por usuário",
        description = "Recupera todos os atendimentos realizados por um usuário específico."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Atendimentos encontrados para o usuário"),
            ApiResponse(responseCode = "204", description = "Nenhum atendimento encontrado para este usuário")
        ]
    )
    @GetMapping("listar-por-usuario/{id}")
    fun listarPorUsuario(@PathVariable id: Int): ResponseEntity<List<AtendimentoDTO>> {
        val atendimentos = atendimentoService.getPorUsuario(id)
        return if (atendimentos.isEmpty()) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok(atendimentos.map { it.toDTO() })
        }
    }

    @Operation(
        summary = "Listar atendimentos por id de status",
        description = "Recupera todos os atendimentos com um status específico."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Atendimentos encontrados para o id de status"),
            ApiResponse(responseCode = "204", description = "Nenhum atendimento encontrado para este id de status")
        ]
    )
    @GetMapping("listar-por-status/{status}")
    fun listarPorStatus(@PathVariable status: Int): ResponseEntity<List<AtendimentoDTO>> {
        val atendimentos = atendimentoService.getPorStatus(status)
        return if (atendimentos.isEmpty()) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok(atendimentos.map { it.toDTO() })
        }
    }

    @Operation(
        summary = "Listar atendimentos ordenados pelo mais recente",
        description = "Recupera todos os atendimentos ordenados pela data de início, do mais próximo para o mais distante."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Atendimentos encontrados e ordenados com sucesso"),
            ApiResponse(responseCode = "204", description = "Nenhum atendimento encontrado")
        ]
    )
    @GetMapping("/mais-recentes")
    fun listarAtendimentosOrdenados(): ResponseEntity<List<AtendimentoDTO>> {
        val atendimentos = atendimentoService.getAtendimentosOrdenados()
        return if (atendimentos.isEmpty()) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok(atendimentos.map { it.toDTO() })
        }
    }


    @Operation(
        summary = "Criar novo atendimento",
        description = "Cria um novo atendimento, validando se já existe um atendimento marcado no mesmo horário para o mesmo cliente."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Atendimento criado com sucesso"),
            ApiResponse(responseCode = "400", description = "Horário já reservado ou erro de validação")
        ]
    )
    @PostMapping()
    fun criarAtendimento(
        @RequestBody atendimento: Atendimento,
        @RequestParam statusInicialId: Int
    ): ResponseEntity<AtendimentoDTO> {
        val atendimentoCriado = atendimentoService.criarAtendimento(atendimento, statusInicialId)

        if (atendimentoCriado == null) {
            return ResponseEntity.badRequest().build()
        }

        return ResponseEntity.status(201).body(atendimentoCriado.toDTO())
    }


    @Operation(
        summary = "Mudar status do atendimento",
        description = "Altera o status de um atendimento específico."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Status do atendimento alterado com sucesso"),
            ApiResponse(responseCode = "404", description = "Atendimento não encontrado")
        ]
    )
    @PatchMapping("/mudar-status/{id}/{status}")
    fun mudarStatus(@PathVariable id: Int, @PathVariable status: Int): ResponseEntity<Void> {
        val sucesso = atendimentoService.mudarStatus(id, status)
        return if (sucesso) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @Operation(
        summary = "Editar atendimento completo",
        description = "Edita todas as informações de um atendimento específico."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Atendimento editado com sucesso"),
            ApiResponse(responseCode = "404", description = "Atendimento não encontrado")
        ]
    )
    @PutMapping("/{id}")
    fun editarAtendimento(@PathVariable id: Int, @RequestBody atendimento: Atendimento): ResponseEntity<AtendimentoDTO> {
        val atendimentoEditado = atendimentoService.editarAtendimento(id, atendimento)
        return if (atendimentoEditado != null) {
            ResponseEntity.ok(atendimentoEditado.toDTO())
        } else {
            ResponseEntity.notFound().build()
        }
    }


}
