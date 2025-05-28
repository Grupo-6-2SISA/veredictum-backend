package com.veredictum.backendveredictum.controller

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
import java.time.LocalDate
import java.util.*

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
        // Arrange
        val usuario = Usuario(idUsuario = 1)
        val novaConta = Conta(
            usuario = usuario,
            etiqueta = "Teste",
            valor = 100.0,
            dataVencimento = LocalDate.now().plusDays(30),
            isPago = false
        )

        `when`(contaService.save(any())).thenReturn(novaConta.copy(idConta = 1))

        // Act
        val response = controller.criarConta(novaConta)

        // Assert
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertNotNull(response.body)
        assertEquals(1, response.body?.idConta)
    }

    @Test
    fun `buscarContaPorId deve retornar conta quando encontrada`() {
        // Arrange
        val id = 1
        val conta = Conta(
            idConta = id,
            usuario = Usuario(idUsuario = 1),
            etiqueta = "Teste",
            valor = 100.0,
            dataVencimento = LocalDate.now(),
            isPago = false
        )

        `when`(contaService.findById(id)).thenReturn(Optional.of(conta))

        // Act
        val response = controller.buscarContaPorId(id)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(id, response.body?.idConta)
    }

    @Test
    fun `listarTodasContas deve retornar lista ordenada`() {
        // Arrange
        val contas = listOf(
            Conta(
                idConta = 1,
                usuario = Usuario(idUsuario = 1),
                dataCriacao = LocalDate.now(),
                etiqueta = "Conta 1",
                valor = 100.0,
                dataVencimento = LocalDate.now().plusDays(30),
                urlNuvem = null,
                descricao = "Descrição da conta 1",
                isPago = true
            ),
            Conta(
                idConta = 2,
                usuario = Usuario(idUsuario = 1),
                dataCriacao = LocalDate.now(),
                etiqueta = "Conta 2",
                valor = 200.0,
                dataVencimento = LocalDate.now().plusDays(15),
                urlNuvem = null,
                descricao = "Descrição da conta 2",
                isPago = false
            )
        )

        `when`(contaService.findAll(any<Sort>())).thenReturn(contas)

        // Act
        val response = controller.listarTodasContas()

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(2, response.body?.size)
        assertFalse(response.body?.first()?.isPago ?: true) // Primeiro item não pago
    }

    @Test
    fun `listarContasPorUsuario deve retornar contas do usuario`() {
        // Arrange
        val usuarioId = 1
        val contas = listOf(
            Conta(
                idConta = 1,
                usuario = Usuario(idUsuario = usuarioId),
                dataCriacao = LocalDate.now(),
                etiqueta = "Conta Usuário 1",
                valor = 150.0,
                dataVencimento = LocalDate.now().plusDays(10),
                urlNuvem = null,
                descricao = "Descrição conta 1",
                isPago = false
            ),
            Conta(
                idConta = 2,
                usuario = Usuario(idUsuario = usuarioId),
                dataCriacao = LocalDate.now(),
                etiqueta = "Conta Usuário 2",
                valor = 250.0,
                dataVencimento = LocalDate.now().plusDays(20),
                urlNuvem = null,
                descricao = "Descrição conta 2",
                isPago = false
            )
        )

        `when`(contaService.findByUsuarioId(usuarioId)).thenReturn(contas)

        // Act
        val response = controller.listarContasPorUsuario(usuarioId)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(2, response.body?.size)
    }

    @Test
    fun `listarContasPorPago deve retornar contas por status de pagamento`() {
        // Arrange
        val isPago = false
        val contas = listOf(
            Conta(
                idConta = 1,
                usuario = Usuario(idUsuario = 1),
                dataCriacao = LocalDate.now(),
                etiqueta = "Conta Não Paga 1",
                valor = 300.0,
                dataVencimento = LocalDate.now().plusDays(5),
                urlNuvem = null,
                descricao = "Descrição conta não paga 1",
                isPago = false
            ),
            Conta(
                idConta = 2,
                usuario = Usuario(idUsuario = 1),
                dataCriacao = LocalDate.now(),
                etiqueta = "Conta Não Paga 2",
                valor = 400.0,
                dataVencimento = LocalDate.now().plusDays(15),
                urlNuvem = null,
                descricao = "Descrição conta não paga 2",
                isPago = false
            )
        )

        `when`(contaService.findByIsPago(isPago)).thenReturn(contas)

        // Act
        val response = controller.listarContasPorPago(isPago)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertTrue(response.body?.all { !it.isPago } ?: false)
    }

    @Test
    fun `atualizarParcialConta deve atualizar campos específicos`() {
        // Arrange
        val id = 1
        val updates = mapOf(
            "etiqueta" to "Nova Etiqueta",
            "valor" to 150.0,
            "isPago" to true
        )

        val contaAtualizada = Conta(
            idConta = id,
            usuario = Usuario(idUsuario = 1),
            dataCriacao = LocalDate.now(),
            etiqueta = "Nova Etiqueta",
            valor = 150.0,
            dataVencimento = LocalDate.now().plusDays(30),
            urlNuvem = null,
            descricao = "Descrição atualizada",
            isPago = true
        )

        `when`(contaService.partialUpdate(eq(id), any())).thenReturn(contaAtualizada)

        // Act
        val response = controller.atualizarParcialConta(id, updates)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals("Nova Etiqueta", response.body?.etiqueta)
        assertEquals(150.0, response.body?.valor)
        assertTrue(response.body?.isPago ?: false)
    }

    @Test
    fun `atualizarConta deve atualizar conta completa`() {
        // Arrange
        val id = 1
        val contaAtualizada = Conta(
            idConta = id,
            usuario = Usuario(idUsuario = 1),
            dataCriacao = LocalDate.now(),
            etiqueta = "Atualizada",
            valor = 200.0,
            dataVencimento = LocalDate.now().plusDays(30),
            urlNuvem = null,
            descricao = "Descrição da conta atualizada",
            isPago = true
        )

        `when`(contaService.save(any())).thenReturn(contaAtualizada)

        // Act
        val response = controller.atualizarConta(id, contaAtualizada)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals("Atualizada", response.body?.etiqueta)
        assertEquals(200.0, response.body?.valor)
        assertTrue(response.body?.isPago ?: false)
    }

    @Test
    fun `excluirConta deve remover conta existente`() {
        // Arrange
        val id = 1
        `when`(contaService.existsById(id)).thenReturn(true)
        doNothing().`when`(contaService).deleteById(id)

        // Act
        val response = controller.excluirConta(id)

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        verify(contaService).deleteById(id)
    }
}