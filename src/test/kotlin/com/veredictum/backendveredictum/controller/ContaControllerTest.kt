package com.veredictum.backendveredictum.controller

import com.veredictum.backendveredictum.dto.ContaDTO
import com.veredictum.backendveredictum.entity.Conta
import com.veredictum.backendveredictum.entity.Usuario
import com.veredictum.backendveredictum.services.ContaService
import com.veredictum.backendveredictum.services.UsuarioService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.util.*

import org.mockito.ArgumentMatchers

// Funções auxiliares para uso correto dos matchers do Mockito em Kotlin
fun <T> any(): T = ArgumentMatchers.any<T>()
fun <T> eq(value: T): T = ArgumentMatchers.eq(value)

@ExtendWith(MockitoExtension::class)
class ContaControllerTest {

    @Mock
    private lateinit var contaService: ContaService

    @Mock
    private lateinit var usuarioService: UsuarioService

    @InjectMocks
    private lateinit var controller: ContaController

    @Test
    fun `criarConta deve criar conta com sucesso`() {
        val usuario = Usuario(idUsuario = 1)
        val contaDTO = ContaDTO(
            idConta = null,
            fkUsuario = 1,
            dataCriacao = LocalDate.now(),
            etiqueta = "Conta Teste",
            valor = 100.0,
            dataVencimento = LocalDate.now().plusDays(10),
            urlNuvem = null,
            descricao = null,
            isPago = false
        )
        val contaSalva = Conta(
            idConta = 1,
            usuario = usuario,
            dataCriacao = contaDTO.dataCriacao,
            etiqueta = contaDTO.etiqueta,
            valor = contaDTO.valor,
            dataVencimento = contaDTO.dataVencimento,
            urlNuvem = contaDTO.urlNuvem,
            descricao = contaDTO.descricao,
            isPago = contaDTO.isPago
        )

        `when`(usuarioService.findById(eq(1))).thenReturn(Optional.of(usuario))
        `when`(contaService.save(any())).thenReturn(contaSalva)

        val response = controller.criarConta(contaDTO)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertNotNull(response.body)
        assertEquals(contaSalva.idConta, response.body?.idConta)
        verify(usuarioService).findById(eq(1))
        verify(contaService).save(any())
    }

    @Test
    fun `criarConta deve retornar erro quando servico falha`() {
        val contaDTO = ContaDTO(
            idConta = null,
            fkUsuario = 1,
            dataCriacao = LocalDate.now(),
            etiqueta = "Conta Teste",
            valor = 100.0,
            dataVencimento = LocalDate.now().plusDays(10),
            urlNuvem = null,
            descricao = null,
            isPago = false
        )
        val usuario = Usuario(idUsuario = 1)
        val mensagemErro = "Erro ao salvar"
        `when`(usuarioService.findById(eq(1))).thenReturn(Optional.of(usuario))
        `when`(contaService.save(any())).thenThrow(ResponseStatusException(HttpStatus.BAD_REQUEST, mensagemErro))

        val exception = assertThrows(ResponseStatusException::class.java) {
            controller.criarConta(contaDTO)
        }
        assertEquals(HttpStatus.BAD_REQUEST, exception.statusCode)
        assertEquals(mensagemErro, exception.reason)
        verify(usuarioService).findById(eq(1))
        verify(contaService).save(any())
    }

    @Test
    fun `buscarContaPorId deve retornar conta quando encontrada`() {
        val idConta = 1
        val contaEsperada = Conta(
            idConta = idConta,
            usuario = Usuario(idUsuario = 1),
            etiqueta = "Conta Encontrada",
            valor = 200.0,
            dataVencimento = LocalDate.now().plusDays(5),
            isPago = false
        )
        `when`(contaService.findById(idConta)).thenReturn(Optional.of(contaEsperada))

        val response = controller.buscarContaPorId(idConta)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(contaEsperada.idConta, response.body?.idConta)
        verify(contaService).findById(idConta)
    }

    @Test
    fun `buscarContaPorId deve retornar NOT FOUND quando conta nao encontrada`() {
        val idConta = 1
        `when`(contaService.findById(idConta)).thenReturn(Optional.empty())

        val response = controller.buscarContaPorId(idConta)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNull(response.body)
        verify(contaService).findById(idConta)
    }

    @Test
    fun `listarTodasContas deve retornar lista de contas`() {
        val contasEsperadas = listOf(
            Conta(idConta = 1, usuario = Usuario(idUsuario = 1), etiqueta = "Conta A", valor = 10.0, dataVencimento = LocalDate.now(), isPago = false),
            Conta(idConta = 2, usuario = Usuario(idUsuario = 1), etiqueta = "Conta B", valor = 20.0, dataVencimento = LocalDate.now().plusDays(1), isPago = true)
        )
        `when`(contaService.findAll(any<Sort>())).thenReturn(contasEsperadas)

        val response = controller.listarTodasContas()

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(2, response.body?.size)
        verify(contaService).findAll(any<Sort>())
    }

    @Test
    fun `listarTodasContas deve retornar NO CONTENT quando lista vazia`() {
        `when`(contaService.findAll(any<Sort>())).thenReturn(emptyList())

        val response = controller.listarTodasContas()

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
        verify(contaService).findAll(any<Sort>())
    }

    @Test
    fun `listarContasPorUsuario deve retornar lista de contas do usuario`() {
        val idUsuario = 1
        val contasEsperadas = listOf(
            Conta(idConta = 1, usuario = Usuario(idUsuario = idUsuario), etiqueta = "Conta Usuario A", valor = 30.0, dataVencimento = LocalDate.now()),
            Conta(idConta = 3, usuario = Usuario(idUsuario = idUsuario), etiqueta = "Conta Usuario C", valor = 40.0, dataVencimento = LocalDate.now())
        )
        `when`(contaService.findByUsuarioId(idUsuario)).thenReturn(contasEsperadas)

        val response = controller.listarContasPorUsuario(idUsuario)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(2, response.body?.size)
        assertTrue(response.body?.all { it.usuario?.idUsuario == idUsuario } ?: false)
        verify(contaService).findByUsuarioId(idUsuario)
    }

    @Test
    fun `listarContasPorUsuario deve retornar NO CONTENT quando usuario nao tem contas`() {
        val idUsuario = 1
        `when`(contaService.findByUsuarioId(idUsuario)).thenReturn(emptyList())

        val response = controller.listarContasPorUsuario(idUsuario)

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
        verify(contaService).findByUsuarioId(idUsuario)
    }

    @Test
    fun `listarContasPorPago deve retornar lista de contas pagas`() {
        val isPago = true
        val contasEsperadas = listOf(
            Conta(idConta = 2, usuario = Usuario(idUsuario = 1), etiqueta = "Conta Paga B", valor = 20.0, dataVencimento = LocalDate.now(), isPago = true)
        )
        `when`(contaService.findByIsPago(isPago)).thenReturn(contasEsperadas)

        val response = controller.listarContasPorPago(isPago)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(1, response.body?.size)
        assertTrue(response.body?.all { it.isPago == isPago } ?: false)
        verify(contaService).findByIsPago(isPago)
    }

    @Test
    fun `listarContasPorPago deve retornar NO CONTENT quando nao ha contas com status especifico`() {
        val isPago = false
        `when`(contaService.findByIsPago(isPago)).thenReturn(emptyList())

        val response = controller.listarContasPorPago(isPago)

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
        verify(contaService).findByIsPago(isPago)
    }

    @Test
    fun `atualizarParcialConta deve atualizar conta com sucesso`() {
        val idConta = 1
        val updates = mapOf("etiqueta" to "Etiqueta Atualizada", "isPago" to true)
        val contaAtualizada = Conta(
            idConta = idConta,
            usuario = Usuario(idUsuario = 1),
            etiqueta = "Etiqueta Atualizada",
            valor = 100.0,
            dataVencimento = LocalDate.now(),
            isPago = true
        )
        `when`(contaService.partialUpdate(eq(idConta), eq(updates))).thenReturn(contaAtualizada)

        val response = controller.atualizarParcialConta(idConta, updates)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals("Etiqueta Atualizada", response.body?.etiqueta)
        assertTrue(response.body?.isPago ?: false)
        verify(contaService).partialUpdate(eq(idConta), eq(updates))
    }

    @Test
    fun `atualizarParcialConta deve retornar NOT FOUND quando conta nao existe`() {
        val idConta = 1
        val updates = mapOf("etiqueta" to "Nova Etiqueta")
        `when`(contaService.partialUpdate(eq(idConta), eq(updates))).thenThrow(NoSuchElementException("Conta não encontrada"))

        val response = controller.atualizarParcialConta(idConta, updates)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNull(response.body)
        verify(contaService).partialUpdate(eq(idConta), eq(updates))
    }

    @Test
    fun `atualizarParcialConta deve retornar BAD REQUEST para dados invalidos`() {
        val idConta = 1
        val updates = mapOf("valor" to "textoInvalido")
        val mensagemErro = "Valor inválido para atualização"
        `when`(contaService.partialUpdate(eq(idConta), eq(updates))).thenThrow(IllegalArgumentException(mensagemErro))

        val exception = assertThrows(ResponseStatusException::class.java) {
            controller.atualizarParcialConta(idConta, updates)
        }
        assertEquals(HttpStatus.BAD_REQUEST, exception.statusCode)
        assertEquals(mensagemErro, exception.reason)
        verify(contaService).partialUpdate(eq(idConta), eq(updates))
    }

    @Test
    fun `atualizarConta deve atualizar conta com sucesso`() {
        val idConta = 1
        val usuario = Usuario(idUsuario = 1)
        val contaDTO = ContaDTO(
            idConta = idConta,
            fkUsuario = 1,
            dataCriacao = LocalDate.now(),
            etiqueta = "Conta Totalmente Atualizada",
            valor = 150.0,
            dataVencimento = LocalDate.now().plusMonths(1),
            urlNuvem = null,
            descricao = null,
            isPago = true
        )
        val contaAtualizadaEsperada = Conta(
            idConta = idConta,
            usuario = usuario,
            dataCriacao = contaDTO.dataCriacao,
            etiqueta = contaDTO.etiqueta,
            valor = contaDTO.valor,
            dataVencimento = contaDTO.dataVencimento,
            urlNuvem = contaDTO.urlNuvem,
            descricao = contaDTO.descricao,
            isPago = contaDTO.isPago
        )

        `when`(contaService.existsById(idConta)).thenReturn(true)
        `when`(usuarioService.findById(eq(1))).thenReturn(Optional.of(usuario))
        `when`(contaService.save(any())).thenReturn(contaAtualizadaEsperada)

        val response = controller.atualizarConta(idConta, contaDTO)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(contaAtualizadaEsperada.idConta, response.body?.idConta)
        assertEquals(contaAtualizadaEsperada.etiqueta, response.body?.etiqueta)
        verify(contaService).existsById(idConta)
        verify(usuarioService).findById(eq(1))
        verify(contaService).save(any())
    }

    @Test
    fun `atualizarConta deve retornar NOT FOUND quando conta nao existe`() {
        val idConta = 1
        val contaDTO = ContaDTO(
            idConta = idConta,
            fkUsuario = 1,
            dataCriacao = LocalDate.now(),
            etiqueta = "Conta Inexistente",
            valor = 50.0,
            dataVencimento = LocalDate.now(),
            urlNuvem = null,
            descricao = null,
            isPago = false
        )
        `when`(contaService.existsById(idConta)).thenReturn(false)

        val response = controller.atualizarConta(idConta, contaDTO)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNull(response.body)
        verify(contaService).existsById(idConta)
        verify(contaService, never()).save(any())
    }

    @Test
    fun `atualizarConta deve retornar erro quando servico falha ao salvar`() {
        val idConta = 1
        val usuario = Usuario(idUsuario = 1)
        val contaDTO = ContaDTO(
            idConta = idConta,
            fkUsuario = 1,
            dataCriacao = LocalDate.now(),
            etiqueta = "Conta com Erro",
            valor = 10.0,
            dataVencimento = LocalDate.now(),
            urlNuvem = null,
            descricao = null,
            isPago = false
        )
        val mensagemErro = "Erro ao atualizar conta no serviço"
        `when`(contaService.existsById(idConta)).thenReturn(true)
        `when`(usuarioService.findById(eq(1))).thenReturn(Optional.of(usuario))
        `when`(contaService.save(any())).thenThrow(ResponseStatusException(HttpStatus.BAD_REQUEST, mensagemErro))

        val exception = assertThrows(ResponseStatusException::class.java) {
            controller.atualizarConta(idConta, contaDTO)
        }
        assertEquals(HttpStatus.BAD_REQUEST, exception.statusCode)
        assertEquals(mensagemErro, exception.reason)
        verify(contaService).existsById(idConta)
        verify(usuarioService).findById(eq(1))
        verify(contaService).save(any())
    }

    @Test
    fun `excluirConta deve excluir conta com sucesso`() {
        val idConta = 1
        `when`(contaService.existsById(idConta)).thenReturn(true)
        doNothing().`when`(contaService).deleteById(idConta)

        val response = controller.excluirConta(idConta)

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
        verify(contaService).existsById(idConta)
        verify(contaService).deleteById(idConta)
    }

    @Test
    fun `excluirConta deve retornar NOT FOUND quando conta nao existe`() {
        val idConta = 1
        `when`(contaService.existsById(idConta)).thenReturn(false)

        val response = controller.excluirConta(idConta)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNull(response.body)
        verify(contaService).existsById(idConta)
        verify(contaService, never()).deleteById(idConta)
    }
}