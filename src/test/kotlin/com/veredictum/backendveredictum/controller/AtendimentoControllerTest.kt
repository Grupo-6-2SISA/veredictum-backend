package com.veredictum.backendveredictum.controller

import com.veredictum.backendveredictum.dto.AtendimentoDTO
import com.veredictum.backendveredictum.entity.Atendimento
import com.veredictum.backendveredictum.entity.Cliente
import com.veredictum.backendveredictum.entity.Usuario
import com.veredictum.backendveredictum.services.AtendimentoService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.springframework.http.HttpStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import org.mockito.Mockito.times
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.doReturn

@ExtendWith(MockitoExtension::class)
class AtendimentoControllerTest {


    @Mock
    private lateinit var atendimentoService: AtendimentoService

    @InjectMocks
    private lateinit var controller: AtendimentoController


    @Test
    fun `getAtendimento deve retornar OK quando encontrar o atendimento`() {
        // Arrange
        val id = 1
        val atendimento = mock(Atendimento::class.java)
        val atendimentoDTO = AtendimentoDTO(
            idAgendamento = 1,
            fkCliente = 1,
            fkUsuario = 1,
            etiqueta = "Consulta",
            valor = 100.0,
            descricao = "Descrição do atendimento",
            dataInicio = LocalDateTime.now(),
            dataFim = LocalDateTime.now().plusHours(1),
            dataVencimento = LocalDateTime.now().plusDays(7),
            isPago = false
        )

        `when`(atendimentoService.getAtendimento(id)).thenReturn(atendimento)
        `when`(atendimento.toDTO()).thenReturn(atendimentoDTO)

        // Act
        val response = controller.getAtendimento(id)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(atendimentoDTO, response.body)
        verify(atendimentoService).getAtendimento(id)
    }

    @Test
    fun `getAtendimento deve retornar NOT_FOUND quando não encontrar o atendimento`() {
        // Arrange
        val id = 2
        `when`(atendimentoService.getAtendimento(id)).thenReturn(null)

        // Act
        val response = controller.getAtendimento(id)

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNull(response.body)
        verify(atendimentoService).getAtendimento(id)
    }

    @Test
    fun `getTodos deve retornar OK quando encontrar atendimentos`() {
        // Arrange
        val atendimento = mock(Atendimento::class.java)
        val atendimentoDTO = AtendimentoDTO(
            idAgendamento = 1,
            fkCliente = 1,
            fkUsuario = 1,
            etiqueta = "Consulta",
            valor = 100.0,
            descricao = "Descrição do atendimento",
            dataInicio = LocalDateTime.now(),
            dataFim = LocalDateTime.now().plusHours(1),
            dataVencimento = LocalDateTime.now().plusDays(7),
            isPago = false
        )

        `when`(atendimentoService.getTodos()).thenReturn(listOf(atendimento))
        `when`(atendimento.toDTO()).thenReturn(atendimentoDTO)

        // Act
        val response = controller.getTodos()

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(listOf(atendimentoDTO), response.body)
        verify(atendimentoService).getTodos()
    }

    @Test
    fun `getTodos deve retornar NO_CONTENT quando não encontrar atendimentos`() {
        // Arrange
        `when`(atendimentoService.getTodos()).thenReturn(emptyList())

        // Act
        val response = controller.getTodos()

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
        verify(atendimentoService).getTodos()
    }

    @Test
    fun `listarPorCliente deve retornar OK quando encontrar atendimentos`() {
        // Arrange
        val clienteId = 1
        val atendimento = mock(Atendimento::class.java)
        val atendimentoDTO = AtendimentoDTO(
            idAgendamento = 1,
            fkCliente = clienteId,
            fkUsuario = 1,
            etiqueta = "Consulta",
            valor = 100.0,
            descricao = "Descrição do atendimento",
            dataInicio = LocalDateTime.now(),
            dataFim = LocalDateTime.now().plusHours(1),
            dataVencimento = LocalDateTime.now().plusDays(7),
            isPago = false
        )

        `when`(atendimentoService.getPorCliente(clienteId)).thenReturn(listOf(atendimento))
        `when`(atendimento.toDTO()).thenReturn(atendimentoDTO)

        // Act
        val response = controller.listarPorCliente(clienteId)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(listOf(atendimentoDTO), response.body)
        verify(atendimentoService).getPorCliente(clienteId)
    }

    @Test
    fun `listarPorCliente deve retornar NO_CONTENT quando não encontrar atendimentos`() {
        // Arrange
        val clienteId = 1
        `when`(atendimentoService.getPorCliente(clienteId)).thenReturn(emptyList())

        // Act
        val response = controller.listarPorCliente(clienteId)

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
        verify(atendimentoService).getPorCliente(clienteId)
    }

    @Test
    fun `listarPorUsuario deve retornar OK quando encontrar atendimentos`() {
        // Arrange
        val usuarioId = 1
        val atendimento = mock(Atendimento::class.java)
        val atendimentoDTO = AtendimentoDTO(
            idAgendamento = 1,
            fkCliente = 1,
            fkUsuario = usuarioId,
            etiqueta = "Consulta",
            valor = 100.0,
            descricao = "Descrição do atendimento",
            dataInicio = LocalDateTime.now(),
            dataFim = LocalDateTime.now().plusHours(1),
            dataVencimento = LocalDateTime.now().plusDays(7),
            isPago = false
        )

        `when`(atendimentoService.getPorUsuario(usuarioId)).thenReturn(listOf(atendimento))
        `when`(atendimento.toDTO()).thenReturn(atendimentoDTO)

        // Act
        val response = controller.listarPorUsuario(usuarioId)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(listOf(atendimentoDTO), response.body)
        verify(atendimentoService).getPorUsuario(usuarioId)
    }

    @Test
    fun `listarPorUsuario deve retornar NO_CONTENT quando não encontrar atendimentos`() {
        // Arrange
        val usuarioId = 1
        `when`(atendimentoService.getPorUsuario(usuarioId)).thenReturn(emptyList())

        // Act
        val response = controller.listarPorUsuario(usuarioId)

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
        verify(atendimentoService).getPorUsuario(usuarioId)
    }

    @Test
    fun `listarPorStatus deve retornar OK quando encontrar atendimentos pelo status`() {
        // Arrange
        val status = 1  // 1 para pago, 0 para não pago
        val atendimento = mock(Atendimento::class.java)
        val atendimentoDTO = AtendimentoDTO(
            idAgendamento = 1,
            fkCliente = 1,
            fkUsuario = 1,
            etiqueta = "Consulta",
            valor = 100.0,
            descricao = "Descrição do atendimento",
            dataInicio = LocalDateTime.now(),
            dataFim = LocalDateTime.now().plusHours(1),
            dataVencimento = LocalDateTime.now().plusDays(7),
            isPago = true
        )

        `when`(atendimentoService.getPorStatus(status)).thenReturn(listOf(atendimento))
        `when`(atendimento.toDTO()).thenReturn(atendimentoDTO)

        // Act
        val response = controller.listarPorStatus(status)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(listOf(atendimentoDTO), response.body)
        verify(atendimentoService).getPorStatus(status)
    }

    @Test
    fun `listarPorStatus deve retornar NO_CONTENT quando não encontrar atendimentos pelo status`() {
        // Arrange
        val status = 0  // 0 para não pago
        `when`(atendimentoService.getPorStatus(status)).thenReturn(emptyList())

        // Act
        val response = controller.listarPorStatus(status)

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
        verify(atendimentoService).getPorStatus(status)
    }


    @Test
    fun listarAtendimentosOrdenados() {
        // Arrange
        val atendimento1 = mock(Atendimento::class.java)
        val atendimento2 = mock(Atendimento::class.java)

        val atendimentoDTO1 = AtendimentoDTO(
            idAgendamento = 1,
            fkCliente = 1,
            fkUsuario = 1,
            etiqueta = "Consulta A",
            valor = 100.0,
            descricao = "Primeira consulta",
            dataInicio = LocalDateTime.now(),
            dataFim = LocalDateTime.now().plusHours(1),
            dataVencimento = LocalDateTime.now().plusDays(7),
            isPago = true
        )

        val atendimentoDTO2 = AtendimentoDTO(
            idAgendamento = 2,
            fkCliente = 2,
            fkUsuario = 1,
            etiqueta = "Consulta B",
            valor = 150.0,
            descricao = "Segunda consulta",
            dataInicio = LocalDateTime.now().plusDays(1),
            dataFim = LocalDateTime.now().plusDays(1).plusHours(1),
            dataVencimento = LocalDateTime.now().plusDays(8),
            isPago = false
        )

        `when`(atendimentoService.getAtendimentosOrdenados()).thenReturn(listOf(atendimento1, atendimento2))
        `when`(atendimento1.toDTO()).thenReturn(atendimentoDTO1)
        `when`(atendimento2.toDTO()).thenReturn(atendimentoDTO2)

        // Act
        val response = controller.listarAtendimentosOrdenados()

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(listOf(atendimentoDTO1, atendimentoDTO2), response.body)
        verify(atendimentoService).getAtendimentosOrdenados()
    }

    @Test
    fun `criarAtendimento deve retornar created quando sucesso`() {
        // Arrange
        val atendimento = Atendimento()
        val statusInicialId = 1
        val atendimentoCriado = Atendimento()
        `when`(atendimentoService.criarAtendimento(atendimento, statusInicialId)).thenReturn(atendimentoCriado)

        // Act
        val response = controller.criarAtendimento(atendimento, statusInicialId)

        // Assert
        assertEquals(201, response.statusCode.value())
        assertEquals(atendimentoCriado.toDTO(), response.body)
        verify(atendimentoService).criarAtendimento(atendimento, statusInicialId)
    }

    @Test
    fun `mudarStatus deve retornar ok quando sucesso`() {
        // Arrange
        val idAtendimento = 1
        val novoStatusId = 2
        `when`(atendimentoService.mudarStatus(idAtendimento, novoStatusId)).thenReturn(true)

        // Act
        val response = controller.mudarStatus(idAtendimento, novoStatusId)

        // Assert
        assertEquals(200, response.statusCode.value())
        verify(atendimentoService).mudarStatus(idAtendimento, novoStatusId)
    }

    @Test
    fun `editarAtendimento deve retornar ok quando sucesso`() {
        // Arrange
        val id = 1
        val atendimento = Atendimento()
        val atendimentoEditado = Atendimento()
        `when`(atendimentoService.editarAtendimento(id, atendimento)).thenReturn(atendimentoEditado)

        // Act
        val response = controller.editarAtendimento(id, atendimento)

        // Assert
        assertEquals(200, response.statusCode.value())
        assertEquals(atendimentoEditado.toDTO(), response.body)
        verify(atendimentoService).editarAtendimento(id, atendimento)
    }

}