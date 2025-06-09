package com.veredictum.backendveredictum.controller

import com.veredictum.backendveredictum.dto.AtendimentoDTO
import com.veredictum.backendveredictum.entity.Atendimento
import com.veredictum.backendveredictum.services.AtendimentoService
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.http.HttpStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class AtendimentoControllerTest {

    @Mock
    private lateinit var atendimentoService: AtendimentoService

    @InjectMocks
    private lateinit var controller: AtendimentoController

    @Test
    fun `getAtendimento deve retornar OK quando encontrar o atendimento`() {
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
            isPago = false,
            shouldEnviarEmail = false
        )

        `when`(atendimentoService.getAtendimento(id)).thenReturn(atendimento)
        `when`(atendimento.toDTO()).thenReturn(atendimentoDTO)

        val response = controller.getAtendimento(id)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(atendimentoDTO, response.body)
        verify(atendimentoService).getAtendimento(id)
    }

    @Test
    fun `getAtendimento deve retornar NOT_FOUND quando não encontrar o atendimento`() {
        val id = 2
        `when`(atendimentoService.getAtendimento(id)).thenReturn(null)

        val response = controller.getAtendimento(id)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNull(response.body)
        verify(atendimentoService).getAtendimento(id)
    }

    @Test
    fun `getTodos deve retornar OK quando encontrar atendimentos`() {
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
            isPago = false,
            shouldEnviarEmail = false
        )

        `when`(atendimentoService.getTodos()).thenReturn(listOf(atendimento))
        `when`(atendimento.toDTO()).thenReturn(atendimentoDTO)

        val response = controller.getTodos()

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(listOf(atendimentoDTO), response.body)
        verify(atendimentoService).getTodos()
    }

    @Test
    fun `getTodos deve retornar NO_CONTENT quando não encontrar atendimentos`() {
        `when`(atendimentoService.getTodos()).thenReturn(emptyList())

        val response = controller.getTodos()

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
        verify(atendimentoService).getTodos()
    }

    @Test
    fun `listarPorCliente deve retornar OK quando encontrar atendimentos`() {
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
            isPago = false,
            shouldEnviarEmail = false
        )

        `when`(atendimentoService.getPorCliente(clienteId)).thenReturn(listOf(atendimento))
        `when`(atendimento.toDTO()).thenReturn(atendimentoDTO)

        val response = controller.listarPorCliente(clienteId)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(listOf(atendimentoDTO), response.body)
        verify(atendimentoService).getPorCliente(clienteId)
    }

    @Test
    fun `listarPorCliente deve retornar NO_CONTENT quando não encontrar atendimentos`() {
        val clienteId = 1
        `when`(atendimentoService.getPorCliente(clienteId)).thenReturn(emptyList())

        val response = controller.listarPorCliente(clienteId)

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
        verify(atendimentoService).getPorCliente(clienteId)
    }

    @Test
    fun `listarPorUsuario deve retornar OK quando encontrar atendimentos`() {
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
            isPago = false,
            shouldEnviarEmail = false
        )

        `when`(atendimentoService.getPorUsuario(usuarioId)).thenReturn(listOf(atendimento))
        `when`(atendimento.toDTO()).thenReturn(atendimentoDTO)

        val response = controller.listarPorUsuario(usuarioId)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(listOf(atendimentoDTO), response.body)
        verify(atendimentoService).getPorUsuario(usuarioId)
    }

    @Test
    fun `listarPorUsuario deve retornar NO_CONTENT quando não encontrar atendimentos`() {
        val usuarioId = 1
        `when`(atendimentoService.getPorUsuario(usuarioId)).thenReturn(emptyList())

        val response = controller.listarPorUsuario(usuarioId)

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
        verify(atendimentoService).getPorUsuario(usuarioId)
    }

    @Test
    fun `listarPorStatus deve retornar OK quando encontrar atendimentos pelo status`() {
        val status = 1
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
            isPago = true,
            shouldEnviarEmail = false
        )

        `when`(atendimentoService.getPorStatus(status)).thenReturn(listOf(atendimento))
        `when`(atendimento.toDTO()).thenReturn(atendimentoDTO)

        val response = controller.listarPorStatus(status)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(listOf(atendimentoDTO), response.body)
        verify(atendimentoService).getPorStatus(status)
    }

    @Test
    fun `listarPorStatus deve retornar NO_CONTENT quando não encontrar atendimentos pelo status`() {
        val status = 0
        `when`(atendimentoService.getPorStatus(status)).thenReturn(emptyList())

        val response = controller.listarPorStatus(status)

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
        verify(atendimentoService).getPorStatus(status)
    }

    @Test
    fun listarAtendimentosOrdenados() {
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
            isPago = true,
            shouldEnviarEmail = false
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
            isPago = false,
            shouldEnviarEmail = false
        )

        `when`(atendimentoService.getAtendimentosOrdenados()).thenReturn(listOf(atendimento1, atendimento2))
        `when`(atendimento1.toDTO()).thenReturn(atendimentoDTO1)
        `when`(atendimento2.toDTO()).thenReturn(atendimentoDTO2)

        val response = controller.listarAtendimentosOrdenados()

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(listOf(atendimentoDTO1, atendimentoDTO2), response.body)
        verify(atendimentoService).getAtendimentosOrdenados()
    }

    @Test
    fun `criarAtendimento deve retornar created quando sucesso`() {
        val atendimento = Atendimento()
        val statusInicialId = 1
        val atendimentoCriado = Atendimento()
        `when`(atendimentoService.criarAtendimento(atendimento, statusInicialId)).thenReturn(atendimentoCriado)

        val response = controller.criarAtendimento(atendimento, statusInicialId)

        assertEquals(201, response.statusCode.value())
        assertEquals(atendimentoCriado.toDTO(), response.body)
        verify(atendimentoService).criarAtendimento(atendimento, statusInicialId)
    }

    @Test
    fun `criarAtendimento deve retornar bad request quando conflito de horário`() {
        val atendimento = Atendimento()
        val statusInicialId = 1
        `when`(atendimentoService.criarAtendimento(atendimento, statusInicialId)).thenReturn(null)

        val response = controller.criarAtendimento(atendimento, statusInicialId)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertNull(response.body)
        verify(atendimentoService).criarAtendimento(atendimento, statusInicialId)
    }

    @Test
    fun `mudarStatus deve retornar ok quando sucesso`() {
        val idAtendimento = 1
        val novoStatusId = 2
        `when`(atendimentoService.mudarStatus(idAtendimento, novoStatusId)).thenReturn(true)

        val response = controller.mudarStatus(idAtendimento, novoStatusId)

        assertEquals(200, response.statusCode.value())
        verify(atendimentoService).mudarStatus(idAtendimento, novoStatusId)
    }

    @Test
    fun `mudarStatus deve retornar not found quando atendimento não existe`() {
        val idAtendimento = 1
        val novoStatusId = 2
        `when`(atendimentoService.mudarStatus(idAtendimento, novoStatusId)).thenReturn(false)

        val response = controller.mudarStatus(idAtendimento, novoStatusId)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        verify(atendimentoService).mudarStatus(idAtendimento, novoStatusId)
    }

    @Test
    fun `editarAtendimento deve retornar ok quando sucesso`() {
        val id = 1
        val atendimento = Atendimento()
        val atendimentoEditado = Atendimento()
        `when`(atendimentoService.editarAtendimento(id, atendimento)).thenReturn(atendimentoEditado)

        val response = controller.editarAtendimento(id, atendimento)

        assertEquals(200, response.statusCode.value())
        assertEquals(atendimentoEditado.toDTO(), response.body)
        verify(atendimentoService).editarAtendimento(id, atendimento)
    }

    @Test
    fun `editarAtendimento deve retornar not found quando atendimento não existe`() {
        val id = 1
        val atendimento = Atendimento()
        `when`(atendimentoService.editarAtendimento(id, atendimento)).thenReturn(null)

        val response = controller.editarAtendimento(id, atendimento)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNull(response.body)
        verify(atendimentoService).editarAtendimento(id, atendimento)
    }
}