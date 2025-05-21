package com.veredictum.backendveredictum.controller

import com.veredictum.backendveredictum.dto.CriarUsuarioDTO
import com.veredictum.backendveredictum.dto.LogarUsuarioDTO
import com.veredictum.backendveredictum.dto.LoginUsuarioDTO
import com.veredictum.backendveredictum.dto.UsuarioDTO
import com.veredictum.backendveredictum.entity.Usuario
import com.veredictum.backendveredictum.repository.UsuarioRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*

class UsuarioControllerTest {

    private lateinit var usuarioRepository: UsuarioRepository
    private lateinit var usuarioController: UsuarioController

    @BeforeEach
    fun setUp() {
        usuarioRepository = mock(UsuarioRepository::class.java) // Cria um mock do repositório
        usuarioController = UsuarioController(usuarioRepository) // Inicializa o controlador com o mock
    }

    @Test
    fun `buscarTodos deve retornar lista de usuarios DTO quando houver usuarios ativos`() {
        // Arrange
        val usuario1 = Usuario(
            idUsuario = 1,
            nome = "Ana",
            email = "ana@example.com",
            senha = "senha1",
            isAtivo = true,
            isAdm = false
        )
        val usuario2 = Usuario(
            idUsuario = 2,
            nome = "Beto",
            email = "beto@example.com",
            senha = "senha2",
            isAtivo = true,
            isAdm = false
        )
        val usuarios = listOf(usuario1, usuario2)
        val usuarioDTO1 = UsuarioDTO(
            idUsuario = 1,
            nome = "Ana",
            email = "ana@example.com",
            isAtivo = true,
            isAdm = false,
            fkAdm = null
        )
        val usuarioDTO2 = UsuarioDTO(
            idUsuario = 2,
            nome = "Beto",
            email = "beto@example.com",
            isAtivo = true,
            isAdm = false,
            fkAdm = null
        )
        val usuariosDTO = listOf(usuarioDTO1, usuarioDTO2)

        `when`(usuarioRepository.findAllOrderByIsAtivo()).thenReturn(usuarios) // Define o comportamento do mock

        // Act
        val response: ResponseEntity<List<UsuarioDTO>> = usuarioController.buscarTodos()

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(usuariosDTO, response.body)
        verify(usuarioRepository, times(1)).findAllOrderByIsAtivo() // Verifica se o método do repositório foi chamado
    }

    @Test
    fun `buscarTodos deve retornar status 204 quando nao houver usuarios ativos`() {
        // Arrange
        `when`(usuarioRepository.findAllOrderByIsAtivo()).thenReturn(emptyList())

        // Act
        val response: ResponseEntity<List<UsuarioDTO>> = usuarioController.buscarTodos()

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
        verify(usuarioRepository, times(1)).findAllOrderByIsAtivo()
    }



    @Test
    fun `logar deve retornar status 400 quando usuario e senha estiverem incorretos`() {
        // Arrange
        val logarUsuarioDTO = LogarUsuarioDTO("ana@example.com", "senha1")
        `when`(usuarioRepository.findByEmailAndSenha(logarUsuarioDTO.email, logarUsuarioDTO.senha)).thenReturn(null)

        // Act
        val response: ResponseEntity<LoginUsuarioDTO> = usuarioController.logar(logarUsuarioDTO)

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertNull(response.body)
        verify(usuarioRepository, times(1)).findByEmailAndSenha(logarUsuarioDTO.email, logarUsuarioDTO.senha)
    }

    @Test
    fun `logar deve retornar status 401 quando usuario estiver inativo`() {
        // Arrange
        val usuario = Usuario(
            idUsuario = 1,
            nome = "Ana",
            email = "ana@example.com",
            senha = "senha1",
            isAtivo = false,
            isAdm = false
        )
        val logarUsuarioDTO = LogarUsuarioDTO("ana@example.com", "senha1")

        `when`(usuarioRepository.findByEmailAndSenha(logarUsuarioDTO.email, logarUsuarioDTO.senha)).thenReturn(usuario)

        // Act
        val response: ResponseEntity<LoginUsuarioDTO> = usuarioController.logar(logarUsuarioDTO)

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
        assertNull(response.body)
        verify(usuarioRepository, times(1)).findByEmailAndSenha(logarUsuarioDTO.email, logarUsuarioDTO.senha)
    }

    @Test
    fun `deslogar deve retornar usuario DTO com isLogado false quando usuario estiver ativo`() {
        // Arrange
        val usuario = Usuario(
            idUsuario = 1,
            nome = "Ana",
            email = "ana@example.com",
            senha = "senha1",
            isAtivo = true,
            isAdm = false
        )
        val loginUsuarioDTO = LoginUsuarioDTO(
            idUsuario = 1,
            nome = "Ana",
            email = "ana@example.com",
            isAtivo = true,
            isAdm = false,
            isLogado = false
        )

        `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario))

        // Act
        val response: ResponseEntity<LoginUsuarioDTO> = usuarioController.deslogar(1)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(loginUsuarioDTO, response.body)
        verify(usuarioRepository, times(1)).findById(1)
    }

    @Test
    fun `deslogar deve retornar status 404 quando usuario nao for encontrado`() {
        // Arrange
        `when`(usuarioRepository.findById(1)).thenReturn(Optional.empty())

        // Act
        val response: ResponseEntity<LoginUsuarioDTO> = usuarioController.deslogar(1)

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNull(response.body)
        verify(usuarioRepository, times(1)).findById(1)
    }

    @Test
    fun `deslogar deve retornar status 404 quando usuario estiver inativo`() {
        // Arrange
        val usuario = Usuario(
            idUsuario = 1,
            nome = "Ana",
            email = "ana@example.com",
            senha = "senha1",
            isAtivo = false,
            isAdm = false
        )
        `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario))

        // Act
        val response: ResponseEntity<LoginUsuarioDTO> = usuarioController.deslogar(1)

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNull(response.body)
        verify(usuarioRepository, times(1)).findById(1)
    }

    @Test
    fun `buscarPorAdministrador deve retornar lista de usuarios DTO quando houver usuarios para o administrador`() {
        // Arrange
        val administrador = Usuario(
            idUsuario = 1,
            nome = "Admin",
            email = "admin@example.com",
            senha = "admin",
            isAtivo = true,
            isAdm = true
        )
        val usuario1 = Usuario(
            idUsuario = 2,
            nome = "Ana",
            email = "ana@example.com",
            senha = "senha1",
            isAtivo = true,
            isAdm = false,
            administrador = administrador
        )
        val usuario2 = Usuario(
            idUsuario = 3,
            nome = "Beto",
            email = "beto@example.com",
            senha = "senha2",
            isAtivo = false,
            isAdm = false,
            administrador = administrador
        )
        val usuarios = listOf(usuario1, usuario2)
        val usuarioDTO1 =
            UsuarioDTO(idUsuario = 2, nome = "Ana", email = "ana@example.com", isAtivo = true, isAdm = false, fkAdm = 1)
        val usuarioDTO2 = UsuarioDTO(
            idUsuario = 3,
            nome = "Beto",
            email = "beto@example.com",
            isAtivo = false,
            isAdm = false,
            fkAdm = 1
        )
        val usuariosDTO = listOf(usuarioDTO1, usuarioDTO2)

        `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(administrador))
        `when`(usuarioRepository.findByAdministradorOrderByIsAtivo(administrador)).thenReturn(usuarios)

        // Act
        val response: ResponseEntity<List<UsuarioDTO>> = usuarioController.buscarPorAdministrador(1)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(usuariosDTO, response.body)
        verify(usuarioRepository, times(1)).findById(1)
        verify(usuarioRepository, times(1)).findByAdministradorOrderByIsAtivo(administrador)
    }

    @Test
    fun `buscarPorAdministrador deve retornar status 204 quando nao houver usuarios para o administrador`() {
        // Arrange
        val administrador = Usuario(
            idUsuario = 1,
            nome = "Admin",
            email = "admin@example.com",
            senha = "admin",
            isAtivo = true,
            isAdm = true
        )
        `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(administrador))
        `when`(usuarioRepository.findByAdministradorOrderByIsAtivo(administrador)).thenReturn(emptyList())

        // Act
        val response: ResponseEntity<List<UsuarioDTO>> = usuarioController.buscarPorAdministrador(1)

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
        verify(usuarioRepository, times(1)).findById(1)
        verify(usuarioRepository, times(1)).findByAdministradorOrderByIsAtivo(administrador)
    }




    @Test
    fun `buscarPorSemAdministrador deve retornar lista de usuarios DTO quando houver usuarios sem administrador`() {
        // Arrange
        val usuario1 = Usuario(
            idUsuario = 2,
            nome = "Ana",
            email = "ana@example.com",
            senha = "senha1",
            isAtivo = true,
            isAdm = false,
            administrador = null
        )
        val usuario2 = Usuario(
            idUsuario = 3,
            nome = "Beto",
            email = "beto@example.com",
            senha = "senha2",
            isAtivo = false,
            isAdm = false,
            administrador = null
        )
        val usuarios = listOf(usuario1, usuario2)
        val usuarioDTO1 = UsuarioDTO(
            idUsuario = 2,
            nome = "Ana",
            email = "ana@example.com",
            isAtivo = true,
            isAdm = false,
            fkAdm = null
        )
        val usuarioDTO2 = UsuarioDTO(
            idUsuario = 3,
            nome = "Beto",
            email = "beto@example.com",
            isAtivo = false,
            isAdm = false,
            fkAdm = null
        )
        val usuariosDTO = listOf(usuarioDTO1, usuarioDTO2)

        `when`(usuarioRepository.findAllByAdministradorNullOrderByIsAtivo()).thenReturn(usuarios)

        // Act
        val response: ResponseEntity<List<UsuarioDTO>> = usuarioController.buscarPorSemAdministrador()

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(usuariosDTO, response.body)
        verify(usuarioRepository, times(1)).findAllByAdministradorNullOrderByIsAtivo()
    }

    @Test
    fun `buscarPorSemAdministrador deve retornar status 204 quando nao houver usuarios sem administrador`() {
        // Arrange
        `when`(usuarioRepository.findAllByAdministradorNullOrderByIsAtivo()).thenReturn(emptyList())

        // Act
        val response: ResponseEntity<List<UsuarioDTO>> = usuarioController.buscarPorSemAdministrador()

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
        verify(usuarioRepository, times(1)).findAllByAdministradorNullOrderByIsAtivo()
    }

    @Test
    fun `ativarUsuario deve retornar usuario DTO com isAtivo true quando usuario for encontrado`() {
        // Arrange
        val usuario = Usuario(
            idUsuario = 1,
            nome = "Ana",
            email = "ana@example.com",
            senha = "senha1",
            isAtivo = false,
            isAdm = false
        )
        val usuarioAtivado = Usuario(
            idUsuario = 1,
            nome = "Ana",
            email = "ana@example.com",
            senha = "senha1",
            isAtivo = true,
            isAdm = false
        )
        val usuarioDTO = UsuarioDTO(
            idUsuario = 1,
            nome = "Ana",
            email = "ana@example.com",
            isAtivo = true,
            isAdm = false,
            fkAdm = null
        )

        `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario))
        `when`(usuarioRepository.save(usuarioAtivado)).thenReturn(usuarioAtivado)

        // Act
        val response: ResponseEntity<UsuarioDTO> = usuarioController.ativarUsuario(1)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(usuarioDTO, response.body)
        verify(usuarioRepository, times(1)).findById(1)
        verify(usuarioRepository, times(1)).save(usuarioAtivado)
    }

    @Test
    fun `ativarUsuario deve retornar status 404 quando usuario nao for encontrado`() {
        // Arrange
        `when`(usuarioRepository.findById(1)).thenReturn(Optional.empty())

        // Act
        val response: ResponseEntity<UsuarioDTO> = usuarioController.ativarUsuario(1)

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNull(response.body)
        verify(usuarioRepository, times(1)).findById(1)
        verify(usuarioRepository, never()).save(any())
    }

    @Test
    fun `inativarUsuario deve retornar usuario DTO com isAtivo false quando usuario for encontrado`() {
        // Arrange
        val usuario = Usuario(
            idUsuario = 1,
            nome = "Ana",
            email = "ana@example.com",
            senha = "senha1",
            isAtivo = true,
            isAdm = false
        )
        val usuarioInativado = Usuario(
            idUsuario = 1,
            nome = "Ana",
            email = "ana@example.com",
            senha = "senha1",
            isAtivo = false,
            isAdm = false
        )
        val usuarioDTO = UsuarioDTO(
            idUsuario = 1,
            nome = "Ana",
            email = "ana@example.com",
            isAtivo = false,
            isAdm = false,
            fkAdm = null
        )

        `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario))
        `when`(usuarioRepository.save(usuarioInativado)).thenReturn(usuarioInativado)

        // Act
        val response: ResponseEntity<UsuarioDTO> = usuarioController.inativarUsuario(1)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(usuarioDTO, response.body)
        verify(usuarioRepository, times(1)).findById(1)
        verify(usuarioRepository, times(1)).save(usuarioInativado)
    }

    @Test
    fun `inativarUsuario deve retornar status 404 quando usuario nao for encontrado`() {
        // Arrange
        `when`(usuarioRepository.findById(1)).thenReturn(Optional.empty())

        // Act
        val response: ResponseEntity<UsuarioDTO> = usuarioController.inativarUsuario(1)

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNull(response.body)
        verify(usuarioRepository, times(1)).findById(1)
        verify(usuarioRepository, never()).save(any())
    }


    @Test
    fun `atualizarParcial deve retornar 404 quando o usuario nao existe`() {
        // Arrange
        val atualizacoes = mapOf<String, Any>("nome" to "Ana Maria")
        `when`(usuarioRepository.findById(1)).thenReturn(Optional.empty())

        // Act
        val response = usuarioController.atualizarParcial(1, atualizacoes)

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNull(response.body)
        verify(usuarioRepository, times(1)).findById(1)
        verify(usuarioRepository, never()).save(any())
    }

    @Test
    fun `atualizar deve atualizar o usuario e retornar 200 quando o usuario existe`() {
        // Arrange
        val usuarioExistente = Usuario(
            idUsuario = 1,
            nome = "Ana",
            email = "ana@example.com",
            senha = "senha1",
            isAtivo = true,
            isAdm = false
        )
        val usuarioAtualizado = Usuario(
            idUsuario = 1,
            nome = "Ana Maria",
            email = "ana.maria@example.com",
            senha = "novaSenha",
            isAtivo = false,
            isAdm = true
        )
        val usuarioDTO = UsuarioDTO(
            idUsuario = 1,
            nome = "Ana Maria",
            email = "ana.maria@example.com",
            isAtivo = false,
            isAdm = true,
            fkAdm = null
        )

        `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioExistente))
        `when`(usuarioRepository.save(usuarioAtualizado)).thenReturn(usuarioAtualizado)

        // Act
        val response = usuarioController.atualizar(1, usuarioAtualizado)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(usuarioDTO, response.body)
        verify(usuarioRepository, times(1)).findById(1)
        verify(usuarioRepository, times(1)).save(usuarioAtualizado)
    }

    @Test
    fun `atualizar deve retornar 400 quando o id do usuario no corpo da requisicao for diferente do id na url`() {
        // Arrange
        val usuarioAtualizado = Usuario(
            idUsuario = 2,
            nome = "Ana Maria",
            email = "ana.maria@example.com",
            senha = "novaSenha",
            isAtivo = false,
            isAdm = true
        )

        // Act
        val response = usuarioController.atualizar(1, usuarioAtualizado)

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertNull(response.body)
        verify(usuarioRepository, never()).findById(any())
        verify(usuarioRepository, never()).save(any())
    }



    @Test
    fun `alterarSenha deve alterar a senha do usuario e retornar 200 quando a senha for valida`() {
        // Arrange
        val usuario = Usuario(
            idUsuario = 1,
            nome = "Ana",
            email = "ana@example.com",
            senha = "senha1",
            isAtivo = true,
            isAdm = false
        )
        val novaSenhaMap = mapOf("senha" to "novaSenha123")

        `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario))
        `when`(usuarioRepository.save(any())).thenReturn(usuario)

        // Act
        val response = usuarioController.alterarSenha(1, novaSenhaMap)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        verify(usuarioRepository, times(1)).findById(1)
        verify(usuarioRepository, times(1)).save(any())
        assertEquals("novaSenha123", usuario.senha)
    }

    @Test
    fun `alterarSenha deve retornar 400 quando a senha for invalida`() {
        // Arrange
        val usuario = Usuario(
            idUsuario = 1,
            nome = "Ana",
            email = "ana@example.com",
            senha = "senha1",
            isAtivo = true,
            isAdm = false
        )
        val senhaInvalidaMap = mapOf("senha" to "123")

        `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario))

        // Act
        val response = usuarioController.alterarSenha(1, senhaInvalidaMap)

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        verify(usuarioRepository, times(1)).findById(1)
        verify(usuarioRepository, never()).save(any())
        assertEquals("senha1", usuario.senha)
    }

    @Test
    fun `alterarSenha deve retornar 404 quando o usuario nao for encontrado`() {
        // Arrange
        `when`(usuarioRepository.findById(1)).thenReturn(Optional.empty())
        val novaSenhaMap = mapOf("senha" to "novaSenha123")

        // Act
        val response = usuarioController.alterarSenha(1, novaSenhaMap)

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        verify(usuarioRepository, times(1)).findById(1)
        verify(usuarioRepository, never()).save(any())
    }

}