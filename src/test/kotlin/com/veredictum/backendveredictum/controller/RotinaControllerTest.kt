package com.veredictum.backendveredictum.controller

import com.veredictum.backendveredictum.entity.Rotina
import com.veredictum.backendveredictum.repository.RotinaRepository
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
class RotinaControllerTest {

    @Mock
    lateinit var repository: RotinaRepository

    @InjectMocks
    lateinit var controller: RotinaController

    @Test
    fun listarTodas() {
        val rotina = Rotina(idRotina = 1, nomeRotina = "Teste")
        given(repository.findAll()).willReturn(listOf(rotina))
        val response = controller.listarTodas()
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(1, response.body?.size)
    }

    @Test
    fun listarTodasVazio() {
        given(repository.findAll()).willReturn(emptyList())
        val response = controller.listarTodas()
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
    }

    @Test
    fun buscarPorId() {
        val rotina = Rotina(idRotina = 1, nomeRotina = "Teste")
        given(repository.findById(1)).willReturn(Optional.of(rotina))
        val response = controller.buscarPorId(1)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(1, response.body?.idRotina)
    }

    @Test
    fun buscarPorIdNaoEncontrado() {
        given(repository.findById(1)).willReturn(Optional.empty())
        val response = controller.buscarPorId(1)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun criar() {
        val rotina = Rotina(idRotina = 1, nomeRotina = "Nova")
        given(repository.save(any(Rotina::class.java))).willReturn(rotina)
        val response = controller.criar(rotina)
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(1, response.body?.idRotina)
    }

    @Test
    fun atualizar() {
        val rotinaExistente = Rotina(idRotina = 1, nomeRotina = "Antiga")
        val rotinaAtualizada = Rotina(idRotina = 1, nomeRotina = "Atualizada")
        given(repository.findById(1)).willReturn(Optional.of(rotinaExistente))
        given(repository.save(any(Rotina::class.java))).willReturn(rotinaAtualizada)
        val response = controller.atualizar(1, rotinaAtualizada)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("Atualizada", response.body?.nomeRotina)
    }

    @Test
    fun atualizarNaoEncontrado() {
        val rotinaAtualizada = Rotina(idRotina = 1, nomeRotina = "Atualizada")
        given(repository.findById(1)).willReturn(Optional.empty())
        val response = controller.atualizar(1, rotinaAtualizada)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
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

    @Test
    fun ativar() {
        val rotina = Rotina(idRotina = 1, isAtivo = false)
        val rotinaAtiva = rotina.copy(isAtivo = true)
        given(repository.findById(1)).willReturn(Optional.of(rotina))
        given(repository.save(any(Rotina::class.java))).willReturn(rotinaAtiva)
        val response = controller.ativar(1)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertTrue(response.body?.isAtivo == true)
    }

    @Test
    fun ativarNaoEncontrado() {
        given(repository.findById(1)).willReturn(Optional.empty())
        val response = controller.ativar(1)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun inativar() {
        val rotina = Rotina(idRotina = 1, isAtivo = true)
        val rotinaInativa = rotina.copy(isAtivo = false)
        given(repository.findById(1)).willReturn(Optional.of(rotina))
        given(repository.save(any(Rotina::class.java))).willReturn(rotinaInativa)
        val response = controller.inativar(1)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertFalse(response.body?.isAtivo == true)
    }

    @Test
    fun inativarNaoEncontrado() {
        given(repository.findById(1)).willReturn(Optional.empty())
        val response = controller.inativar(1)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }
}