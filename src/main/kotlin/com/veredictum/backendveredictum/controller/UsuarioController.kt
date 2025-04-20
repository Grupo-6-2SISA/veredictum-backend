package com.veredictum.backendveredictum.controller

import com.veredictum.backendveredictum.dto.*
import com.veredictum.backendveredictum.entity.Usuario
import com.veredictum.backendveredictum.repository.UsuarioRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.models.media.Schema
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Usuários", description = "Endpoints para gerenciar usuários")
@RestController
@RequestMapping("/usuarios")
class UsuarioController (
    val repository: UsuarioRepository
) {

    @Operation(
        summary = "Buscar todos os usuários",
        description = "Retorna todos os usuários ativos ordenado por isAtivo. NotFound se nenhum usuário for encontrado."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Usuários retornados com sucesso"),
            ApiResponse(responseCode = "204", description = "Usuários cliente encontrado")
        ]
    )
    @GetMapping
    fun buscarTodos(): ResponseEntity<List<UsuarioDTO>> {
        val usuarios = repository.findAllOrderByIsAtivo()
        return if (usuarios.isEmpty()) {
            ResponseEntity.noContent().build() // 204 No Content
        } else {
            // Converte a lista de usuarios para DTOs
            ResponseEntity.ok(usuarios.map { it.toDTO() }) // 200 OK com lista de clientes
        }
    }

    @Operation(
        summary = "Logar com um usuário",
        description = "Retorna os dado do usuário a logar no sistema"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Usuário encontrado e logado com sucesso"),
            ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            ApiResponse(responseCode = "400", description = "Email ou Senha incorretos")
        ]
    )
    @PostMapping("/logar")
    fun logar(@RequestBody usuarioALogar: LogarUsuarioDTO): ResponseEntity<LoginUsuarioDTO> {
        val isLogado = repository.findByEmailAndSenha(usuarioALogar.email, usuarioALogar.senha)

        if (isLogado == null) {
            return ResponseEntity.badRequest().build() // 400 Bad Request
        }

        return if (isLogado.isAtivo) {
            ResponseEntity.ok(isLogado.toLoginDTO(true)) // 200 OK com dados do usuário
        } else {
            ResponseEntity.notFound().build() // 404 Not Found
        }
    }

    @Operation(
        summary = "Deslogar usuário",
        description = "Retorna os dados do usuário com o estado de login definido como false."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Usuário encontrado e deslogado com sucesso"),
            ApiResponse(responseCode = "404", description = "Usuário não encontrado ou inativo")
        ]
    )
    @GetMapping("/deslogar/{id}")
    fun deslogar(@PathVariable id: Int): ResponseEntity<LoginUsuarioDTO> {
        val usuario = repository.findById(id)
            .orElse(null)

        if (usuario == null) {
            return ResponseEntity.notFound().build() // 404 Not Found
        }

        return if (usuario.isAtivo) {
            ResponseEntity.ok(usuario.toLoginDTO(false)) // 200 OK com isLogado = false
        } else {
            ResponseEntity.notFound().build() // 404 Not Found
        }
    }

    @Operation(
        summary = "Listar usuários por administrador",
        description = "Retorna uma lista de usuários associados a um administrador específico, ordenados pelo status de atividade."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso"),
            ApiResponse(responseCode = "204", description = "Nenhum usuário encontrado para o administrador informado"),
            ApiResponse(responseCode = "400", description = "O ID informado não pertence a um administrador"),
            ApiResponse(responseCode = "404", description = "Administrador não encontrado")
        ]
    )
    @GetMapping("/lista-por-administrador/{idAdm}")
    fun buscarPorAdministrador(@PathVariable idAdm: Int): ResponseEntity<List<UsuarioDTO>> {

        val adm = repository.findById(idAdm)
            .orElse(null)

        if (adm == null) {
            return ResponseEntity.notFound().build() // 404 Not Found
        }

        if (!adm.isAdm) {
            return ResponseEntity.status(400).build() // 400 Bad Request
        }

        val usuarios = repository.findByAdministradorOrderByIsAtivo(adm)

        return if (usuarios.isEmpty()) {
            ResponseEntity.noContent().build() // 204 No Content
        } else {
            ResponseEntity.ok(usuarios.map { it.toDTO() }) // 200 OK com lista de usuários
        }
    }

    @Operation(
        summary = "Listar usuários sem administrador",
        description = "Retorna uma lista de usuários que não possuem administrador associado, ordenados pelo status de atividade."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso"),
            ApiResponse(responseCode = "204", description = "Nenhum usuário encontrado sem administrador")
        ]
    )
    @GetMapping("/lista-por-sem-administrador")
    fun buscarPorSemAdministrador(): ResponseEntity<List<UsuarioDTO>> {
        val usuarios = repository.findAllByAdministradorNullOrderByIsAtivo()
        return if (usuarios.isEmpty()) {
            ResponseEntity.noContent().build() // 204 No Content
        } else {
            // Converte a lista de usuarios para DTOs
            ResponseEntity.ok(usuarios.map { it.toDTO() }) // 200 OK com lista de clientes
        }
    }

    @Operation(
        summary = "Ativar usuário",
        description = "Ativa um usuário específico pelo ID e retorna os dados atualizados."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Usuário ativado com sucesso"),
            ApiResponse(responseCode = "404", description = "Usuário não encontrado")
        ]
    )
    @PatchMapping("/ativar/{id}")
    fun ativarUsuario(@PathVariable id: Int): ResponseEntity<UsuarioDTO> {
        val usuario = repository.findById(id)
            .orElse(null)

        if (usuario == null) {
            return ResponseEntity.notFound().build() // 404 Not Found
        }

        usuario.isAtivo = true
        val usuarioAtualizado = repository.save(usuario)
        return ResponseEntity.ok(usuarioAtualizado.toDTO()) // 200 OK com o usuário atualizado
    }

    @Operation(
        summary = "Inativar usuário",
        description = "Inativa um usuário específico pelo ID e retorna os dados atualizados."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Usuário inativado com sucesso"),
            ApiResponse(responseCode = "404", description = "Usuário não encontrado")
        ]
    )
    @PatchMapping("/inativar/{id}")
    fun inativarUsuario(@PathVariable id: Int): ResponseEntity<UsuarioDTO> {
        val usuario = repository.findById(id)
            .orElse(null)

        if (usuario == null) {
            return ResponseEntity.notFound().build() // 404 Not Found
        }

        usuario.isAtivo = false
        val usuarioAtualizado = repository.save(usuario)
        return ResponseEntity.ok(usuarioAtualizado.toDTO()) // 200 OK com o usuário atualizado
    }

    @Operation(
        summary = "Atualização parcial do usuário",
        description = "Atualiza parcialmente os dados do usuário utilizando um mapa de atualizações."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Usuário atualizado parcialmente com sucesso"),
            ApiResponse(responseCode = "404", description = "Usuário não encontrado")
        ]
    )
    @PatchMapping("/{id}")
    fun atualizarParcial(
        @PathVariable id: Int,
        @RequestBody atualizacoes: Map<String, Any>
    ): ResponseEntity<UsuarioDTO> {
        return repository.findById(id).map { usuario ->
            atualizacoes.forEach { (chave, valor) ->
                val valorStr = valor.toString()
                if (valorStr.isNotBlank()) {
                    when (chave) {
                        "idUsuario" -> usuario.idUsuario = id
                        "nome" -> usuario.nome = valorStr
                        "email" -> usuario.email = valorStr
                        "senha" -> usuario.senha = valorStr
                        "ativo" -> usuario.isAtivo = valorStr.toBoolean()
                        "adm" -> usuario.isAdm = valorStr.toBoolean()
                        "fkAdm" -> usuario.administrador = valorStr.toIntOrNull()?.let { adminId ->
                            repository.findById(adminId).orElse(null)
                        }
                        else -> {} // Ignora campos não reconhecidos
                    }
                }
            }
            val usuarioAtualizado = repository.save(usuario)
            ResponseEntity.ok(usuarioAtualizado.toDTO())
        }.orElseGet {
            ResponseEntity.notFound().build()
        }
    }

    @Operation(
        summary = "Atualizar usuário",
        description = "Atualiza os dados do usuário com o ID informado (Necessário informar senha nova). Se o corpo contiver um ID diferente, é ignorado."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            ApiResponse(responseCode = "400", description = "ID inconsistente entre a URL e o corpo"),
            ApiResponse(responseCode = "404", description = "Usuário não encontrado")
        ]
    )
    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Int, @RequestBody @Valid usuarioAtualizado: Usuario): ResponseEntity<UsuarioDTO> {
        if (usuarioAtualizado.idUsuario != null && usuarioAtualizado.idUsuario != id) {
            return ResponseEntity.badRequest().build()
        }
        return repository.findById(id).map { usuarioExistente ->
            usuarioAtualizado.idUsuario = id
            val usuarioSalvo = repository.save(usuarioAtualizado)
            ResponseEntity.ok(usuarioSalvo.toDTO())
        }.orElseGet {
            ResponseEntity.notFound().build()
        }
    }

    @Operation(
        summary = "Alterar senha do usuário",
        description = "Altera a senha de um usuário específico pelo ID. Retorna 200 se a senha for alterada com sucesso, 400 se a senha for inválida ou 404 se o usuário não for encontrado."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Senha alterada com sucesso"),
            ApiResponse(responseCode = "400", description = "Senha inválida ou não fornecida"),
            ApiResponse(responseCode = "404", description = "Usuário não encontrado")
        ]
    )
    @PatchMapping("/alterar-senha/{id}")
    fun alterarSenha(
        @PathVariable id: Int,
        @RequestBody senha: Map<String, String>
    ): ResponseEntity<Void> {
        val usuarioOptional = repository.findById(id)
        if (usuarioOptional.isEmpty) {
            return ResponseEntity.notFound().build() // 404 Not Found
        }

        val usuario = usuarioOptional.get()
        val novaSenha = senha["senha"]

        return if (novaSenha != null && novaSenha.isNotBlank() && novaSenha.length >= 6) {
                usuario.senha = novaSenha
                repository.save(usuario)
                ResponseEntity.ok().build() // 200 OK
            } else {
                ResponseEntity.badRequest().build() // 400 Bad Request
            }
    }

    @Operation(
        summary = "Cadastrar um novo usuário",
        description = "Cadastra um novo usuário no sistema. Retorna 201 se o cadastro for bem-sucedido, 400 se houver algum dado inválido ou 404 se o administrador associado não for encontrado."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso"),
            ApiResponse(responseCode = "404", description = "Administrador associado não encontrado"),
            ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
        ]
    )
    @PostMapping("/cadastrar")
    fun cadastrar(@RequestBody @Valid novoUsuarioDTO: CriarUsuarioDTO): ResponseEntity<UsuarioDTO> {

        val administrador = repository.findAdministradorById(novoUsuarioDTO.fkAdm)

        if (administrador == null) {
            return ResponseEntity.notFound().build() // 404 Not Found
        }

        val novoUsuario: Usuario = Usuario(
            nome = novoUsuarioDTO.nome,
            email = novoUsuarioDTO.email,
            senha = novoUsuarioDTO.senha,
            isAtivo = novoUsuarioDTO.isAtivo,
            isAdm = novoUsuarioDTO.isAdm,
            administrador = administrador
        )

        val usuarioSalvo = repository.save(novoUsuario)
        return ResponseEntity.status(201).body(usuarioSalvo.toDTO()) // 201 Created
    }

}