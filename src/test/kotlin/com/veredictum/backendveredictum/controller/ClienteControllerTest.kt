package com.veredictum.backendveredictum.controller

import com.veredictum.backendveredictum.dto.ClienteDTO
import com.veredictum.backendveredictum.entity.Cliente
import com.veredictum.backendveredictum.repository.ClienteRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import java.time.LocalDate
import java.util.*

@ExtendWith(MockitoExtension::class)
class ClienteControllerTest {

    @Mock
    private lateinit var repository: ClienteRepository

    @InjectMocks
    private lateinit var controller: ClienteController

    @Test
    fun `buscarTodos deve retornar lista de clientes quando existirem`() {
        // Arrange
        val cliente = Cliente(
            idCliente = 1,
            nome = "Teste",
            email = "teste@email.com",
            rg = "123456",
            dataNascimento = LocalDate.now().minusYears(20),
            dataInicio = LocalDate.now(),
            isAtivo = true
        )

        `when`(repository.findAllOrderByIsAtivo()).thenReturn(listOf(cliente))

        // Act
        val response = controller.buscarTodos()

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(1, response.body?.size)
    }

    @Test
    fun `buscarTodos deve retornar no content quando não houver clientes`() {
        // Arrange
        `when`(repository.findAllOrderByIsAtivo()).thenReturn(emptyList())

        // Act
        val response = controller.buscarTodos()

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
    }

    @Test
    fun `buscarPorId deve retornar cliente quando encontrado`() {
        // Arrange
        val cliente = Cliente(
            idCliente = 1,
            nome = "Teste",
            email = "teste@email.com",
            rg = "123456",
            dataNascimento = LocalDate.now().minusYears(20),
            dataInicio = LocalDate.now(),
            isAtivo = true
        )

        `when`(repository.findById(1)).thenReturn(Optional.of(cliente))

        // Act
        val response = controller.buscarPorId(1)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
    }

    @Test
    fun `cadastrarInativo deve criar novo cliente com sucesso`() {
        // Arrange
        val clienteDTO = ClienteDTO(
            idCliente = 0,
            nome = "Novo Cliente",
            fkIndicador = null,
            email = "novo@email.com",
            rg = "123456",
            cpf = "12345678901",
            cnpj = null,
            dataNascimento = LocalDate.now().minusYears(25),
            dataInicio = LocalDate.now(),
            endereco = "Endereço Teste",
            cep = "12345678",
            descricao = "Descrição teste",
            inscricaoEstadual = null,
            isProBono = false,
            isAtivo = false,
            isJuridico = false
        )

        val clienteSalvo = Cliente(
            idCliente = 1,
            nome = clienteDTO.nome,
            email = clienteDTO.email,
            rg = clienteDTO.rg,
            dataNascimento = clienteDTO.dataNascimento,
            dataInicio = clienteDTO.dataInicio,
            isAtivo = false
        )

        `when`(repository.save(any())).thenReturn(clienteSalvo)

        // Act
        val response = controller.cadastrarInativo(clienteDTO)

        // Assert
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertNotNull(response.body)
    }

    @Test
    fun `atualizar deve atualizar cliente existente com sucesso`() {
        // Arrange
        val id = 1
        val clienteAtualizado = Cliente(
            idCliente = id,
            nome = "Cliente Atualizado",
            email = "atualizado@email.com",
            rg = "654321",
            dataNascimento = LocalDate.now().minusYears(30),
            dataInicio = LocalDate.now(),
            isAtivo = true
        )

        `when`(repository.findById(id)).thenReturn(Optional.of(clienteAtualizado))
        `when`(repository.save(any())).thenReturn(clienteAtualizado)

        // Act
        val response = controller.atualizar(id, clienteAtualizado)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
    }

    @Test
    fun `atualizarParcial deve atualizar campos específicos do cliente`() {
        // Arrange
        val id = 1
        val clienteExistente = Cliente(
            idCliente = id,
            nome = "Cliente Original",
            email = "original@email.com",
            rg = "123456",
            dataNascimento = LocalDate.now().minusYears(25),
            dataInicio = LocalDate.now(),
            isAtivo = true
        )

        val atualizacoes = mapOf(
            "nome" to "Nome Atualizado",
            "email" to "atualizado@email.com"
        )

        `when`(repository.findById(id)).thenReturn(Optional.of(clienteExistente))
        `when`(repository.save(any())).thenReturn(clienteExistente)

        // Act
        val response = controller.atualizarParcial(id, atualizacoes)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
    }

    @Test
    fun `inativar deve desativar cliente com sucesso`() {
        // Arrange
        val id = 1
        val cliente = Cliente(
            idCliente = id,
            nome = "Cliente",
            email = "cliente@email.com",
            rg = "123456",
            dataNascimento = LocalDate.now().minusYears(25),
            dataInicio = LocalDate.now(),
            isAtivo = true
        )

        `when`(repository.findById(id)).thenReturn(Optional.of(cliente))
        `when`(repository.save(any())).thenReturn(cliente.apply { isAtivo = false })

        // Act
        val response = controller.inativar(id)

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        verify(repository).save(any())
    }

    @Test
    fun `ativar deve ativar cliente com sucesso`() {
        // Arrange
        val id = 1
        val cliente = Cliente(
            idCliente = id,
            nome = "Cliente",
            email = "cliente@email.com",
            rg = "123456",
            dataNascimento = LocalDate.now().minusYears(25),
            dataInicio = LocalDate.now(),
            isAtivo = false
        )

        `when`(repository.findById(id)).thenReturn(Optional.of(cliente))
        `when`(repository.save(any())).thenReturn(cliente.apply { isAtivo = true })

        // Act
        val response = controller.ativar(id)

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        verify(repository).save(any())
    }

    @Test
    fun `getRepository deve retornar o repositório injetado`() {
        // Act
        val result = controller.repository

        // Assert
        assertNotNull(result)
        assertTrue(result is ClienteRepository)
    }
}