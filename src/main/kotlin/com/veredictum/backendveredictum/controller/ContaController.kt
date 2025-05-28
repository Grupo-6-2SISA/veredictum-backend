package com.veredictum.backendveredictum.controller

import com.veredictum.backendveredictum.entity.Conta
import com.veredictum.backendveredictum.entity.Usuario
import com.veredictum.backendveredictum.services.ContaService
import com.veredictum.backendveredictum.services.UsuarioService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate

@Tag(name = "Contas", description = "Endpoints para gerenciamento de contas")
@RestController
@RequestMapping("/contas")
class ContaController(
    private val contaService: ContaService,
    private val usuarioService: UsuarioService
) {

    @Operation(
        summary = "Criar nova conta",
        description = "Cria uma nova conta no sistema. Requer um ID de usuário válido."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Conta criada com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados inválidos ou usuário não informado/encontrado"),
            ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        ]
    )
    @PostMapping
    fun criarConta(@RequestBody conta: Conta): ResponseEntity<Conta> {
        val contaSalva = contaService.save(conta)
        return ResponseEntity.status(HttpStatus.CREATED).body(contaSalva)
    }

    @Operation(
        summary = "Buscar conta por ID",
        description = "Retorna os detalhes de uma conta específica com base no seu ID."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Conta encontrada"),
            ApiResponse(responseCode = "404", description = "Conta não encontrada"),
            ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        ]
    )


    @GetMapping("/{id}")
    fun buscarContaPorId(@PathVariable id: Int): ResponseEntity<Conta> {
        return contaService.findById(id)
            .map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())
    }

    @Operation(
        summary = "Listar todas as contas",
        description = "Retorna uma lista de todas as contas cadastradas no sistema, ordenadas por status de pagamento (não pago primeiro)."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista de contas retornada com sucesso"),
            ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        ]
    )
    @GetMapping
    fun listarTodasContas(): ResponseEntity<List<Conta>> {
        val contas = contaService.findAll(Sort.by(Sort.Direction.ASC, "isPago"))
        // Para mostrar 'isPago = false' primeiro, podemos inverter a ordem após buscar, se necessário.
        val sortedContas = contas.sortedBy { !it.isPago }
        return ResponseEntity.ok(sortedContas)
    }

    @Operation(
        summary = "Listar contas por ID de usuário",
        description = "Retorna uma lista de todas as contas associadas a um determinado ID de usuário."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista de contas do usuário retornada com sucesso"),
            ApiResponse(responseCode = "204", description = "Nenhuma conta encontrada para o usuário"),
            ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        ]
    )
    @GetMapping("/por-usuario/{fkUsuarioId}")
    fun listarContasPorUsuario(@PathVariable fkUsuarioId: Int): ResponseEntity<List<Conta>> {
        val contas = contaService.findByUsuarioId(fkUsuarioId)
        return if (contas.isNotEmpty()) {
            ResponseEntity.ok(contas)
        } else {
            ResponseEntity.noContent().build()
        }
    }

    @Operation(
        summary = "Listar contas por status de pagamento",
        description = "Retorna uma lista de todas as contas com o status de pagamento especificado."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista de contas por status de pagamento retornada com sucesso"),
            ApiResponse(responseCode = "204", description = "Nenhuma conta encontrada com o status especificado"),
            ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        ]
    )
    @GetMapping("/por-pagamento/{isPago}")
    fun listarContasPorPago(@PathVariable isPago: Boolean): ResponseEntity<List<Conta>> {
        val contas = contaService.findByIsPago(isPago)
        return if (contas.isNotEmpty()) {
            ResponseEntity.ok(contas)
        } else {
            ResponseEntity.noContent().build()
        }
    }

    @Operation(
        summary = "Atualizar parcialmente uma conta",
        description = "Atualiza parcialmente os dados de uma conta existente com base no seu ID."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Conta atualizada parcialmente com sucesso"),
            ApiResponse(responseCode = "404", description = "Conta não encontrada"),
            ApiResponse(responseCode = "400", description = "Dados inválidos")
        ]
    )
    @PatchMapping("/{id}")
    fun atualizarParcialConta(@PathVariable id: Int, @RequestBody updates: Map<String, Any>): ResponseEntity<Conta> {
        return try {
            val contaAtualizada = contaService.partialUpdate(id, updates)
            ResponseEntity.ok(contaAtualizada)
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    @Operation(
        summary = "Atualizar conta existente",
        description = "Atualiza os dados de uma conta existente com base no seu ID."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Conta atualizada com sucesso"),
            ApiResponse(responseCode = "404", description = "Conta não encontrada"),
            ApiResponse(responseCode = "400", description = "Dados inválidos")
        ]
    )
    @PutMapping("/{id}")
    fun atualizarConta(@PathVariable id: Int, @RequestBody contaAtualizada: Conta): ResponseEntity<Conta> {
        return try {
            val contaComId = contaAtualizada.copy(idConta = id)
            val contaSalva = contaService.save(contaComId)
            ResponseEntity.ok(contaSalva)
        } catch (e: ResponseStatusException) {
            throw e
        }
    }

    @Operation(
        summary = "Excluir conta por ID",
        description = "Exclui uma conta do sistema com base no seu ID."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Conta excluída com sucesso"),
            ApiResponse(responseCode = "404", description = "Conta não encontrada"),
            ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        ]
    )
    @DeleteMapping("/{id}")
    fun excluirConta(@PathVariable id: Int): ResponseEntity<Void> {
        return if (contaService.existsById(id)) {
            contaService.deleteById(id)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}