package com.veredictum.backendveredictum.controller

import com.veredictum.backendveredictum.dto.ContaDTO
import com.veredictum.backendveredictum.entity.Conta
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
import java.util.NoSuchElementException

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
    fun criarConta(@RequestBody contaDTO: ContaDTO): ResponseEntity<Conta> {
        try {

            val conta = Conta(
                dataCriacao = contaDTO.dataCriacao,
                etiqueta = contaDTO.etiqueta,
                valor = contaDTO.valor,
                dataVencimento = contaDTO.dataVencimento,
                urlNuvem = contaDTO.urlNuvem,
                descricao = contaDTO.descricao,
                isPago = contaDTO.isPago
            )
            val contaSalva = contaService.save(conta)
            return ResponseEntity.status(HttpStatus.CREATED).body(contaSalva)
        } catch (e: ResponseStatusException) {
            throw e
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao criar conta.", e)
        }
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
            ApiResponse(responseCode = "204", description = "Nenhuma conta encontrada"),
            ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        ]
    )
    @GetMapping
    fun listarTodasContas(): ResponseEntity<List<Conta>> {
        val sort = Sort.by(Sort.Direction.ASC, "isPago", "dataVencimento")
        val contas = contaService.findAll(sort)
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
            ApiResponse(responseCode = "400", description = "Dados inválidos para atualização")
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
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message, e)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao atualizar conta parcialmente.", e)
        }
    }

    @Operation(
        summary = "Atualizar conta existente",
        description = "Atualiza os dados de uma conta existente com base no seu ID."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Conta atualizada com sucesso"),
            ApiResponse(responseCode = "404", description = "Conta não encontrada para atualização"),
            ApiResponse(responseCode = "400", description = "Dados inválidos")
        ]
    )
    @PutMapping("/{id}")
    fun atualizarConta(@PathVariable id: Int, @RequestBody contaDTO: ContaDTO): ResponseEntity<Conta> {
        if (!contaService.existsById(id)) {
            return ResponseEntity.notFound().build()
        }
        try {

            val contaParaSalvar = Conta(
                idConta = id,
                dataCriacao = contaDTO.dataCriacao,
                etiqueta = contaDTO.etiqueta,
                valor = contaDTO.valor,
                dataVencimento = contaDTO.dataVencimento,
                urlNuvem = contaDTO.urlNuvem,
                descricao = contaDTO.descricao,
                isPago = contaDTO.isPago
            )
            val contaSalva = contaService.save(contaParaSalvar)
            return ResponseEntity.ok(contaSalva)
        } catch (e: ResponseStatusException) {
            throw e
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao atualizar conta.", e)
        }
    }

    @Operation(
        summary = "Excluir conta por ID",
        description = "Exclui uma conta do sistema com base no seu ID."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Conta excluída com sucesso"),
            ApiResponse(responseCode = "404", description = "Conta não encontrada para exclusão"),
            ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        ]
    )
    @DeleteMapping("/{id}")
    fun excluirConta(@PathVariable id: Int): ResponseEntity<Void> {
        if (!contaService.existsById(id)) {
            return ResponseEntity.notFound().build()
        }
        contaService.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}