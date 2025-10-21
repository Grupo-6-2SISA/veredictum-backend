package com.veredictum.backendveredictum.controller

import com.veredictum.backendveredictum.dto.AtendimentoDTO
import com.veredictum.backendveredictum.dto.VisaoGeralAtendimentoDTO
import com.veredictum.backendveredictum.entity.Atendimento
import com.veredictum.backendveredictum.services.AtendimentoService
import com.veredictum.backendveredictum.services.ClienteService
import com.veredictum.backendveredictum.services.UsuarioService
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
    private val atendimentoService: AtendimentoService,
    private val clienteService: ClienteService,
    private val usuarioService: UsuarioService
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
    fun listarAtendimentosOrdenados(): ResponseEntity<List<VisaoGeralAtendimentoDTO>> {
        val atendimentos = atendimentoService.visaoGeral()
        return if (atendimentos.isEmpty()) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok(atendimentos)
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
        @RequestBody atendimentoDTO: AtendimentoDTO,
        @RequestParam statusInicialId: Int
    ): ResponseEntity<AtendimentoDTO> {
        val cliente = clienteService.findById(atendimentoDTO.fkCliente)
            .orElseThrow { IllegalArgumentException("Cliente não encontrado com o ID fornecido") }

        val usuario = usuarioService.findById(atendimentoDTO.fkUsuario)
            .orElseThrow { IllegalArgumentException("Usuário não encontrado com o ID fornecido") }

        val atendimento = Atendimento(
            idAtendimento = null,
            cliente = cliente,
            usuario = usuario,
            etiqueta = atendimentoDTO.etiqueta,
            valor = atendimentoDTO.valor,
            descricao = atendimentoDTO.descricao,
            dataInicio = atendimentoDTO.dataInicio,
            dataFim = atendimentoDTO.dataFim,
            dataVencimento = atendimentoDTO.dataVencimento,
            isPago = atendimentoDTO.isPago,
            shouldEnviarEmail = atendimentoDTO.shouldEnviarEmail
        )

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

    @Operation(
        summary = "Excluir atendimento",
        description = "Remove um atendimento específico do sistema."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Atendimento excluído com sucesso"),
            ApiResponse(responseCode = "404", description = "Atendimento não encontrado")
        ]
    )
    @DeleteMapping("/{id}")
    fun excluirAtendimento(@PathVariable id: Int): ResponseEntity<Void> {
        val sucesso = atendimentoService.excluirAtendimento(id)
        return if (sucesso) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @Operation(
        summary = "Listar atendimentos de um mês/ano, ordenados por data",
        description = "Recupera os atendimentos do mês e ano informados, ordenados pela data de início (mais recentes primeiro)."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Atendimentos encontrados e ordenados com sucesso"),
            ApiResponse(responseCode = "204", description = "Nenhum atendimento encontrado para o mês/ano informado")
        ]
    )
    @GetMapping("listar-por-mes-ano/{ano}/{mes}")
    fun listarPorMesEAnoOrdenados(
        @PathVariable ano: Int,
        @PathVariable mes: Int
    ): ResponseEntity<List<AtendimentoDTO>> {
        val atendimentos = atendimentoService.getPorMesEAnoOrdenados(ano, mes)
        if (atendimentos != null) {
            return if (atendimentos.isEmpty()) {
                ResponseEntity.noContent().build()
            } else {
                ResponseEntity.ok(atendimentos.map { it.toDTO() })
            }
        } else {
            return ResponseEntity.notFound().build()
        }
    }

    @Operation(
        summary = "Contagem de atendimentos concluídos de um mês/ano, ordenados por data",
        description = "Recupera a contagem dos atendimentos do mês e ano informados, ordenados pela data de início (mais recentes primeiro)."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Atendimentos encontrados, contados e ordenados com sucesso"),
            ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
        ]
    )
    @GetMapping("contagem-cancelados/{mes}/{ano}")
    fun contagemCancelados(
        @PathVariable mes: Int,
        @PathVariable ano: Int
    ): ResponseEntity<Int> {

        if (mes !in 1..12) {
            return ResponseEntity.badRequest().body(null)
        }

        if (ano.toString().length != 4) {
            return ResponseEntity.badRequest().body(null)
        }

        val contagemAtendimentos = atendimentoService.contagemCancelados(ano, mes)

        return ResponseEntity.ok(contagemAtendimentos)

    }

}
