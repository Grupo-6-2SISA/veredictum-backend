package com.veredictum.backendveredictum.controller

import com.veredictum.backendveredictum.entity.HistoricoStatusAgendamento
import com.veredictum.backendveredictum.entity.StatusAgendamento
import com.veredictum.backendveredictum.repository.HistoricoStatusAgendamentoRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import java.time.LocalDateTime
import java.util.*

class HistoricoStatusAgendamentoControllerTest {

    private lateinit var repository: HistoricoStatusAgendamentoRepository
    private lateinit var controller: HistoricoStatusAgendamentoController

    @BeforeEach
    fun setup() {
        repository = mock(HistoricoStatusAgendamentoRepository::class.java)
        controller = HistoricoStatusAgendamentoController(repository)
    }

    @Test
    fun `buscarTodos deve retornar lista quando existem registros`() {
        // Arrange
        val historico = HistoricoStatusAgendamento(
            idHistoricoAgendamento = null,
            atendimento = null,
            nota = null,
            conta = null,
            statusAgendamento = StatusAgendamento(),
            dataHoraAlteracao = LocalDateTime.now()
        )
        val historicos = listOf(historico)
        `when`(repository.findAll()).thenReturn(historicos)

        // Act
        val response = controller.buscarTodos()

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(historicos, response.body)
        verify(repository).findAll()
    }

    @Test
    fun `buscarTodos deve retornar no content quando não existem registros`() {
        // Arrange
        `when`(repository.findAll()).thenReturn(emptyList())

        // Act
        val response = controller.buscarTodos()

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
        verify(repository).findAll()
    }

    @Test
    fun `buscarPorId deve retornar historico quando existe`() {
        // Arrange
        val id = 1
        val historico = HistoricoStatusAgendamento()
        historico.idHistoricoAgendamento = id
        `when`(repository.findById(id)).thenReturn(Optional.of(historico))

        // Act
        val response = controller.buscarPorId(id)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(historico, response.body)
        verify(repository).findById(id)
    }

    @Test
    fun `buscarPorId deve retornar not found quando não existe`() {
        // Arrange
        val id = 1
        `when`(repository.findById(id)).thenReturn(Optional.empty())

        // Act
        val response = controller.buscarPorId(id)

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNull(response.body)
        verify(repository).findById(id)
    }

    @Test
    fun `criar deve retornar created com historico`() {
        // Arrange
        val novoHistorico = HistoricoStatusAgendamento()  // Usando o construtor secundário
        `when`(repository.save(any())).thenReturn(novoHistorico)

        // Act
        val response = controller.criar(novoHistorico)

        // Assert
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(novoHistorico, response.body)
        verify(repository).save(novoHistorico)
    }

    @Test
    fun `atualizar deve retornar historico atualizado quando existe`() {
        // Arrange
        val id = 1
        val historicoExistente = HistoricoStatusAgendamento()
        val historicoAtualizado = HistoricoStatusAgendamento()
        `when`(repository.findById(id)).thenReturn(Optional.of(historicoExistente))
        `when`(repository.save(any())).thenReturn(historicoAtualizado)

        // Act
        val response = controller.atualizar(id, historicoAtualizado)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(historicoAtualizado, response.body)
        verify(repository).findById(id)
        verify(repository).save(any())
    }

    @Test
    fun `atualizar deve retornar not found quando não existe`() {
        // Arrange
        val id = 1
        val historicoAtualizado = HistoricoStatusAgendamento()
        `when`(repository.findById(id)).thenReturn(Optional.empty())

        // Act
        val response = controller.atualizar(id, historicoAtualizado)

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNull(response.body)
        verify(repository).findById(id)
        verify(repository, never()).save(any())
    }

    @Test
    fun `excluir deve retornar no content quando existe`() {
        // Arrange
        val id = 1
        val historico = HistoricoStatusAgendamento()
        `when`(repository.findById(id)).thenReturn(Optional.of(historico))
        doNothing().`when`(repository).delete(historico)

        // Act
        val response = controller.excluir(id)

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
        verify(repository).findById(id)
        verify(repository).delete(historico)
    }

    @Test
    fun `excluir deve retornar not found quando não existe`() {
        // Arrange
        val id = 1
        `when`(repository.findById(id)).thenReturn(Optional.empty())

        // Act
        val response = controller.excluir(id)

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNull(response.body)
        verify(repository).findById(id)
        verify(repository, never()).delete(any())
    }
}