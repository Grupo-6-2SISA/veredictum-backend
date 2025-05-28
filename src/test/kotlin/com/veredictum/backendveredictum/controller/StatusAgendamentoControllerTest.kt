package com.veredictum.backendveredictum.controller

import com.veredictum.backendveredictum.entity.StatusAgendamento
import com.veredictum.backendveredictum.repository.StatusAgendamentoRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*
import kotlin.test.assertEquals

@WebMvcTest(StatusAgendamentoController::class)
class StatusAgendamentoControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var repository: StatusAgendamentoRepository

    @Test
    fun `deve retornar lista de status com sucesso`() {
        val statusList = listOf(
            StatusAgendamento(1, "Ativo"),
            StatusAgendamento(2, "Inativo")
        )
        `when`(repository.findAll()).thenReturn(statusList)

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/status-agendamento"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        verify(repository).findAll()
        assertEquals("[{\"idStatusAgendamento\":1,\"descricao\":\"Ativo\"},{\"idStatusAgendamento\":2,\"descricao\":\"Inativo\"}]", result.response.contentAsString)
    }

    @Test
    fun `deve retornar 204 quando lista de status estiver vazia`() {
        `when`(repository.findAll()).thenReturn(emptyList())

        mockMvc.perform(MockMvcRequestBuilders.get("/status-agendamento"))
            .andExpect(MockMvcResultMatchers.status().isNoContent)

        verify(repository).findAll()
    }

    @Test
    fun `deve retornar status por ID com sucesso`() {
        val status = StatusAgendamento(1, "Ativo")
        `when`(repository.findById(1)).thenReturn(Optional.of(status))

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/status-agendamento/1"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        verify(repository).findById(1)
        assertEquals("{\"idStatusAgendamento\":1,\"descricao\":\"Ativo\"}", result.response.contentAsString)
    }

    @Test
    fun `deve retornar 404 quando status por ID nao for encontrado`() {
        `when`(repository.findById(1)).thenReturn(Optional.empty())

        mockMvc.perform(MockMvcRequestBuilders.get("/status-agendamento/1"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)

        verify(repository).findById(1)
    }

    @Test
    fun `deve criar um novo status com sucesso`() {
        val novoStatus = StatusAgendamento(1, "Ativo")
        `when`(repository.save(any(StatusAgendamento::class.java))).thenReturn(novoStatus)

        val result = mockMvc.perform(MockMvcRequestBuilders.post("/status-agendamento")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"descricao\":\"Ativo\"}")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andReturn()

        verify(repository).save(any(StatusAgendamento::class.java))
        assertEquals("{\"idStatusAgendamento\":1,\"descricao\":\"Ativo\"}", result.response.contentAsString)
    }

    @Test
    fun `deve atualizar um status existente com sucesso`() {
        val statusExistente = StatusAgendamento(1, "Ativo")
        val statusAtualizado = StatusAgendamento(1, "Inativo")
        `when`(repository.findById(1)).thenReturn(Optional.of(statusExistente))
        `when`(repository.save(any(StatusAgendamento::class.java))).thenReturn(statusAtualizado)

        val result = mockMvc.perform(MockMvcRequestBuilders.put("/status-agendamento/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"descricao\":\"Inativo\"}")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        verify(repository).findById(1)
        verify(repository).save(any(StatusAgendamento::class.java))
        assertEquals("{\"idStatusAgendamento\":1,\"descricao\":\"Inativo\"}", result.response.contentAsString)
    }

    @Test
    fun `deve retornar 404 ao tentar atualizar um status inexistente`() {
        `when`(repository.findById(1)).thenReturn(Optional.empty())

        mockMvc.perform(MockMvcRequestBuilders.put("/status-agendamento/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"descricao\":\"Inativo\"}")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound)

        verify(repository).findById(1)
    }

    @Test
    fun `deve excluir um status existente com sucesso`() {
        val statusExistente = StatusAgendamento(1, "Ativo")
        `when`(repository.findById(1)).thenReturn(Optional.of(statusExistente))
        doNothing().`when`(repository).delete(statusExistente)

        mockMvc.perform(MockMvcRequestBuilders.delete("/status-agendamento/1"))
            .andExpect(MockMvcResultMatchers.status().isNoContent)

        verify(repository).findById(1)
        verify(repository).delete(statusExistente)
    }

    @Test
    fun `deve retornar 404 ao tentar excluir um status inexistente`() {
        `when`(repository.findById(1)).thenReturn(Optional.empty())

        mockMvc.perform(MockMvcRequestBuilders.delete("/status-agendamento/1"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)

        verify(repository).findById(1)
    }
}