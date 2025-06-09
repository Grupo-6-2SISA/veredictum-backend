package com.veredictum.backendveredictum.controller

import com.veredictum.backendveredictum.entity.LogExecucaoRotina
import com.veredictum.backendveredictum.enums.TipoStatusLogRotina
import com.veredictum.backendveredictum.repository.LogExecucaoRotinaRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import java.util.*

@ExtendWith(MockitoExtension::class)
class LogExecucaoRotinaControllerTest {

    @Mock
    lateinit var repository: LogExecucaoRotinaRepository

    @InjectMocks
    lateinit var controller: LogExecucaoRotinaController

    @Test
    fun listarTodos() {
        val log = LogExecucaoRotina(idLogExecucaoRotina = 1)
        given(repository.findAll()).willReturn(listOf(log))
        val response = controller.listarTodos()
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(1, response.body?.size)
    }

    @Test
    fun listarTodosVazio() {
        given(repository.findAll()).willReturn(emptyList())
        val response = controller.listarTodos()
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
    }

    @Test
    fun buscarPorId() {
        val log = LogExecucaoRotina(idLogExecucaoRotina = 1)
        given(repository.findById(1)).willReturn(Optional.of(log))
        val response = controller.buscarPorId(1)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(1, response.body?.idLogExecucaoRotina)
    }

    @Test
    fun buscarPorIdNaoEncontrado() {
        given(repository.findById(1)).willReturn(Optional.empty())
        val response = controller.buscarPorId(1)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun criar() {
        val log = LogExecucaoRotina(idLogExecucaoRotina = 1)
        given(repository.save(any(LogExecucaoRotina::class.java))).willReturn(log)
        val response = controller.criar(log)
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(1, response.body?.idLogExecucaoRotina)
    }

    @Test
    fun atualizarParaEmAndamento() {
        val log = LogExecucaoRotina(idLogExecucaoRotina = 1, statusExecucao = "CRIADO")
        given(repository.findById(1)).willReturn(Optional.of(log))
        given(repository.save(any(LogExecucaoRotina::class.java))).willReturn(log.copy(statusExecucao = TipoStatusLogRotina.EM_ANDAMENTO.tipo))
        val response = controller.atualizarParaEmAndamento(1)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(TipoStatusLogRotina.EM_ANDAMENTO.tipo, response.body?.statusExecucao)
    }

    @Test
    fun atualizarParaConcluida() {
        val log = LogExecucaoRotina(idLogExecucaoRotina = 1, statusExecucao = "CRIADO")
        given(repository.findById(1)).willReturn(Optional.of(log))
        given(repository.save(any(LogExecucaoRotina::class.java))).willReturn(log.copy(statusExecucao = TipoStatusLogRotina.CONCLUIDA.tipo))
        val response = controller.atualizarParaConcluida(1)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(TipoStatusLogRotina.CONCLUIDA.tipo, response.body?.statusExecucao)
    }

    @Test
    fun atualizarParaCancelada() {
        val log = LogExecucaoRotina(idLogExecucaoRotina = 1, statusExecucao = "CRIADO")
        given(repository.findById(1)).willReturn(Optional.of(log))
        given(repository.save(any(LogExecucaoRotina::class.java))).willReturn(log.copy(statusExecucao = TipoStatusLogRotina.CANCELADA.tipo))
        val response = controller.atualizarParaCancelada(1)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(TipoStatusLogRotina.CANCELADA.tipo, response.body?.statusExecucao)
    }

    @Test
    fun deletar() {
        given(repository.existsById(1)).willReturn(true)
        willDoNothing().given(repository).deleteById(1)
        val response = controller.deletar(1)
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }

    @Test
    fun deletarNaoEncontrado() {
        given(repository.existsById(1)).willReturn(false)
        val response = controller.deletar(1)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }
}