package com.veredictum.backendveredictum.controller

import com.veredictum.backendveredictum.dto.AtendimentoDTO
import com.veredictum.backendveredictum.entity.Atendimento
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
    fun getTodos() {
    }

    @Test
    fun listarPorCliente() {
    }

    @Test
    fun listarPorUsuario() {
    }

    @Test
    fun listarPorStatus() {
    }

    @Test
    fun listarAtendimentosOrdenados() {
    }

    @Test
    fun criarAtendimento() {
    }

    @Test
    fun mudarStatus() {
    }

    @Test
    fun editarAtendimento() {
    }

}