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
    fun criarConta(@RequestBody contaDTO: ContaDTO, @RequestParam statusInicialId: Int): ResponseEntity<Conta> {
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
            val contaSalva = contaService.criarConta(conta, statusInicialId)
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
            val contaSalva = contaService.editarConta(id, contaParaSalvar)
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

    @Operation(
        summary = "Listar contas por mês e ano",
        description = "Retorna uma lista de todas as contas cadastradas para o mês e ano especificados."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista de contas retornada com sucesso"),
            ApiResponse(responseCode = "204", description = "Nenhuma conta encontrada para o mês e ano especificados"),
            ApiResponse(responseCode = "400", description = "Parâmetros inválidos fornecidos"),
            ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        ]
    )
    @GetMapping("/por-mes-e-ano")
    fun listarContasPorMesEAno(
        @RequestParam mes: Int,
        @RequestParam ano: Int
    ): ResponseEntity<List<Conta>> {
        if (mes !in 1..12) {
            return ResponseEntity.badRequest().body(null)
        }
        if (ano.toString().length != 4) {
            return ResponseEntity.badRequest().body(null)
        }
        val contas = contaService.findByMesEAno(mes, ano)
        return if (contas.isNotEmpty()) {
            ResponseEntity.ok(contas)
        } else {
            ResponseEntity.noContent().build()
        }
    }

    @Operation(
        summary = "Obter total do mês",
        description = "Retorna o somatório do campo valor das contas cadastradas para o mês e ano especificados."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Total calculado com sucesso"),
            ApiResponse(responseCode = "400", description = "Parâmetros inválidos fornecidos"),
            ApiResponse(responseCode = "204", description = "Nenhuma conta encontrada para o mês e ano especificados"),
            ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        ]
    )
    @GetMapping("/total-por-mes-e-ano")
    fun getTotalPorMesEAno(
        @RequestParam mes: Int,
        @RequestParam ano: Int
    ): ResponseEntity<Double> {
        if (mes !in 1..12) {
            return ResponseEntity.badRequest().body(null)
        }
        if (ano.toString().length != 4) {
            return ResponseEntity.badRequest().body(null)
        }
        val total = contaService.getTotalPorMesEAno(mes, ano)
        return if (total > 0) {
            ResponseEntity.ok(total)
        } else {
            ResponseEntity.noContent().build()
        }
    }

    @Operation(
        summary = "Obter as top x contas mais atrasadas não pagas do ano atual",
        description = "Retorna as top x contas não pagas com data de vencimento mais antiga no ano atual, ordenadas do mais atrasado para o menos atrasado."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista de contas atrasadas retornada com sucesso"),
            ApiResponse(responseCode = "204", description = "Nenhuma conta atrasada encontrada para o ano atual"),
            ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        ]
    )
    @GetMapping("/mais-atrasadas/{top}")
    fun getMaisAtrasadas(@PathVariable top: Int?): ResponseEntity<List<Conta>> {
        val contas = contaService.getMaisAtrasadas(top)
        return if (contas.isNotEmpty()) {
            ResponseEntity.ok(contas)
        } else {
            ResponseEntity.noContent().build()
        }
    }

    @Operation(
        summary = "Obter as top x contas mais recentes do ano atual",
        description = "Retorna as top x contas com data de vencimento mais recente no ano atual, ordenadas da mais nova para a mais antiga."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista de contas recentes retornada com sucesso"),
            ApiResponse(responseCode = "204", description = "Nenhuma conta recente encontrada para o ano atual"),
            ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        ]
    )
    @GetMapping("/mais-recentes/{top}")
    fun getMaisRecentes(@PathVariable top: Int?): ResponseEntity<List<Conta>> {
        val contas = contaService.getMaisRecentes(top)
        return if (contas.isNotEmpty()) {
            ResponseEntity.ok(contas)
        } else {
            ResponseEntity.noContent().build()
        }
    }

}