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

    private fun clienteCompleto(
        id: Int? = 1,
        nome: String = "Teste",
        email: String = "teste@email.com",
        rg: String = "123456",
        cpf: String = "12345678901",
        cnpj: String? = null,
        telefone: String = "11999999999",
        dataNascimento: LocalDate = LocalDate.now().minusYears(20),
        dataInicio: LocalDate = LocalDate.now(),
        endereco: String = "Rua Teste",
        cep: String = "12345678",
        descricao: String = "Descrição",
        inscricaoEstadual: String = "123456789",
        isProBono: Boolean = false,
        isAtivo: Boolean = true,
        isJuridico: Boolean = false,
        indicador: Cliente? = null
    ) = Cliente(
        idCliente = id,
        indicador = indicador,
        nome = nome,
        email = email,
        rg = rg,
        cpf = cpf,
        cnpj = cnpj,
        telefone = telefone,
        dataNascimento = dataNascimento,
        dataInicio = dataInicio,
        endereco = endereco,
        cep = cep,
        descricao = descricao,
        inscricaoEstadual = inscricaoEstadual,
        isProBono = isProBono,
        isAtivo = isAtivo,
        isJuridico = isJuridico
    )

    @Test
    fun `buscarTodos deve retornar lista de clientes quando existirem`() {
        val cliente = clienteCompleto()
        `when`(repository.findAllOrderByIsAtivo()).thenReturn(listOf(cliente))
        val response = controller.buscarTodos()
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(1, response.body?.size)
    }

    @Test
    fun `buscarTodos deve retornar no content quando não houver clientes`() {
        `when`(repository.findAllOrderByIsAtivo()).thenReturn(emptyList())
        val response = controller.buscarTodos()
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
    }

    @Test
    fun `buscarPorId deve retornar cliente quando encontrado`() {
        val cliente = clienteCompleto()
        `when`(repository.findById(1)).thenReturn(Optional.of(cliente))
        val response = controller.buscarPorId(1)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
    }

    @Test
    fun `cadastrarInativo deve criar novo cliente com sucesso`() {
        val clienteDTO = ClienteDTO(
            idCliente = 0,
            nome = "Novo Cliente",
            fkIndicador = null,
            email = "novo@email.com",
            rg = "123456",
            cpf = "12345678901",
            cnpj = null,
            telefone = "11999999999",
            dataNascimento = LocalDate.now().minusYears(25),
            dataInicio = LocalDate.now(),
            endereco = "Endereço Teste",
            cep = "12345678",
            descricao = "Descrição teste",
            inscricaoEstadual = "123456789",
            isProBono = false,
            isAtivo = false,
            isJuridico = false
        )
        val clienteSalvo = clienteCompleto(
            id = 1,
            nome = clienteDTO.nome,
            email = clienteDTO.email,
            rg = clienteDTO.rg,
            cpf = clienteDTO.cpf ?: "",
            cnpj = clienteDTO.cnpj,
            telefone = clienteDTO.telefone ?: "11999999999",
            dataNascimento = clienteDTO.dataNascimento!!,
            dataInicio = clienteDTO.dataInicio!!,
            endereco = clienteDTO.endereco ?: "Endereço Teste",
            cep = clienteDTO.cep ?: "12345678",
            descricao = clienteDTO.descricao ?: "Descrição teste",
            inscricaoEstadual = clienteDTO.inscricaoEstadual ?: "123456789",
            isProBono = clienteDTO.isProBono ?: false,
            isAtivo = false,
            isJuridico = clienteDTO.isJuridico ?: false
        )
        `when`(repository.save(any())).thenReturn(clienteSalvo)
        val response = controller.cadastrarInativo(clienteDTO)
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertNotNull(response.body)
    }

    @Test
    fun `atualizar deve atualizar cliente existente com sucesso`() {
        val id = 1
        val clienteAtualizado = clienteCompleto(
            id = id,
            nome = "Cliente Atualizado",
            email = "atualizado@email.com",
            rg = "654321"
        )
        `when`(repository.findById(id)).thenReturn(Optional.of(clienteAtualizado))
        `when`(repository.save(any())).thenReturn(clienteAtualizado)
        val response = controller.atualizar(id, clienteAtualizado)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
    }

    @Test
    fun `atualizarParcial deve atualizar campos específicos do cliente`() {
        val id = 1
        val clienteExistente = clienteCompleto(id = id)
        val atualizacoes = mapOf(
            "nome" to "Nome Atualizado",
            "email" to "atualizado@email.com"
        )
        `when`(repository.findById(id)).thenReturn(Optional.of(clienteExistente))
        `when`(repository.save(any())).thenReturn(clienteExistente)
        val response = controller.atualizarParcial(id, atualizacoes)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
    }

    @Test
    fun `inativar deve desativar cliente com sucesso`() {
        val id = 1
        val cliente = clienteCompleto(id = id, isAtivo = true)
        `when`(repository.findById(id)).thenReturn(Optional.of(cliente))
        `when`(repository.save(any())).thenReturn(cliente.apply { isAtivo = false })
        val response = controller.inativar(id)
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        verify(repository).save(any())
    }

    @Test
    fun `ativar deve ativar cliente com sucesso`() {
        val id = 1
        val cliente = clienteCompleto(id = id, isAtivo = false)
        `when`(repository.findById(id)).thenReturn(Optional.of(cliente))
        `when`(repository.save(any())).thenReturn(cliente.apply { isAtivo = true })
        val response = controller.ativar(id)
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        verify(repository).save(any())
    }

    @Test
    fun `getRepository deve retornar o repositório injetado`() {
        val result = controller.repository
        assertNotNull(result)
        assertTrue(result is ClienteRepository)
    }
}